package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.PackedSwitch;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APackedSwitchBlock;

public class PackedSwitchBlockJp extends APackedSwitchBlock {

    private final PackedSwitch packedSwitch;

    public PackedSwitchBlockJp(PackedSwitch packedSwitch) {
        super(new StatementJp(packedSwitch));
        this.packedSwitch = packedSwitch;
    }

    @Override
    public SmaliNode getNode() {
        return this.packedSwitch;
    }
}
