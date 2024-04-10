package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;

public class FieldReference extends Expression implements Reference {

    public static String TYPE_LABEL = "field";

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            HashMap::new);

    public FieldReference(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        var attributes = get(ATTRIBUTES);

        var referenceTypeDescriptor = (TypeDescriptor) attributes.get("referenceTypeDescriptor");
        var member = attributes.get("memberName");
        var nonVoidTypeDescriptor = (TypeDescriptor) attributes.get("nonVoidTypeDescriptor");

        if (referenceTypeDescriptor != null) {
            sb.append(referenceTypeDescriptor.getCode()).append("->");
        }

        sb.append(member).append(":");

        sb.append(nonVoidTypeDescriptor.getCode());

        return sb.toString();
    }

    public TypeDescriptor getNonVoidTypeDescriptor() {
        return (TypeDescriptor) get(ATTRIBUTES).get("nonVoidTypeDescriptor");
    }

    @Override
    public String getName() {
        return get(ATTRIBUTES).get("memberName") + ":"
                + ((TypeDescriptor) get(ATTRIBUTES).get("nonVoidTypeDescriptor")).getCode();
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
