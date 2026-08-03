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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ParameterDirective extends Statement {

    public static final DataKey<Optional<String>> NAME = KeyFactory.optional("name");

    public ParameterDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public RegisterReference getRegister() {
        return getChild(RegisterReference.class);
    }

    public List<AnnotationDirective> getAnnotations() {
        return getChildren(AnnotationDirective.class, 1);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var register = getRegister();
        var name = get(NAME);

        sb.append(getLine());

        sb.append(".param " + register.getCode());

        name.ifPresent(n -> sb.append(", " + n));

        sb.append("\n");

        for (var child : getAnnotations()) {
            sb.append(indentCode(child.getCode()) + "\n");
        }

        sb.append(".end param");

        return sb.toString();
    }

}
