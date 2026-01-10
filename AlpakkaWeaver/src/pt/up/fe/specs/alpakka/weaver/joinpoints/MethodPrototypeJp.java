package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodPrototype;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

public class MethodPrototypeJp extends AMethodPrototype {

    private final MethodPrototype methodPrototype;

    public MethodPrototypeJp(MethodPrototype methodPrototype, SmaliWeaver weaver) {
        super(new LiteralJp(methodPrototype, weaver), weaver);
        this.methodPrototype = methodPrototype;
    }

    @Override
    public SmaliNode getNode() {
        return this.methodPrototype;
    }

    @Override
    public ATypeDescriptor[] getParametersArrayImpl() {
        return this.methodPrototype.getParameters().stream()
                .map(param -> SmaliJoinpoints.create(param,
                        getWeaverEngine(), ATypeDescriptor.class))
                .toArray(ATypeDescriptor[]::new);
    }

    @Override
    public ATypeDescriptor getReturnTypeImpl() {
        return SmaliJoinpoints.create(this.methodPrototype.getReturnType(), getWeaverEngine(), ATypeDescriptor.class);
    }
}
