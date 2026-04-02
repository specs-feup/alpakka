package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.type.TypeDescriptor;

import java.util.Collection;
import java.util.Optional;

public class FieldReference extends Expression implements Reference {

    private static final String TYPE_LABEL = "field";

    public static String fieldLabel() {
        return TYPE_LABEL;
    }

    public static final DataKey<String> MEMBER_NAME = KeyFactory.string("memberName");
    public static final DataKey<Optional<TypeDescriptor>> BASE_TYPE = KeyFactory.optional("baseType");
    public static final DataKey<TypeDescriptor> FIELD_TYPE = KeyFactory.object("fieldType", TypeDescriptor.class);

    public FieldReference(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        var referenceTypeDescriptor = getParentClassDescriptor();
        var member = get(MEMBER_NAME);
        var nonVoidTypeDescriptor = getFieldReferenceType();

        referenceTypeDescriptor.ifPresent(type -> sb.append(type.getCode()).append("->"));

        sb.append(member).append(":");

        sb.append(nonVoidTypeDescriptor.getCode());

        return sb.toString();
    }

    public Optional<TypeDescriptor> getParentClassDescriptor() {
        return get(BASE_TYPE);
    }

    public TypeDescriptor getFieldReferenceType() {
        return get(FIELD_TYPE);
    }

    @Override
    public String getName() {
        return getCode();
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
    public String getTypeLabel() {
        return TYPE_LABEL;
    }

}
