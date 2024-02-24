package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.RegisterReference;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.type.MethodPrototype;
import pt.up.fe.specs.smali.ast.type.Type;

public class InstructionFormat35cMethod extends Instruction {

	public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
			() -> new HashMap<String, Object>());

	public InstructionFormat35cMethod(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var sb = new StringBuilder();
		var attributes = get(ATTRIBUTES);

		var registerList = (List<RegisterReference>) attributes.get("registerList");
		var referenceTypeDescriptor = (Type) attributes.get("referenceTypeDescriptor");
		var member = attributes.get("memberName");
		var prototype = (MethodPrototype) attributes.get("prototype");

		sb.append(get(INSTRUCTION) + " ");

		sb.append("{");
		for (var register : registerList) {
			sb.append(register.getCode());
			if (registerList.indexOf(register) < registerList.size() - 1)
				sb.append(", ");
		}
		sb.append("}, ");

		if (referenceTypeDescriptor != null) {
			sb.append(referenceTypeDescriptor.getCode() + "->");
		}

		sb.append(member);

		sb.append(prototype.getCode());

		return sb.toString();
	}

}
