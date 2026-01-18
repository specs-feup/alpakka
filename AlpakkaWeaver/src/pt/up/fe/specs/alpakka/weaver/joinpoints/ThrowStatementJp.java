package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.ThrowStatement;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AThrowStatement;

public class ThrowStatementJp extends AThrowStatement {

    private final ThrowStatement instruction;

    public ThrowStatementJp(ThrowStatement instruction, SmaliWeaver weaver) {
        super(new InstructionJp(instruction, weaver), weaver);
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
