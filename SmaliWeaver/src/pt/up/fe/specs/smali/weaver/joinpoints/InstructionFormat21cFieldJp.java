package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21cField;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AInstructionFormat21cField;

public class InstructionFormat21cFieldJp extends AInstructionFormat21cField {

	private final InstructionFormat21cField instruction;

	public InstructionFormat21cFieldJp(InstructionFormat21cField instruction) {
		super(new InstructionJp(instruction));
		this.instruction = instruction;
	}

	@Override
	public SmaliNode getNode() {
		return this.instruction;
	}
}
