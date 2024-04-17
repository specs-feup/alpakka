package pt.up.fe.specs.smali.ast.stmt.instruction;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class FillArrayStatement extends Instruction {

	public FillArrayStatement(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}
}
