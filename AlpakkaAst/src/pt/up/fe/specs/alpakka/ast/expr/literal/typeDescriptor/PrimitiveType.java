package pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;

public class PrimitiveType extends TypeDescriptor {

    public static final DataKey<String> TYPE_DESCRIPTOR = KeyFactory.string("typeDescriptor");

    public PrimitiveType(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return get(TYPE_DESCRIPTOR);
    }

}
