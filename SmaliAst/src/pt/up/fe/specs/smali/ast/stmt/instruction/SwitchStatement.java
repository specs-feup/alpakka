package pt.up.fe.specs.smali.ast.stmt.instruction;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.smali.ast.SmaliNode;

import java.util.Collection;

public class SwitchStatement extends Instruction {

	public SwitchStatement(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

}
