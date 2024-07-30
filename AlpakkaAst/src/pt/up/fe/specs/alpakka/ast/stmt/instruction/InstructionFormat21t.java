package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.LabelRef;

public class InstructionFormat21t extends Instruction {

	public InstructionFormat21t(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	public LabelRef getLabel() {
		return (LabelRef) getChildren().get(1);
	}
}
