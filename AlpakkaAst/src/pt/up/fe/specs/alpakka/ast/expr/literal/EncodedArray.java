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

package pt.up.fe.specs.alpakka.ast.expr.literal;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.Expression;
import pt.up.fe.specs.alpakka.ast.type.TypeDescriptor;
import pt.up.fe.specs.util.SpecsCheck;

import java.util.Collection;

public class EncodedArray extends SmaliNode {

    public EncodedArray(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append("{");

        var children = getChildren();

        for (int i = 0; i < children.size(); i++) {
            sb.append("\n").append(indentCode(children.get(i).getCode()));

            if (i < children.size() - 1) {
                sb.append(",");
            } else {
                sb.append("\n");
            }
        }

        sb.append("}");

        return sb.toString();
    }

    public TypeDescriptor getType() {
        SpecsCheck.checkArgument(!getChildren().isEmpty(), () -> "Encoded array must have at least one element to determine type");

        var child = getChildren().get(0);

        if (!(child instanceof Expression expr)) {
            throw new RuntimeException("Expected first child to be an expression: " + child);
        }

        return expr.getType();
    }

}
