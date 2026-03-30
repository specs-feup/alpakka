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

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Manifest extends Resource {

    public static final DataKey<File> SOURCE = KeyFactory.file("source");

    public static final DataKey<String> PACKAGE_NAME = KeyFactory.string("packageName");

    public static final DataKey<HashMap<String, List<String>>> COMPONENTS = KeyFactory.generic("components", () -> new HashMap<String, List<String>>())
            .setDefault(HashMap::new);


    public Manifest(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public File getSource() {
        return get(SOURCE);
    }

    public String getPackageName() {
        return get(PACKAGE_NAME);
    }

    public List<String> getActivities() {
        var componentsMap = get(COMPONENTS);

        if (componentsMap.isEmpty()) {
            return List.of();
        }

        var activities = componentsMap.get("activity");

        if (activities == null) {
            return List.of();
        }

        return activities;
    }

    public List<String> getServices() {
        var componentsMap = get(COMPONENTS);

        if (componentsMap.isEmpty()) {
            return List.of();
        }

        var services = componentsMap.get("service");

        if (services == null) {
            return List.of();
        }

        return services;
    }

}
