package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.instruction.InstructionFormat21t;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AIfComparisonWithZero;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;

public class SmaliIfComparisonWithZero<Self extends SmaliIfComparisonWithZero<Self>> extends AIfComparisonWithZero<Self> {

    public SmaliIfComparisonWithZero(InstructionFormat21t node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public InstructionFormat21t getNodeImpl() {
        return (InstructionFormat21t) super.getNodeImpl();
    }

    @Override
    public ALabelReference<?> getLabelImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getLabel(), getWeaverEngine(), ALabelReference.class);
    }
}
