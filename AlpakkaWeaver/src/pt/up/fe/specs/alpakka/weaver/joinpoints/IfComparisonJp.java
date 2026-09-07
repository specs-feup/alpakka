package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.InstructionFormat22t;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AIfComparison;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;

public class IfComparisonJp extends AIfComparison {

    private final InstructionFormat22t instruction;

    public IfComparisonJp(InstructionFormat22t instruction, SmaliWeaver weaver) {
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
