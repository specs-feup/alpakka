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
import pt.up.fe.specs.util.SpecsIo;

import java.io.File;
import java.util.Collection;

public class Resource extends SmaliNode {

    public static final DataKey<File> SOURCE = KeyFactory.file("source");

    public Resource(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return SpecsIo.read(getFile());
    }

    public File getFile() {
        return get(SOURCE);
    }

}
