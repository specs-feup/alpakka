package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.GotoStatement;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AGoto;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;

public class GotoJp extends AGoto {

    private final GotoStatement instruction;

    public GotoJp(GotoStatement instruction, SmaliWeaver weaver) {
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
