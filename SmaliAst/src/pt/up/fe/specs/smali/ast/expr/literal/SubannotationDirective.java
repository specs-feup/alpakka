package pt.up.fe.specs.smali.ast.expr.literal;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.ClassType;

public class SubannotationDirective extends Literal {

    public SubannotationDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var classDescriptor = (ClassType) get(ATTRIBUTES).get("classDescriptor");

        sb.append(".subannotation ").append(classDescriptor.getCode()).append("\n");

        for (var child : getChildren()) {
            sb.append(indentCode(child.getCode())).append("\n");
        }

        sb.append(".end subannotation");

        return sb.toString();
    }

}
