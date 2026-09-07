package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.MethodReference;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodPrototype;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodReference;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

public class SmaliMethodReference<Self extends SmaliMethodReference<Self>> extends AMethodReference<Self> {

    public SmaliMethodReference(MethodReference node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public MethodReference getNodeImpl() {
        return (MethodReference) super.getNodeImpl();
    }

    @Override
    public ATypeDescriptor<?> getParentClassDescriptorImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getBaseType().orElse(null), getWeaverEngine(),
                ATypeDescriptor.class);
    }

    @Override
    public String getNameImpl() {
        return this.getNodeImpl().getMethodName();
    }

    @Override
    public AMethodPrototype<?> getPrototypeImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getPrototype(), getWeaverEngine(), AMethodPrototype.class);
    }
}
