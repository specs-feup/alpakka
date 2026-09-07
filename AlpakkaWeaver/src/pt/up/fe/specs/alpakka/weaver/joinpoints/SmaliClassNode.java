package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.ClassNode;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassType;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AFieldNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodNode;

public class SmaliClassNode<Self extends SmaliClassNode<Self>> extends AClassNode<Self> {

    public SmaliClassNode(ClassNode node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public ClassNode getNodeImpl() {
        return (ClassNode) super.getNodeImpl();
    }

    @Override
    public AMethodNode<?>[] getMethodsImpl() {
        return this.getNodeImpl().getMethods().stream()
                .map(method -> SmaliJoinpoints.create(method, getWeaverEngine(), AMethodNode.class))
                .toArray(AMethodNode<?>[]::new);
    }

    @Override
    public AFieldNode<?>[] getFieldsImpl() {
        return this.getNodeImpl().getFields().stream()
                .map(field -> SmaliJoinpoints.create(field, getWeaverEngine(), AFieldNode.class))
                .toArray(AFieldNode<?>[]::new);
    }

    @Override
    public AClassType<?> getClassDescriptorImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getClassDescriptor(), getWeaverEngine(), AClassType.class);
    }

    @Override
    public AClassType<?> getSuperClassDescriptorImpl() {
        return this.getNodeImpl().getSuperClass()
                .map(node -> SmaliJoinpoints.create(node, getWeaverEngine(), AClassType.class))
                .orElse(null);
    }
}
