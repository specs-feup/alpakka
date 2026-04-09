package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.PackedSwitchCase;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APackedSwitchCase;

public class PackedSwitchCaseJp extends APackedSwitchCase {

    private final PackedSwitchCase packedSwitchCase;

    public PackedSwitchCaseJp(PackedSwitchCase packedSwitchCase) {
        super(new StatementJp(packedSwitchCase));
        this.packedSwitchCase = packedSwitchCase;
    }

    @Override
    public SmaliNode getNode() {
        return this.packedSwitchCase;
    }
}
