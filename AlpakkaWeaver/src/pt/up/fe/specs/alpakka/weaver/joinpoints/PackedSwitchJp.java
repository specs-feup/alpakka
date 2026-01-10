package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.PackedSwitchDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APackedSwitch;

public class PackedSwitchJp extends APackedSwitch {

    private final PackedSwitchDirective instruction;

    public PackedSwitchJp(PackedSwitchDirective instruction, SmaliWeaver weaver) {
        super(new StatementJp(instruction, weaver), weaver);
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
