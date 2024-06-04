package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.Literal;

public class AnnotationElement extends Expression {

    public AnnotationElement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var attributes = get(ATTRIBUTES);
        var name = (String) attributes.get("name");
        var value = (Literal) attributes.get("value");

        return name + " = " + value.getCode();
    }

}
