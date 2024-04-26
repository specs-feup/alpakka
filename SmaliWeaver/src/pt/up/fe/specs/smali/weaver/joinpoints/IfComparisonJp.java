package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat22t;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AIfComparison;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ALabelReference;

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

    @Override
    public ALabelReference getLabelImpl() {
        return SmaliJoinpoints.create(this.instruction.getLabel(), ALabelReference.class);
    }
}
