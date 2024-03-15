package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public abstract class Statement extends SmaliNode {

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            () -> new HashMap<String, Object>());

    public Statement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    protected String getLineDirective() {
        var attributes = get(ATTRIBUTES);
        var lineDirective = (LineDirective) attributes.get("lineDirective");

        return lineDirective != null ? "\t" + lineDirective.getCode() + "\n" : "";
    }

}
