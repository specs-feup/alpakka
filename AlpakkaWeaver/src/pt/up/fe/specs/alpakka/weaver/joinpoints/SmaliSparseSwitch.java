package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.SparseSwitchDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ASparseSwitch;

public class SmaliSparseSwitch<Self extends SmaliSparseSwitch<Self>> extends ASparseSwitch<Self> {

    public SmaliSparseSwitch(SparseSwitchDirective node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public SparseSwitchDirective getNodeImpl() {
        return (SparseSwitchDirective) super.getNodeImpl();
    }
}
