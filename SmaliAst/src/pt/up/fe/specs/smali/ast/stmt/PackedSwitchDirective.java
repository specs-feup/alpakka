package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.Literal;
import pt.up.fe.specs.smali.ast.stmt.instruction.Instruction;

public class PackedSwitchDirective extends Instruction {

    public PackedSwitchDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLineDirective());

        var key = (Literal) get(ATTRIBUTES).get("key");

        sb.append(".packed-switch " + key.getCode() + "\n");

        for (int i = 0; i < getChildren().size(); i++) {
            sb.append(indentCode(getChildren().get(i).getCode()) + "\n");
        }

        sb.append(".end packed-switch");

        return sb.toString();
    }

}
