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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.yaml.snakeyaml.Yaml;

import brut.apktool.Main;
import brut.common.BrutException;
import pt.up.fe.specs.smali.ast.App;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.context.SmaliContext;
import pt.up.fe.specs.util.SpecsIo;

public class SmaliParser {

    public Optional<SmaliNode> parse(List<File> sources, Integer targetSdkVersion) {
        var context = new SmaliContext();

        var classes = sources.stream()
                .map(file -> parseSingleFile(file, context, targetSdkVersion))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (classes.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(classes.get(0));
    }

    private Optional<SmaliNode> parseSingleFile(File source, SmaliContext context, Integer targetSdkVersion) {
        return switch (SpecsIo.getExtension(source).toLowerCase()) {
        case "apk" -> Optional.of(decompileApk(source, context));
        case "smali" -> new SmaliFileParser(source, context, targetSdkVersion).parse();
        default -> Optional.empty();
        };
    }

    private App decompileApk(File apkFile, SmaliContext context) {
        var outputFolder = SpecsIo.mkdir(apkFile.getName() + "_decompiled");

        String[] commands = { "d", "-f", apkFile.getAbsolutePath(), "-o", outputFolder.getAbsolutePath() };

        try {
            Main.main(commands);
        } catch (BrutException e) {
            throw new RuntimeException("Error decompiling APK", e);
        }

        var attributes = getAttributesFromYaml(outputFolder.getAbsolutePath() + "/apktool.yml");

        var targetSdkVersion = (Integer) ((HashMap<String, Object>) attributes.get("sdkInfo")).get("targetSdkVersion");

        var decompiledFiles = SpecsIo.getFilesRecursive(outputFolder);

        var children = decompiledFiles.stream()
                .map(file -> parseSingleFile(file, context, targetSdkVersion))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        SpecsIo.deleteFolder(outputFolder);

        var factory = context.get(SmaliContext.FACTORY);

        return factory.app(attributes, children);
    }

    private HashMap<String, Object> getAttributesFromYaml(String yamlFilePath) {
        var yaml = new Yaml();
        var data = SpecsIo.read(yamlFilePath);

        return yaml.load(data);
    }
}
