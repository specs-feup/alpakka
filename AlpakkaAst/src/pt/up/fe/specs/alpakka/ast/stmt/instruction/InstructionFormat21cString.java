package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;

public class InstructionFormat21cString extends Instruction {

	public InstructionFormat21cString(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}
}
