package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

public class AnnotationElement extends Expression {

    public static final DataKey<String> NAME = KeyFactory.string("name");
    public static final DataKey<String> VALUE = KeyFactory.string("literal");

    public AnnotationElement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public SmaliNode getValue() {
        return getChild(SmaliNode.class, 0);
    }

    @Override
    public String getCode() {
        var name = get(NAME);
        var value = get(VALUE);

        return name + " = " + value;
    }

}
