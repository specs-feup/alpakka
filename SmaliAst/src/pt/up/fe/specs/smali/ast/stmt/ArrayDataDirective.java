package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.Literal;

public class ArrayDataDirective extends Statement {

    public ArrayDataDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLine());

        var width = (Literal) get(ATTRIBUTES).get("elementWidth");

        sb.append(".array-data " + width.getCode() + "\n");

        for (int i = 0; i < getChildren().size(); i++) {
            sb.append(indentCode(getChildren().get(i).getCode()) + "\n");
        }

        sb.append(".end array-data");

        return sb.toString();
    }

}
