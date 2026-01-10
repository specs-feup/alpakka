package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.SparseSwitchDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ASparseSwitch;

public class SparseSwitchJp extends ASparseSwitch {

    private final SparseSwitchDirective instruction;

    public SparseSwitchJp(SparseSwitchDirective instruction, SmaliWeaver weaver) {
        super(new StatementJp(instruction, weaver), weaver);
        this.instruction = instruction;
    }

    @Override
    public SmaliNode getNode() {
        return this.instruction;
    }
}
