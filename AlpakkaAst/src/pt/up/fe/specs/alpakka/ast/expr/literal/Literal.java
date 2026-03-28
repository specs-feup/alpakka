package pt.up.fe.specs.alpakka.ast.expr.literal;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.Expression;

import java.util.Collection;

public abstract class Literal extends Expression {

    public static final DataKey<String> VALUE = KeyFactory.string("value");

    public Literal(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        // TODO: remove when no ATTRIBUTES are used
        if (hasValue(ATTRIBUTES) && get(ATTRIBUTES).containsKey("value")) {
            return (String) get(ATTRIBUTES).get("value");
        }

        return get(VALUE);
    }

}
