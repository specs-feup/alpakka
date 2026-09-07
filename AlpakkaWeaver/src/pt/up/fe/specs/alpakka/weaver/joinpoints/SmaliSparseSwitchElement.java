package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.SparseSwitchElement;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ASparseSwitchElement;

public class SmaliSparseSwitchElement<Self extends SmaliSparseSwitchElement<Self>> extends ASparseSwitchElement<Self> {

    public SmaliSparseSwitchElement(SparseSwitchElement node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public SparseSwitchElement getNodeImpl() {
        return (SparseSwitchElement) super.getNodeImpl();
    }

    @Override
    public ALabelReference<?> getLabelImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getLabel(), getWeaverEngine(), ALabelReference.class);
    }
}
