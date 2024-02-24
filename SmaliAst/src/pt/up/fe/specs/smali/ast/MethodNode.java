package pt.up.fe.specs.smali.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.type.MethodPrototype;

public class MethodNode extends SmaliNode {

	public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
			() -> new HashMap<String, Object>());

	public MethodNode(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var attributes = get(ATTRIBUTES);
		var name = (String) attributes.get("name");
		var prototype = (MethodPrototype) attributes.get("prototype");
		var accessList = (ArrayList<String>) attributes.get("accessOrRestrictionList");
		var registersDirective = (RegistersDirective) attributes.get("registersOrLocals");

		var builder = new StringBuilder();
		builder.append(".method ");
		accessList.forEach(a -> builder.append(a).append(" "));
		builder.append(name);
		builder.append(prototype.getCode());
		builder.append("\n");

		builder.append("\t").append(registersDirective.getCode()).append("\n");

		getChildren().forEach(c -> builder.append("\t").append(c.getCode()).append("\n"));

		builder.append(".end method");

		return builder.toString();
	}

}
