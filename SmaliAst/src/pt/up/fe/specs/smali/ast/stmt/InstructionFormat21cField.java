package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.RegisterReference;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.type.Type;

public class InstructionFormat21cField extends Instruction {

	public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
			() -> new HashMap<String, Object>());

	public InstructionFormat21cField(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var sb = new StringBuilder();
		var attributes = get(ATTRIBUTES);

		var register = (RegisterReference) attributes.get("register");
		var referenceTypeDescriptor = (Type) attributes.get("referenceTypeDescriptor");
		var member = attributes.get("memberName");
		var nonVoidTypeDescriptor = (Type) attributes.get("nonVoidTypeDescriptor");

		sb.append(get(INSTRUCTION) + " ");

		sb.append(register.getCode() + ", ");

		if (referenceTypeDescriptor != null) {
			sb.append(referenceTypeDescriptor.getCode() + "->");
		}

		sb.append(member + ":");

		sb.append(nonVoidTypeDescriptor.getCode());

		return sb.toString();
	}

}
