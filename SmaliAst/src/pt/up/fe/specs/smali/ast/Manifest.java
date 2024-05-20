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

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.util.SpecsIo;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manifest extends SmaliNode {

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            HashMap::new);

    public Manifest(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var file = getFile();

        return SpecsIo.read(file);
    }

    public File getFile() {
        return (File) get(ATTRIBUTES).get("file");
    }

    public String getPackageName() {
        return (String) get(ATTRIBUTES).get("packageName");
    }

    public List<String> getActivities() {
        var componentsMap = get(ATTRIBUTES).get("components");

        if (componentsMap == null) {
            return List.of();
        }

        return ((Map<String, List<String>>) componentsMap).get("activity");
    }

    public List<String> getServices() {
        var componentsMap = get(ATTRIBUTES).get("components");

        if (componentsMap == null) {
            return List.of();
        }

        return ((Map<String, List<String>>) componentsMap).get("service");
    }

}
