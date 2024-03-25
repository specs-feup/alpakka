package pt.up.fe.specs.smali.ast.expr.literal;

import java.util.ArrayList;
import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;

public class MethodPrototype extends Literal {

    public MethodPrototype(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var attributes = get(ATTRIBUTES);
        var returnType = (TypeDescriptor) attributes.get("returnType");
        var parameters = (ArrayList<TypeDescriptor>) attributes.get("parameters");

        var builder = new StringBuilder();
        builder.append("(");
        parameters.forEach(p -> builder.append(p.getCode()));
        builder.append(")");

        builder.append(returnType.getCode());

        return builder.toString();
    }

}
