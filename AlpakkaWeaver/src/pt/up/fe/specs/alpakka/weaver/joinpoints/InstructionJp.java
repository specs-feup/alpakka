package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.Instruction;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AInstruction;

public class InstructionJp extends AInstruction {
    private final Instruction instruction;

    public InstructionJp(Instruction instruction) {
        super(new StatementJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }

    @Override
    public Boolean getCanThrowImpl() {
        return this.instruction.canThrow();
    }

    @Override
    public Boolean getSetsResultImpl() {
        return this.instruction.setsResult();
    }

    @Override
    public Boolean getSetsRegisterImpl() {
        return this.instruction.setsRegister();
    }

    @Override
    public String getOpCodeNameImpl() {
        return this.instruction.getOpCodeName();
    }
}
