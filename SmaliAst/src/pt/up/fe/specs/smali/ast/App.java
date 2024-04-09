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

package pt.up.fe.specs.smali.ast;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import brut.apktool.Main;
import brut.common.BrutException;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

public class App extends SmaliNode {

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            () -> new HashMap<String, Object>());

    public App(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public File buildApk() {
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

        var attributes = get(ATTRIBUTES);
        var options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        var yaml = new Yaml(options);
        var apktoolYml = new File(outputFolder, "apktool.yml");
        SpecsIo.write(apktoolYml, yaml.dump(attributes));

        String[] commands = { "b", outputFolder.getAbsolutePath(), "-o", "output.apk" };

        try {
            Main.main(commands);
        } catch (BrutException e) {
            SpecsLogs.warn("Could not build apk: " + e.getMessage());
        }

        SpecsIo.deleteFolder(outputFolder);

        return new File("output.apk");
    }

    @Override
    public String getCode() {
        var children = getChildren();
        var sb = new StringBuilder();

        for (var child : children) {
            // Only show smali code
            if (child instanceof ClassNode) {
                sb.append(((ClassNode) child).getCode());
            }
        }

        return "App: " + sb.toString();
    }

}
