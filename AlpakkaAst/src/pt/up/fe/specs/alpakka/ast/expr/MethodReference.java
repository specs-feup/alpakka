package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.type.MethodPrototype;
import pt.up.fe.specs.alpakka.ast.type.TypeDescriptor;

import java.util.Collection;
import java.util.Optional;

public class MethodReference extends Expression implements Reference {

    private static final String TYPE_LABEL = "method";

    public static String methodLabel() {
        return TYPE_LABEL;
    }

    public static final DataKey<String> METHOD_NAME = KeyFactory.string("methodName");

    public static final DataKey<MethodPrototype> METHOD_TYPE = KeyFactory.object("methodType", MethodPrototype.class);

    public static final DataKey<Optional<TypeDescriptor>> BASE_TYPE = KeyFactory.optional("baseType");

    public MethodReference(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var baseType = getBaseType();
        var member = getMethodName();
        var prototype = getPrototype();

        var sb = new StringBuilder();

        baseType.ifPresent(type -> sb.append(type.getCode()).append("->"));

        sb.append(member);

        sb.append(prototype.getCode());

        return sb.toString();
    }

    public Optional<TypeDescriptor> getBaseType() {
        return get(BASE_TYPE);
    }

    public String getMethodName() {
        return get(METHOD_NAME);
    }

    public MethodPrototype getPrototype() {
        return get(METHOD_TYPE);
    }

    @Override
    public void setDeclaration(SmaliNode decl) {
        set(DECL, decl);
    }

    @Override
    public SmaliNode getDeclaration() {
        return get(DECL);
    }

    @Override
    public String getName() {
        return this.getCode();
    }

    @Override
    public String getTypeLabel() {
        return TYPE_LABEL;
    }

}
