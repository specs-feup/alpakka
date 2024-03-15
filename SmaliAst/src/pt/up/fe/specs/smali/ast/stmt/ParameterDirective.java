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

package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.LiteralRef;
import pt.up.fe.specs.smali.ast.expr.RegisterReference;

public class ParameterDirective extends Statement {

    public ParameterDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var register = (RegisterReference) get(ATTRIBUTES).get("register");
        var string = (LiteralRef) get(ATTRIBUTES).get("string");

        sb.append(getLineDirective());

        sb.append("\t.param " + register.getCode());

        if (string != null) {
            sb.append(", " + string.getCode());
        }

        sb.append("\n");

        for (var child : getChildren()) {
            sb.append("\t\t" + child.getCode() + "\n");
        }

        sb.append("\t.end param");

        return sb.toString();
    }

}
