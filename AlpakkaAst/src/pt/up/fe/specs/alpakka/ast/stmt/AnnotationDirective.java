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

package pt.up.fe.specs.alpakka.ast.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.AnnotationVisibility;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.type.ClassType;

import java.util.Collection;

public class AnnotationDirective extends Statement {

    public static final DataKey<AnnotationVisibility> VISIBILITY = KeyFactory.enumeration("visibility", AnnotationVisibility.class);

    public static final DataKey<ClassType> CLASS_DESCRIPTOR = KeyFactory.object("classDescriptor", ClassType.class);

    public AnnotationDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var visibility = get(VISIBILITY);
        var classDescriptor = get(CLASS_DESCRIPTOR);

        sb.append(getLine());

        sb.append(".annotation " + visibility.getVisibility() + " " + classDescriptor.getCode() + "\n");

        for (var child : getChildren()) {
            sb.append(indentCode(child.getCode()) + "\n");
        }

        sb.append(".end annotation");

        return sb.toString();
    }

}
