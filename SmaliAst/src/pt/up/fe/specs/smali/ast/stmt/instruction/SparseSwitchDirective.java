package pt.up.fe.specs.smali.ast.stmt.instruction;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class SparseSwitchDirective extends Instruction {

    public SparseSwitchDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLineDirective());

        sb.append(".sparse-switch\n");

        var children = getChildren();

        for (var child : children) {
            sb.append(indentCode(child.getCode()) + "\n");
        }

        sb.append(".end sparse-switch");

        return sb.toString();
    }

}
