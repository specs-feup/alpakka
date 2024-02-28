package pt.up.fe.specs.smali.ast;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.expr.LiteralRef;

public class RegistersDirective extends SmaliNode {

	public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
			() -> new HashMap<String, Object>());

	public RegistersDirective(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var sb = new StringBuilder();
		var attributes = get(ATTRIBUTES);

		var type = (String) attributes.get("type");
		var value = (LiteralRef) attributes.get("value");

		if (type == "I_REGISTERS") {
			sb.append(".registers ");
		} else if (type == "I_LOCALS") {
			sb.append(".locals ");
		}

		sb.append(value.getCode());

		return sb.toString();
	}

}