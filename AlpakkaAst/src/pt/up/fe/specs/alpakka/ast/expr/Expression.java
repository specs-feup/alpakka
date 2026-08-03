package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.type.TypeDescriptor;

import java.util.Collection;

public abstract class Expression extends SmaliNode {

    public final static DataKey<TypeDescriptor> TYPE = KeyFactory.object("type", TypeDescriptor.class);

    public Expression(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public void setType(TypeDescriptor type) {
        set(TYPE, type);
    }

    public TypeDescriptor getType() {
        return get(TYPE);
    }

}
