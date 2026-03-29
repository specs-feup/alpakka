package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;

import java.util.Collection;

public class FieldReference extends Expression implements Reference {

    private static final String TYPE_LABEL = "field";

    public static String fieldLabel() {
        return TYPE_LABEL;
    }

    public static final DataKey<String> MEMBER_NAME = KeyFactory.string("memberName");
    public static final DataKey<TypeDescriptor> BASE_TYPE = KeyFactory.object("baseType", TypeDescriptor.class);
    public static final DataKey<TypeDescriptor> REFERENCE_TYPE = KeyFactory.object("referenceType", TypeDescriptor.class);

    public FieldReference(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        var attributes = get(ATTRIBUTES);

        var referenceTypeDescriptor = getParentClassDescriptor();
        var member = attributes.get("memberName");
        var nonVoidTypeDescriptor = getFieldReferenceType();

        if (referenceTypeDescriptor != null) {
            sb.append(referenceTypeDescriptor.getCode()).append("->");
        }

        sb.append(member).append(":");

        sb.append(nonVoidTypeDescriptor.getCode());

        return sb.toString();
    }

    public TypeDescriptor getParentClassDescriptor() {
        return (TypeDescriptor) get(ATTRIBUTES).get("referenceTypeDescriptor");
    }

    public TypeDescriptor getFieldReferenceType() {
        return (TypeDescriptor) get(ATTRIBUTES).get("nonVoidTypeDescriptor");
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
