package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.instruction.SparseSwitchDirective;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ASparseSwitch;

public class SparseSwitchJp extends ASparseSwitch {

    private final SparseSwitchDirective instruction;

    public SparseSwitchJp(SparseSwitchDirective instruction) {
        super(new InstructionJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
