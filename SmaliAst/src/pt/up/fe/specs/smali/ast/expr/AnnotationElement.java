package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.Literal;

public class AnnotationElement extends Expression {

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            HashMap::new);

    public AnnotationElement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var attributes = get(ATTRIBUTES);
        var name = (String) attributes.get("name");
        var value = (Literal) attributes.get("value");

        return name + " = " + value.getCode();
    }

}
