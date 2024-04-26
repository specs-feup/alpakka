package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21t;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AIfComparisonWithZero;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ALabelReference;

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

    @Override
    public ALabelReference getLabelImpl() {
        return SmaliJoinpoints.create(this.instruction.getLabel(), ALabelReference.class);
    }
}
