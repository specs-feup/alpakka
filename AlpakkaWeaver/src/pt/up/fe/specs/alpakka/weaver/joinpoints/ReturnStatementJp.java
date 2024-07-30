package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.ReturnStatement;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AReturnStatement;

public class ReturnStatementJp extends AReturnStatement {

    private final ReturnStatement instruction;

    public ReturnStatementJp(ReturnStatement instruction) {
        super(new InstructionJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
