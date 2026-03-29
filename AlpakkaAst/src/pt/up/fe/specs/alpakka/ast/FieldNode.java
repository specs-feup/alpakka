package pt.up.fe.specs.alpakka.ast;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;

import java.util.Collection;
import java.util.List;

public class FieldNode extends SmaliNode {

    public static final DataKey<String> MEMBER_NAME = KeyFactory.string("memberName");

    public static final DataKey<TypeDescriptor> FIELD_TYPE = KeyFactory.object("fieldType", TypeDescriptor.class);

    public static final DataKey<List<Modifier>> MODIFIERS = KeyFactory.list("accessOrRestrictionList", Modifier.class);


    public FieldNode(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var accessOrRestrictionList = getAccessList();
        var name = getFieldName();
        var fieldType = getFieldType();

        sb.append(".field ");
        accessOrRestrictionList.forEach(a -> sb.append(a.getLabel()).append(" "));
        sb.append(name);
        sb.append(":");
        sb.append(fieldType.getCode());

        if (!getChildren().isEmpty()) {
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

    public String getFieldName() {
        return get(MEMBER_NAME);
    }

    public TypeDescriptor getFieldType() {
        return get(FIELD_TYPE);
    }

    public String getFieldReferenceName() {
        var sb = new StringBuilder();
        var parentClassDescriptor = ((ClassNode) getParent()).getClassDescriptor();

        if (parentClassDescriptor != null) {
            sb.append(parentClassDescriptor.getCode()).append("->");
        }

        sb.append(getFieldName());
        sb.append(":");
        sb.append(getFieldType().getCode());

        return sb.toString();
    }

    public List<Modifier> getAccessList() {
        return get(MODIFIERS);
    }

    public boolean isStatic() {
        return getAccessList().contains(AccessSpec.STATIC);
    }

}
