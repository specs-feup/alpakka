/**
 * Copyright 2024 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.smali.parser.antlr;

import brut.androlib.ApkDecoder;
import brut.androlib.Config;
import brut.androlib.exceptions.AndrolibException;
import brut.directory.DirectoryException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;
import pt.up.fe.specs.smali.ast.*;
import pt.up.fe.specs.smali.ast.context.SmaliContext;
import pt.up.fe.specs.smali.ast.expr.FieldReference;
import pt.up.fe.specs.smali.ast.expr.LabelRef;
import pt.up.fe.specs.smali.ast.expr.MethodReference;
import pt.up.fe.specs.smali.ast.expr.Reference;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.ClassType;
import pt.up.fe.specs.smali.ast.stmt.Label;
import pt.up.fe.specs.util.SpecsIo;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pt.up.fe.specs.smali.ast.SmaliNode.ATTRIBUTES;

public class SmaliParser {

    private static final String DECOMPILATION_FOLDERNAME = "decompiledApp";

    public Optional<App> parse(List<File> sources, List<String> options) {
        var context = new SmaliContext();

        var classes = sources.stream()
                .map(file -> parseSingleFile(file, context, options))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (classes.isEmpty()) {
            return Optional.empty();
        }

        // This needs to be changed for multiple files
        var declarationsMap = new HashMap<String, Map<String, SmaliNode>>();
        collectDeclarations(classes.get(0), declarationsMap);
        replaceReferences(classes.get(0), declarationsMap);

        if (classes.size() == 1 && classes.get(0) instanceof App) {
            return Optional.of((App) classes.get(0));
        } else {
            var targetSdkVersion = options.stream()
                    .filter(option -> option.startsWith("-targetSdkVersion"))
                    .map(option -> Integer.parseInt(option.substring("-targetSdkVersion".length())))
                    .findFirst()
                    .orElse(20);

            var factory = context.get(SmaliContext.FACTORY);
            var attributes = new HashMap<String, Object>();
            attributes.put("sdkInfo", new HashMap<String, Object>().put("targetSdkVersion", targetSdkVersion));

            return Optional.of(factory.app(attributes, classes));
        }
    }

    private Optional<SmaliNode> parseSingleFile(File source, SmaliContext context, List<String> options) {
        if (source.getPath().equals(DECOMPILATION_FOLDERNAME + File.separator + "apktool.yml")) {
            return Optional.empty();
        }

        if (source.getPath().equals(DECOMPILATION_FOLDERNAME + File.separator + "AndroidManifest.xml")) {
            return Optional.of(newManifestNode(source, context));
        }

        var targetSdkVersion = options.stream()
                .filter(option -> option.startsWith("-targetSdkVersion"))
                .map(option -> Integer.parseInt(option.substring("-targetSdkVersion".length())))
                .findFirst()
                .orElse(20);

        var packageFilter = options.stream()
                .filter(option -> option.startsWith("-packageFilter"))
                .map(option -> option.substring("-packageFilter".length()))
                .findFirst()
                .orElse("")
                .replace(".", File.separator);

        return switch (SpecsIo.getExtension(source).toLowerCase()) {
        case "apk" -> Optional.of(decompileApk(source, context, options));
        case "smali" -> {
            if (!source.getPath().contains(packageFilter)) {
                yield Optional.of(newResourceNode(source, context));
            } else {
                yield new SmaliFileParser(source, context, targetSdkVersion).parse();
            }
        }
        default -> Optional.of(newResourceNode(source, context));
        };
    }

    private void collectDeclarations(SmaliNode node, Map<String, Map<String, SmaliNode>> declarationsMap) {
        if (node instanceof Label) {
            declarationsMap.computeIfAbsent(LabelRef.TYPE_LABEL, k -> new HashMap<>())
                    .put(((Label) node).getLabelReferenceName(), node);
        } else if (node instanceof FieldNode) {
            declarationsMap.computeIfAbsent(FieldReference.TYPE_LABEL, k -> new HashMap<>())
                    .put(((FieldNode) node).getFieldReferenceName(), node);
        } else if (node instanceof MethodNode) {
            declarationsMap.computeIfAbsent(MethodReference.TYPE_LABEL, k -> new HashMap<>())
                    .put(((MethodNode) node).getMethodReferenceName(), node);
        } else if (node instanceof ClassNode) {
            declarationsMap.computeIfAbsent(ClassType.TYPE_LABEL, k -> new HashMap<>())
                    .put(((ClassNode) node).getClassDescriptor().getCode(), node);
        }

        node.getChildren().forEach(child -> collectDeclarations(child, declarationsMap));
    }

    private void replaceReferences(SmaliNode node, Map<String, Map<String, SmaliNode>> declarationsMap) {
        SmaliNode declaration = null;

        if (node instanceof Reference) {
            declaration = declarationsMap.getOrDefault(((Reference) node).getTypeLabel(), new HashMap<>())
                    .get(((Reference) node).getName());
        }

        if (declaration != null) {
            ((Reference) node).setDeclaration(declaration);
        }

        node.getChildren().forEach(child -> replaceReferences(child, declarationsMap));

        if (node.get(ATTRIBUTES) != null) {
            node.get(ATTRIBUTES).values().forEach(value -> {
                if (value instanceof SmaliNode) {
                    replaceReferences((SmaliNode) value, declarationsMap);
                } else if (value instanceof List) {
                    ((List<?>) value).forEach(listValue -> {
                        if (listValue instanceof SmaliNode) {
                            replaceReferences((SmaliNode) listValue, declarationsMap);
                        }
                    });
                }
            });
        }

    }

    private Resource newResourceNode(File source, SmaliContext context) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = new HashMap<String, Object>();
        attributes.put("file", source);
        return factory.resource(attributes);
    }

    private Manifest newManifestNode(File source, SmaliContext context) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = new HashMap<String, Object>();
        attributes.put("file", source);
        attributes.put("packageName", getPackageNameFromManifest(source.getAbsolutePath()));

        var components = new HashMap<String, List<String>>();
        var manifest = parseXML(source);
        var application = manifest.getElementsByTagName("application");
        var applicationComponents = application.getLength() > 0 ? application.item(0).getChildNodes() : manifest.getChildNodes();

        for (int i = 0; i < applicationComponents.getLength(); i++) {
            var component = applicationComponents.item(i);
            if (component.getNodeName().equals("activity") || component.getNodeName().equals("service")) {
                if (components.containsKey(component.getNodeName())) {
                    var componentList = components.get(component.getNodeName());
                    componentList.add(component.getAttributes().getNamedItem("android:name").getNodeValue());
                } else {
                    var componentList = new ArrayList<String>();
                    componentList.add(component.getAttributes().getNamedItem("android:name").getNodeValue());
                    components.put(component.getNodeName(), componentList);
                }
            }
        }

        attributes.put("components", components);

        return factory.manifest(attributes);
    }

    private App decompileApk(File apkFile, SmaliContext context, List<String> options) {
        var outputFolder = SpecsIo.mkdir(DECOMPILATION_FOLDERNAME);

        var config = Config.getDefaultConfig();
        config.forceDelete = true;

        var decoder = new ApkDecoder(config, apkFile);

        try {
            decoder.decode(outputFolder);
        } catch (AndrolibException | IOException | DirectoryException e) {
            throw new RuntimeException("Error decompiling APK", e);
        }

        var attributes = getAttributesFromYaml(outputFolder.getAbsolutePath() + "/apktool.yml");

        var sdkInfo = (HashMap<String, Object>) attributes.get("sdkInfo");

        if (sdkInfo != null) {
            options.removeIf(option -> option.startsWith("-targetSdkVersion"));
            options.add("-targetSdkVersion" + sdkInfo.get("targetSdkVersion"));
        }

        var decompiledFiles = SpecsIo.getFilesRecursive(outputFolder);

        var children = decompiledFiles.stream()
                .map(file -> parseSingleFile(file, context, options))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        // SpecsIo.deleteFolder(outputFolder);

        var factory = context.get(SmaliContext.FACTORY);

        return factory.app(attributes, children);
    }

    private HashMap<String, Object> getAttributesFromYaml(String yamlFilePath) {
        var cleanYaml = fixYamlContent(SpecsIo.read(yamlFilePath));

        var yaml = new Yaml();

        return yaml.load(cleanYaml);
    }

    private static String fixYamlContent(String content) {
        String regex = "(\\s*:\\s*)(@[^\\s]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, "$1\"$2\"");
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private String getPackageNameFromManifest(String filePath) {
        var inputFile = new File(filePath);
        var doc = parseXML(inputFile);
        var root = doc.getDocumentElement();
        return root.getAttribute("package");
    }

    private Document parseXML(File file) {
        try {
            var factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            return builder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException("Could not parse XML file", e);
        }
    }
}
