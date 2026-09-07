package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.MethodNode;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodPrototype;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ARegistersDirective;

public class SmaliMethodNode<Self extends SmaliMethodNode<Self>> extends AMethodNode<Self> {

    public SmaliMethodNode(MethodNode node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public MethodNode getNodeImpl() {
        return (MethodNode) super.getNodeImpl();
    }

    @Override
    public String getNameImpl() {
        return this.getNodeImpl().getMethodReferenceName();
    }

    @Override
    public String getReferenceNameImpl() {
        return this.getNodeImpl().getMethodReferenceName();
    }

    @Override
    public AMethodPrototype<?> getPrototypeImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getPrototype(), getWeaverEngine(), AMethodPrototype.class);
    }

    @Override
    public ARegistersDirective<?> getRegistersDirectiveImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getRegistersDirective().orElse(null), getWeaverEngine(),
                ARegistersDirective.class);
    }

    @Override
    public boolean getIsStaticImpl() {
        return this.getNodeImpl().isStatic();
    }
}
