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
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.RegisterReference;
import pt.up.fe.specs.alpakka.ast.type.Type;

import java.util.Collection;
import java.util.Optional;

public class LocalDirective extends Statement {

    public static final DataKey<Optional<String>> NAME = KeyFactory.optional("name");
    public static final DataKey<Optional<Type>> TYPE = KeyFactory.optional("type");

    public static final DataKey<Optional<String>> SIGNATURE = KeyFactory.optional("signature");

    public LocalDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public RegisterReference getRegister() {
        return getChild(RegisterReference.class);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        var register = getRegister();
        var name = get(NAME);
        var type = get(TYPE);
        var signature = get(SIGNATURE);

        sb.append(getLine());

        sb.append(".local ");

        sb.append(register.getCode());

        if (name.isPresent() && type.isPresent()) {
            sb.append(", ");
            sb.append(name.get());

            sb.append(":");
            sb.append(type.get().getCode());
        }

        signature.ifPresent(s -> sb.append(", ").append(s));

        return sb.toString();
    }

}
