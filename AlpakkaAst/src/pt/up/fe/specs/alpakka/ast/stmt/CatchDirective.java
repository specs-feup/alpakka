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

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.LabelRef;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;

public class CatchDirective extends Statement {

    public CatchDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var type = (TypeDescriptor) getExceptionTypeDescriptor();
        var tryStartLabel = getTryStartLabelRef();
        var tryEndLabel = getTryEndLabelRef();
        var catchLabel = getCatchLabelRef();

        sb.append(getLine());

        if (type != null) {
            sb.append(".catch ");
            sb.append(type.getCode());
        } else {
            sb.append(".catchall");
        }

        sb.append(" {");
        sb.append(tryStartLabel.getCode());
        sb.append(" .. ");
        sb.append(tryEndLabel.getCode());
        sb.append("} ");

        sb.append(catchLabel.getCode());

        return sb.toString();
    }

    public TypeDescriptor getExceptionTypeDescriptor() {
        return (TypeDescriptor) get(ATTRIBUTES).get("nonVoidTypeDescriptor");
    }

    public LabelRef getTryStartLabelRef() {
        return (LabelRef) getChild(0);
    }

    public LabelRef getTryEndLabelRef() {
        return (LabelRef) getChild(1);
    }

    public LabelRef getCatchLabelRef() {
        return (LabelRef) getChild(2);
    }

}
