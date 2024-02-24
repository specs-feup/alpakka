package pt.up.fe.specs.smali.ast.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class MethodPrototype extends Type {

	public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
			() -> new HashMap<String, Object>());

	public MethodPrototype(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var attributes = get(ATTRIBUTES);
		var returnType = (Type) attributes.get("returnType");
		var parameters = (ArrayList<Type>) attributes.get("parameters");

		var builder = new StringBuilder();
		builder.append("(");
		parameters.forEach(p -> builder.append(p.getCode()));
		builder.append(")");

		builder.append(returnType.getCode());

		return builder.toString();
	}

}
