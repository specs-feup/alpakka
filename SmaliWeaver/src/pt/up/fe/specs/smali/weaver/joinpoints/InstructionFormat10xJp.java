package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat10x;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AInstructionFormat10x;

public class InstructionFormat10xJp extends AInstructionFormat10x {
	private final InstructionFormat10x instruction;

	public InstructionFormat10xJp(InstructionFormat10x instruction) {
		super(new InstructionJp(instruction));
		this.instruction = instruction;
	}

	@Override
	public SmaliNode getNode() {
		return this.instruction;
	}
}
