package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;

import java.util.Collection;

public class MethodReference extends Expression implements Reference {

    public static String TYPE_LABEL = "method";

    public MethodReference(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var referenceTypeDescriptor = getParentClassDescriptor();
        var member = getMethodName();
        var prototype = getPrototype();

        var sb = new StringBuilder();

        if (referenceTypeDescriptor != null) {
            sb.append(referenceTypeDescriptor.getCode()).append("->");
        }

        sb.append(member);

        sb.append(prototype.getCode());

        return sb.toString();
    }

    public TypeDescriptor getParentClassDescriptor() {
        return (TypeDescriptor) get(SmaliNode.ATTRIBUTES).get("referenceTypeDescriptor");
    }

    public String getMethodName() {
        return (String) get(SmaliNode.ATTRIBUTES).get("memberName");
    }

    public MethodPrototype getPrototype() {
        return (MethodPrototype) get(SmaliNode.ATTRIBUTES).get("prototype");
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
