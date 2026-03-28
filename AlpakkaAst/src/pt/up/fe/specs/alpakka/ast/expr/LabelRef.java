package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.MethodNode;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

public class LabelRef extends Expression implements Reference {

    private static final String TYPE_LABEL = "label";

    public static final DataKey<String> LABEL = KeyFactory.string("label");

    public LabelRef(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public static String typeLabel() {
        return TYPE_LABEL;
    }

    @Override
    public String getCode() {
        var name = get(LABEL);
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
