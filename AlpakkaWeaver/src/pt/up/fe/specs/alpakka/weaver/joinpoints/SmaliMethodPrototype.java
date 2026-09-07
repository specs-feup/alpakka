package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.type.MethodPrototype;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodPrototype;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

public class SmaliMethodPrototype<Self extends SmaliMethodPrototype<Self>> extends AMethodPrototype<Self> {

    public SmaliMethodPrototype(MethodPrototype node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public MethodPrototype getNodeImpl() {
        return (MethodPrototype) super.getNodeImpl();
    }

    @Override
    public ATypeDescriptor<?>[] getParametersImpl() {
        return this.getNodeImpl().getParameters().stream()
                .map(param -> SmaliJoinpoints.create(param, getWeaverEngine(), ATypeDescriptor.class))
                .toArray(ATypeDescriptor<?>[]::new);
    }

    @Override
    public ATypeDescriptor<?> getReturnTypeImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getReturnType(), getWeaverEngine(), ATypeDescriptor.class);
    }
}
