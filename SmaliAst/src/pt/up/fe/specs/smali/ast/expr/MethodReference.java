package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.ClassNode;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;

public class MethodReference extends Expression implements Reference {

    public static String TYPE_LABEL = "method";

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            HashMap::new);

    public MethodReference(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var referenceTypeDescriptor = getReferenceTypeDescriptor();
        var member = getMemberName();
        var prototype = getPrototype();

        var sb = new StringBuilder();

        if (referenceTypeDescriptor != null) {
            sb.append(referenceTypeDescriptor.getCode()).append("->");
        }

        sb.append(member);

        sb.append(prototype.getCode());

        return sb.toString();
    }

    public TypeDescriptor getReferenceTypeDescriptor() {
        return (TypeDescriptor) get(ATTRIBUTES).get("referenceTypeDescriptor");
    }

    public String getMemberName() {
        return (String) get(ATTRIBUTES).get("memberName");
    }

    public MethodPrototype getPrototype() {
        return (MethodPrototype) get(ATTRIBUTES).get("prototype");
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
