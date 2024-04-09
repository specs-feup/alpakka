package pt.up.fe.specs.smali.ast;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.expr.literal.Literal;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;

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
        var fieldType = (TypeDescriptor) attributes.get("fieldType");

        sb.append(".field ");
        accessOrRestrictionList.forEach(a -> sb.append(a.getLabel()).append(" "));
        sb.append(name);
        sb.append(":");
        sb.append(fieldType.getCode());

        if (getChildren().size() > 0) {
            var i = 0;

            if (getChild(i) instanceof Literal) {
                sb.append(" = ");
                sb.append(getChild(i).getCode());
                i++;
            }

            for (; i < getChildren().size(); i++) {
                sb.append("\n");
                sb.append(indentCode(getChild(i).getCode()));

                if (i == getChildren().size() - 1) {
                    sb.append("\n.end field");
                }
            }
        }

        sb.append("\n");

        return sb.toString();
    }

    public String getField() {
        return (String) get(ATTRIBUTES).get("memberName") + ":"
                + ((TypeDescriptor) get(ATTRIBUTES).get("fieldType")).getCode();
    }

}
