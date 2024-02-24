package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.RegisterReference;
import pt.up.fe.specs.smali.ast.SmaliNode;

public class InstructionFormat21cString extends Instruction {

	public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
			() -> new HashMap<String, Object>());

	public InstructionFormat21cString(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var sb = new StringBuilder();
		var attributes = get(ATTRIBUTES);

		var register = (RegisterReference) attributes.get("register");
		var string = attributes.get("string");

		sb.append(get(INSTRUCTION) + " ");

		sb.append(register.getCode() + ", ");

		sb.append(string);

		return sb.toString();
	}

}
