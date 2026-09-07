package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.Resource;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AResourceNode;

public class SmaliResourceNode<Self extends SmaliResourceNode<Self>> extends AResourceNode<Self> {

    public SmaliResourceNode(Resource node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public Resource getNodeImpl() {
        return (Resource) super.getNodeImpl();
    }
}
