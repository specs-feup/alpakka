package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.PackedSwitchDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APackedSwitch;

public class SmaliPackedSwitch<Self extends SmaliPackedSwitch<Self>> extends APackedSwitch<Self> {

    public SmaliPackedSwitch(PackedSwitchDirective node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public PackedSwitchDirective getNodeImpl() {
        return (PackedSwitchDirective) super.getNodeImpl();
    }
}
