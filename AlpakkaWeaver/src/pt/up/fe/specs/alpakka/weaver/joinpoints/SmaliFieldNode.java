package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.FieldNode;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AFieldNode;

public class SmaliFieldNode<Self extends SmaliFieldNode<Self>> extends AFieldNode<Self> {

    public SmaliFieldNode(FieldNode node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public FieldNode getNodeImpl() {
        return (FieldNode) super.getNodeImpl();
    }

    @Override
    public String getNameImpl() {
        return this.getNodeImpl().getFieldName();
    }

    @Override
    public String getReferenceNameImpl() {
        return this.getNodeImpl().getFieldReferenceName();
    }

    @Override
    public boolean getIsStaticImpl() {
        return this.getNodeImpl().isStatic();
    }
}
