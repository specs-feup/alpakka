package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AMethodPrototype;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ATypeDescriptor;

public class MethodPrototypeJp extends AMethodPrototype {

    private final MethodPrototype methodPrototype;

    public MethodPrototypeJp(MethodPrototype methodPrototype) {
        super(new LiteralJp(methodPrototype));
        this.methodPrototype = methodPrototype;
    }

    @Override
    public SmaliNode getNode() {
        return this.methodPrototype;
    }

    @Override
    public ATypeDescriptor[] getParametersArrayImpl() {
        return this.methodPrototype.getParameters().stream()
                .map(param -> SmaliJoinpoints.create(param, ATypeDescriptor.class))
                .toArray(ATypeDescriptor[]::new);
    }

    @Override
    public ATypeDescriptor getReturnTypeImpl() {
        return SmaliJoinpoints.create(this.methodPrototype.getReturnType(), ATypeDescriptor.class);
    }
}
