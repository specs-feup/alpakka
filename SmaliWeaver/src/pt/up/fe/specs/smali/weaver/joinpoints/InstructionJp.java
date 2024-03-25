package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.Instruction;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AInstruction;

public class InstructionJp extends AInstruction {
	private final Instruction instruction;

	public InstructionJp(Instruction instruction) {
		this.instruction = instruction;
	}

	@Override
	public SmaliNode getNode() {
		return this.instruction;
	}
}
