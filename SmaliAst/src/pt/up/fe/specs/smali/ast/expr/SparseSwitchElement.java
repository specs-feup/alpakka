package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class SparseSwitchElement extends Expression {

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            () -> new HashMap<String, Object>());

    public SparseSwitchElement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var attributes = get(ATTRIBUTES);
        var key = (LiteralRef) attributes.get("key");
        var label = (LabelRef) attributes.get("label");

        return key.getCode() + " -> " + label.getCode();
    }

}
