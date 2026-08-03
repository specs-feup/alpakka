package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;

import java.util.Collection;

public class SparseSwitchElement extends SmaliNode {


    public SparseSwitchElement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var key = (Literal) getChild(0);
        var label = this.getLabel();

        return key.getCode() + " -> " + label.getCode();
    }

    public LabelRef getLabel() {
        return (LabelRef) getChild(1);
    }

}
