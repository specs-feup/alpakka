package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.GotoStatement;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AGoto;

public class GotoJp extends AGoto {

    private final GotoStatement instruction;

    public GotoJp(GotoStatement instruction) {
        super(new InstructionJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
