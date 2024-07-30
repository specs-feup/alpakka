package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.SparseSwitchDirective;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ASparseSwitch;

public class SparseSwitchJp extends ASparseSwitch {

    private final SparseSwitchDirective instruction;

    public SparseSwitchJp(SparseSwitchDirective instruction) {
        super(new StatementJp(instruction));
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
