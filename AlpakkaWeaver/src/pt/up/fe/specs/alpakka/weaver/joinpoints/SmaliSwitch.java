package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.instruction.SwitchStatement;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ASwitch;

public class SmaliSwitch<Self extends SmaliSwitch<Self>> extends ASwitch<Self> {

    public SmaliSwitch(SwitchStatement node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public SwitchStatement getNodeImpl() {
        return (SwitchStatement) super.getNodeImpl();
    }
}
