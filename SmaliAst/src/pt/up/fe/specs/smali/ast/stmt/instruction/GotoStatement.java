package pt.up.fe.specs.smali.ast.stmt.instruction;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class GotoStatement extends Instruction {

	public GotoStatement(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

}
