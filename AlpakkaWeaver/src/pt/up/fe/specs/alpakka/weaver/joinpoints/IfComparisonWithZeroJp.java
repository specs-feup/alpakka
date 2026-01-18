package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.InstructionFormat21t;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AIfComparisonWithZero;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;

public class IfComparisonWithZeroJp extends AIfComparisonWithZero {

    private final InstructionFormat21t instruction;

    public IfComparisonWithZeroJp(InstructionFormat21t instruction, SmaliWeaver weaver) {
        super(new InstructionJp(instruction, weaver), weaver);
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }

    @Override
    public ALabelReference getLabelImpl() {
        return SmaliJoinpoints.create(this.instruction.getLabel(), getWeaverEngine(), ALabelReference.class);
    }
}
