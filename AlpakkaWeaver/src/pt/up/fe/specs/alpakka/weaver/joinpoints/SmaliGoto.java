package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.instruction.GotoStatement;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AGoto;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;

public class SmaliGoto<Self extends SmaliGoto<Self>> extends AGoto<Self> {

    public SmaliGoto(GotoStatement node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public GotoStatement getNodeImpl() {
        return (GotoStatement) super.getNodeImpl();
    }

    @Override
    public ALabelReference<?> getLabelImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getLabel(), getWeaverEngine(), ALabelReference.class);
    }
}
