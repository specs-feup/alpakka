package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.PackedSwitchDirective;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APackedSwitch;

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
