package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.SwitchStatement;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ASwitch;

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
