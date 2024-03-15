package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.LiteralRef;

public class ArrayDataDirective extends Instruction {

    public ArrayDataDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLineDirective());

        var width = (LiteralRef) get(ATTRIBUTES).get("elementWidth");

        sb.append("\t.array-data " + width.getCode() + "\n");

        for (int i = 0; i < getChildren().size(); i++) {
            sb.append("\t\t" + getChildren().get(i).getCode() + "\n");
        }

        sb.append("\t.end array-data");

        return sb.toString();
    }

}
