package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.type.Type;

import java.util.Collection;

public abstract class Expression extends SmaliNode {

    public final static DataKey<Type> TYPE = KeyFactory.object("type", Type.class);

    public Expression(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public void setType(Type type) {
        set(TYPE, type);
    }

    public Type getType() {
        return get(TYPE);
    }

}
