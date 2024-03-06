package pt.up.fe.specs.smali.ast;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.type.Type;

public class FieldNode extends SmaliNode {

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            () -> new HashMap<String, Object>());

    public FieldNode(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var attributes = get(ATTRIBUTES);
        var accessOrRestrictionList = (List<Modifier>) attributes.get("accessOrRestrictionList");
        var name = (String) attributes.get("memberName");
        var fieldType = (Type) attributes.get("fieldType");

        sb.append(".field ");
        accessOrRestrictionList.forEach(a -> sb.append(a.getLabel()).append(" "));
        sb.append(name);
        sb.append(":");
        sb.append(fieldType.getCode());

        if (getChildren().size() > 0) {
            sb.append(" = ");
            sb.append(getChild(0).getCode());
        }

        sb.append("\n");

        return sb.toString();
    }

}
