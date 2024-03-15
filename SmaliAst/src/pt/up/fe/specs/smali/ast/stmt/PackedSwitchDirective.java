package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.LiteralRef;

public class PackedSwitchDirective extends Instruction {

    public PackedSwitchDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLineDirective());

        var key = (LiteralRef) get(ATTRIBUTES).get("key");

        sb.append("\t.packed-switch " + key.getCode() + "\n");

        for (int i = 0; i < getChildren().size(); i++) {
            sb.append("\t\t" + getChildren().get(i).getCode() + "\n");
        }

        sb.append("\t.end packed-switch");

        return sb.toString();
    }

}
