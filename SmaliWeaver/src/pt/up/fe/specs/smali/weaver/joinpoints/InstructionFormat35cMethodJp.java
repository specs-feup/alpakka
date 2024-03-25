package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat35cMethod;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AInstructionFormat35cMethod;

public class InstructionFormat35cMethodJp extends AInstructionFormat35cMethod {

	private final InstructionFormat35cMethod instruction;

	public InstructionFormat35cMethodJp(InstructionFormat35cMethod instruction) {
		super(new InstructionJp(instruction));
		this.instruction = instruction;
	}

	@Override
	public SmaliNode getNode() {
		return this.instruction;
	}
}
