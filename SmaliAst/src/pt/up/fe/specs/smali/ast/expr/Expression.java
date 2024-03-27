package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;

public abstract class Expression extends SmaliNode {

    public final static DataKey<Optional<TypeDescriptor>> TYPE = KeyFactory.optional("type");

    public Expression(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public void setType(TypeDescriptor type) {
        set(TYPE, Optional.of(type));
    }

    public TypeDescriptor getType() {
        return get(TYPE).orElse(null);
    }

}
