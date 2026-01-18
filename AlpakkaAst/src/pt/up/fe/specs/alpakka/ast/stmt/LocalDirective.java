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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.alpakka.ast.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.RegisterReference;
import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;
import pt.up.fe.specs.alpakka.ast.expr.literal.PrimitiveLiteral;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;

import java.util.Collection;

public class LocalDirective extends Statement {

    public LocalDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var attributes = get(ATTRIBUTES);
        var register = (RegisterReference) attributes.get("register");
        var literal = (Literal) attributes.get("literal");
        var typeDescriptor = (TypeDescriptor) attributes.get("typeDescriptor");
        var signature = (PrimitiveLiteral) attributes.get("signature");

        sb.append(getLine());

        sb.append(".local ");

        sb.append(register.getCode());

        if (literal != null && typeDescriptor != null) {
            sb.append(", ");
            sb.append(literal.getCode());

            sb.append(":");
            sb.append(typeDescriptor.getCode());
        }

        if (signature != null) {
            sb.append(", ");
            sb.append(signature.getCode());
        }

        return sb.toString();
    }

}
