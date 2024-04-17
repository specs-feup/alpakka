package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat22t;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AIfComparison;

public class IfComparisonJp extends AIfComparison {

    private final InstructionFormat22t instruction;

    public IfComparisonJp(InstructionFormat22t instruction) {
        super(new InstructionJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
