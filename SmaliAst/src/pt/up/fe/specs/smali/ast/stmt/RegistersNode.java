package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class RegistersNode extends SmaliNode {

	public static final DataKey<Integer> REGISTERS = KeyFactory.integer("registers");

	public RegistersNode(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		return ".registers " + get(REGISTERS).toString();
	}

}
