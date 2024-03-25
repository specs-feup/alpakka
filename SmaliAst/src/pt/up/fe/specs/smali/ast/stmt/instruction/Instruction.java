package pt.up.fe.specs.smali.ast.stmt.instruction;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.Statement;

public abstract class Instruction extends Statement {

    public Instruction(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var attributes = get(ATTRIBUTES);

        sb.append(getLineDirective());

        sb.append(attributes.get("instruction") + " ");

        var children = getChildren();

        for (var child : children) {
            sb.append(child.getCode());
            if (children.indexOf(child) < children.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

}
