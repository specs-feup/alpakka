package pt.up.fe.specs.smali.ast;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.expr.LiteralRef;
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
		var accessOrRestrictionList = (List<String>) attributes.get("accessOrRestrictionList");
		var name = (String) attributes.get("memberName");
		var fieldType = (Type) attributes.get("fieldType");
		var literal = (LiteralRef) attributes.get("literal");

		sb.append(".field ");
		accessOrRestrictionList.forEach(a -> sb.append(a).append(" "));
		sb.append(name);
		sb.append(":");
		sb.append(fieldType.getCode());

		if (literal != null) {
			sb.append(" = ");
			sb.append(literal.getCode());
		}

		sb.append("\n");

		return sb.toString();
	}

}
