package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.LabelRef;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabel;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;

public class SmaliLabelReference<Self extends SmaliLabelReference<Self>> extends ALabelReference<Self> {

    public SmaliLabelReference(LabelRef node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public LabelRef getNodeImpl() {
        return (LabelRef) super.getNodeImpl();
    }

    @Override
    public ALabel<?> getDeclImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getDeclaration(), getWeaverEngine(), ALabel.class);
    }
}
