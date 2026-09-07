package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.FieldNode;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AFieldNode;

public class FieldNodeJp extends AFieldNode {

    private final FieldNode fieldNode;

    public FieldNodeJp(FieldNode fieldNode, SmaliWeaver weaver) {
        super(weaver);
        this.fieldNode = fieldNode;
    }

    @Override
    public SmaliNode getNode() {
        return fieldNode;
    }

    @Override
    public String getNameImpl() {
        return fieldNode.getFieldName();
    }

    @Override
    public String getReferenceNameImpl() {
        return fieldNode.getFieldReferenceName();
    }

    @Override
    public Boolean getIsStaticImpl() {
        return fieldNode.isStatic();
    }
}
