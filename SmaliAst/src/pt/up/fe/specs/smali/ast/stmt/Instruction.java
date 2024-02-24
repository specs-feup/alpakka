package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public abstract class Instruction extends SmaliNode {

	public static final DataKey<String> INSTRUCTION = KeyFactory.string("instruction");

	public Instruction(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

}
