package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.instruction.Instruction;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AInstruction;

public class SmaliInstruction<Self extends SmaliInstruction<Self>> extends AInstruction<Self> {

    public SmaliInstruction(Instruction node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public Instruction getNodeImpl() {
        return (Instruction) super.getNodeImpl();
    }

    @Override
    public boolean getCanThrowImpl() {
        return this.getNodeImpl().canThrow();
    }

    @Override
    public boolean getSetsResultImpl() {
        return this.getNodeImpl().setsResult();
    }

    @Override
    public boolean getSetsRegisterImpl() {
        return this.getNodeImpl().setsRegister();
    }

    @Override
    public String getOpCodeNameImpl() {
        return this.getNodeImpl().getOpCodeName();
    }
}
