package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.ThrowStatement;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AThrowStatement;

public class ThrowStatementJp extends AThrowStatement {

    private final ThrowStatement instruction;

    public ThrowStatementJp(ThrowStatement instruction) {
        super(new InstructionJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
