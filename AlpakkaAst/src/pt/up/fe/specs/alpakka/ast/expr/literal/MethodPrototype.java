package pt.up.fe.specs.alpakka.ast.expr.literal;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;

import java.util.Collection;
import java.util.List;

public class MethodPrototype extends Literal {

    public static final DataKey<List<TypeDescriptor>> PARAMETERS = KeyFactory.list("parameters", TypeDescriptor.class);

    public static final DataKey<TypeDescriptor> RETURN_TYPE = KeyFactory.object("returnType", TypeDescriptor.class);

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

    public List<TypeDescriptor> getParameters() {
        return get(PARAMETERS);
    }

    public TypeDescriptor getReturnType() {
        return get(RETURN_TYPE);
    }

}
