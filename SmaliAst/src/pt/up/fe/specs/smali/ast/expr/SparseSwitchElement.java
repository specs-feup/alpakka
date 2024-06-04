package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.Literal;

public class SparseSwitchElement extends Expression {


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
