/**
 * Copyright 2024 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.alpakka.ast;

import brut.androlib.ApkBuilder;
import brut.androlib.Config;
import brut.common.BrutException;
import brut.directory.ExtFile;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import pt.up.fe.specs.util.SpecsIo;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App extends SmaliNode {

    private static final int DEFAULT_SDK_VERSION = 20;

    public static int getDefaultSdkVersion() {
        return DEFAULT_SDK_VERSION;
    }

    public static final DataKey<Integer> SDK_VERSION = KeyFactory.integer("sdkVersion");

    public static final DataKey<Optional<File>> APKTOOL_YAML = KeyFactory.optional("apktoolYaml");

    public App(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public static HashMap<String, Object> getAttributesFromYaml(File yamlFile) {
        var cleanYaml = fixYamlContent(SpecsIo.read(yamlFile));

        var yaml = new Yaml();

        HashMap<String, Object> map = yaml.load(cleanYaml);

        return map != null ? map : new HashMap<>();
    }

    private static String fixYamlContent(String content) {
        String regex = "(\\s*:\\s*)(@[^\\s]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(result, "$1\"$2\"");
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private Map<String, Object> getYamlMap() {
        var yamlFile = get(APKTOOL_YAML);
        if (yamlFile.isPresent()) {
            return getAttributesFromYaml(yamlFile.get());
        }

        var attributes = new HashMap<String, Object>();
        var sdkInfo = new HashMap<String, Object>();
        sdkInfo.put("targetSdkVersion", get(SDK_VERSION));
        attributes.put("sdkInfo", sdkInfo);

        return attributes;
    }

    public void buildApk(String outputName) {
        var outputFolder = SpecsIo.mkdir("output");

        for (var child : getChildren()) {
            if (child instanceof ClassNode) {
                var classPath = ((ClassNode) child).getDexClassName() + "/"
                        + ((ClassNode) child).getClassDescriptor().getPackageName() + "/"
                        + ((ClassNode) child).getClassDescriptor().getClassName();
                var file = new File(outputFolder, classPath + ".smali");
                SpecsIo.write(file, child.getCode());

            } else if (child instanceof Resource) {
                var file = ((Resource) child).getFile();
                var filePath = file.getPath();
                var path = filePath.substring(filePath.indexOf(File.separator) + 1);
                SpecsIo.copy(file, new File(outputFolder, path));

            }
        }

        var attributes = getYamlMap();
        var options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        var yaml = new Yaml(options);
        var apktoolYml = new File(outputFolder, "apktool.yml");
        SpecsIo.write(apktoolYml, yaml.dump(attributes));

        var config = new Config();

        var outName = outputName.isBlank() ? "output.apk" : outputName;
        if (!outName.endsWith(".apk")) {
            outName += ".apk";
        }

        var outputFile = new File(outName);

        try {
            new ApkBuilder(new ExtFile(outputFolder), config).build(outputFile);
        } catch (BrutException e) {
            throw new RuntimeException("Could not build apk", e);
        }

        SpecsIo.deleteFolder(outputFolder);
    }

    @Override
    public String getCode() {
        var children = getChildren();
        var sb = new StringBuilder();

        for (var child : children) {
            // Only show smali code
            if (child instanceof ClassNode) {
                sb.append(child.getCode());
            }
        }

        return "App: " + sb;
    }

    public Manifest getManifest() {
        return (Manifest) getChildren().stream()
                .filter(c -> c instanceof Manifest)
                .findFirst()
                .orElseThrow();
    }

    public List<ClassNode> getClasses() {
        return getChildren().stream()
                .filter(c -> c instanceof ClassNode)
                .map(c -> (ClassNode) c)
                .toList();
    }

}
