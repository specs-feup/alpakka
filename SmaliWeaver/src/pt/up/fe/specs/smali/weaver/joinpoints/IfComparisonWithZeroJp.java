package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21t;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AIfComparisonWithZero;

public class IfComparisonWithZeroJp extends AIfComparisonWithZero {

    private final InstructionFormat21t instruction;

    public IfComparisonWithZeroJp(InstructionFormat21t instruction) {
        super(new InstructionJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
