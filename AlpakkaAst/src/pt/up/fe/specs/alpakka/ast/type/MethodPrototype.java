package pt.up.fe.specs.alpakka.ast.type;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;
import java.util.List;

public class MethodPrototype extends Type {

    public static final DataKey<List<Type>> PARAMETERS = KeyFactory.list("parameters", Type.class);

    public static final DataKey<Type> RETURN_TYPE = KeyFactory.object("returnType", Type.class);

    public MethodPrototype(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var returnType = getReturnType();
        var parameters = getParameters();

        var builder = new StringBuilder();
        builder.append("(");
        parameters.forEach(p -> builder.append(p.getCode()));
        builder.append(")");

        builder.append(returnType.getCode());

        return builder.toString();
    }

    public List<Type> getParameters() {
        return get(PARAMETERS);
    }

    public Type getReturnType() {
        return get(RETURN_TYPE);
    }

}
