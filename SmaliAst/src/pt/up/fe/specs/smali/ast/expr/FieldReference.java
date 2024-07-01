package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;

public class FieldReference extends Expression implements Reference {

    public static String TYPE_LABEL = "field";

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
