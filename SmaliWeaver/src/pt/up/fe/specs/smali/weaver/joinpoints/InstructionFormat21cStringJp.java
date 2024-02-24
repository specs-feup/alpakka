package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21cString;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AInstructionFormat21cString;

public class InstructionFormat21cStringJp extends AInstructionFormat21cString {

	private final InstructionFormat21cString instruction;

	public InstructionFormat21cStringJp(InstructionFormat21cString instruction) {
		super(new InstructionJp(instruction));
		this.instruction = instruction;
	}

	@Override
	public SmaliNode getNode() {
		return this.instruction;
	}
}
