package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.SwitchStatement;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ASwitch;

public class SwitchJp extends ASwitch {

    private final SwitchStatement instruction;

    public SwitchJp(SwitchStatement instruction) {
        super(new InstructionJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
