package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;

public class MethodReference extends Expression {

	public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
			() -> new HashMap<String, Object>());

	public MethodReference(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var attributes = get(ATTRIBUTES);

		var referenceTypeDescriptor = (TypeDescriptor) attributes.get("referenceTypeDescriptor");
		var member = attributes.get("memberName");
		var prototype = (MethodPrototype) attributes.get("prototype");

		var sb = new StringBuilder();

		if (referenceTypeDescriptor != null) {
			sb.append(referenceTypeDescriptor.getCode() + "->");
		}

		sb.append(member);

		sb.append(prototype.getCode());

		return sb.toString();
	}

}
