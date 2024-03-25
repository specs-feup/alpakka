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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.yaml.snakeyaml.Yaml;

import brut.apktool.Main;
import brut.common.BrutException;
import pt.up.fe.specs.smali.ast.App;
import pt.up.fe.specs.smali.ast.Resource;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.context.SmaliContext;
import pt.up.fe.specs.util.SpecsIo;

public class SmaliParser {

    private static final String DECOMPILATION_FOLDERNAME = "decompiledApp";

    private static String organizationName = null;

    public Optional<App> parse(List<File> sources, Integer targetSdkVersion) {
        var context = new SmaliContext();

        var classes = sources.stream()
                .map(file -> parseSingleFile(file, context, targetSdkVersion))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (classes.isEmpty()) {
            return Optional.empty();
        }

        if (classes.size() == 1 && classes.get(0) instanceof App) {
            return Optional.of((App) classes.get(0));
        } else {
            var factory = context.get(SmaliContext.FACTORY);
            var attributes = new HashMap<String, Object>();
            attributes.put("sdkInfo", new HashMap<String, Object>().put("targetSdkVersion", targetSdkVersion));

            return Optional.of(factory.app(attributes, classes));
        }
    }

    private Optional<SmaliNode> parseSingleFile(File source, SmaliContext context, Integer targetSdkVersion) {
        if (source.getPath().equals(DECOMPILATION_FOLDERNAME + File.separator + "apktool.yml")) {
            return Optional.empty();
        }

        return switch (SpecsIo.getExtension(source).toLowerCase()) {
        case "apk" -> Optional.of(decompileApk(source, context));
        case "smali" -> {
            // Filter smali files from different organizations
            if (organizationName != null && !source.getPath().contains(organizationName)) {
                yield Optional.of(newResourceNode(source, context));
            } else {
                yield new SmaliFileParser(source, context, targetSdkVersion).parse();
            }
        }
        default -> {
            yield Optional.of(newResourceNode(source, context));
        }
        };
    }

    private Resource newResourceNode(File source, SmaliContext context) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = new HashMap<String, Object>();
        attributes.put("file", source);
        return factory.resource(attributes);
    }

    private App decompileApk(File apkFile, SmaliContext context) {
        var outputFolder = SpecsIo.mkdir(DECOMPILATION_FOLDERNAME);

        String[] commands = { "d", "-f", apkFile.getAbsolutePath(), "-o", outputFolder.getAbsolutePath() };

        try {
            Main.main(commands);
        } catch (BrutException e) {
            throw new RuntimeException("Error decompiling APK", e);
        }

        var attributes = getAttributesFromYaml(outputFolder.getAbsolutePath() + "/apktool.yml");

        var packageName = getPackageNameFromManifest(outputFolder.getAbsolutePath() + "/AndroidManifest.xml")
                .split("\\.");
        organizationName = packageName[0] + File.separator + packageName[1];

        var targetSdkVersion = (Integer) ((HashMap<String, Object>) attributes.get("sdkInfo")).get("targetSdkVersion");

        var decompiledFiles = SpecsIo.getFilesRecursive(outputFolder);

        var children = decompiledFiles.stream()
                .map(file -> parseSingleFile(file, context, targetSdkVersion))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        // SpecsIo.deleteFolder(outputFolder);

        var factory = context.get(SmaliContext.FACTORY);

        return factory.app(attributes, children);
    }

    private HashMap<String, Object> getAttributesFromYaml(String yamlFilePath) {
        var yaml = new Yaml();
        var data = SpecsIo.read(yamlFilePath);

        return yaml.load(data);
    }

    private String getPackageNameFromManifest(String filePath) {
        String packageName = null;
        try {
            var inputFile = new File(filePath);
            var factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            var doc = builder.parse(inputFile);
            var root = doc.getDocumentElement();
            packageName = root.getAttribute("package");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return packageName;
    }
}
