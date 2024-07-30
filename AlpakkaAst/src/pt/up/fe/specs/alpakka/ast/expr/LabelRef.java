package pt.up.fe.specs.alpakka.ast.expr;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.MethodNode;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

public class LabelRef extends Expression implements Reference {

    public static String TYPE_LABEL = "label";

    public LabelRef(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var name = get(ATTRIBUTES).get("label");
        return ":" + name;
    }

    @Override
    public String getName() {
        var parentMethod = getParent();
        while (parentMethod != null && !(parentMethod instanceof MethodNode)) {
            parentMethod = parentMethod.getParent();
        }

        if (parentMethod == null) {
            return null;
        }

        return ((MethodNode) parentMethod).getMethodReferenceName() +
                getCode();
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
