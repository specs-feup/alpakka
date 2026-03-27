package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;

import java.util.Collection;

public class AnnotationElement extends Expression {

    public static final DataKey<String> NAME = KeyFactory.string("name");

    // TODO: Probably this should be a child
    public static final DataKey<Literal> VALUE = KeyFactory.object("literal", Literal.class);

    public AnnotationElement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var name = get(NAME);
        var value = get(VALUE);

        return name + " = " + value.getCode();
    }

}
