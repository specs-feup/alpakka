package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class LabelRef extends Expression implements Reference {

    public static String TYPE_LABEL = "label";

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            () -> new HashMap<String, Object>());

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
        return (String) get(ATTRIBUTES).get("label");
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
