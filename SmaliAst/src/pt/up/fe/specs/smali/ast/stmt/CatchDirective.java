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
import pt.up.fe.specs.smali.ast.expr.LabelRef;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;

public class CatchDirective extends Statement {

    public CatchDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var type = (TypeDescriptor) getExceptionTypeDescriptor();
        var from = (LabelRef) getTryStartLabelRef();
        var to = (LabelRef) getTryEndLabelRef();
        var label = (LabelRef) getCatchLabelRef();

        sb.append(getLineDirective());

        if (type != null) {
            sb.append(".catch ");
            sb.append(type.getCode());
        } else {
            sb.append(".catchall");
        }

        sb.append(" {");
        sb.append(from.getCode());
        sb.append(" .. ");
        sb.append(to.getCode());
        sb.append("} ");

        sb.append(label.getCode());

        return sb.toString();
    }

    public TypeDescriptor getExceptionTypeDescriptor() {
        return (TypeDescriptor) get(ATTRIBUTES).get("nonVoidTypeDescriptor");
    }

    public LabelRef getTryStartLabelRef() {
        return (LabelRef) get(ATTRIBUTES).get("from");
    }

    public LabelRef getTryEndLabelRef() {
        return (LabelRef) get(ATTRIBUTES).get("to");
    }

    public LabelRef getCatchLabelRef() {
        return (LabelRef) get(ATTRIBUTES).get("label");
    }

}
