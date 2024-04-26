package pt.up.fe.specs.smali.ast.stmt.instruction;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.LabelRef;

public class InstructionFormat22t extends Instruction {

	public InstructionFormat22t(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	public LabelRef getLabel() {
		return (LabelRef) getChildren().get(2);
	}
}
