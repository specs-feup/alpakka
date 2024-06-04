package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.PackedSwitchDirective;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.APackedSwitch;

public class PackedSwitchJp extends APackedSwitch {

    private final PackedSwitchDirective instruction;

    public PackedSwitchJp(PackedSwitchDirective instruction) {
        super(new StatementJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
