package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.instruction.InstructionFormat22t;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AIfComparison;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;

public class SmaliIfComparison<Self extends SmaliIfComparison<Self>> extends AIfComparison<Self> {

    public SmaliIfComparison(InstructionFormat22t node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public InstructionFormat22t getNodeImpl() {
        return (InstructionFormat22t) super.getNodeImpl();
    }

    @Override
    public ALabelReference<?> getLabelImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getLabel(), getWeaverEngine(), ALabelReference.class);
    }
}
