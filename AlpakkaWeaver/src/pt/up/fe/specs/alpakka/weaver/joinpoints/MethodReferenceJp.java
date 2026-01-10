package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.MethodReference;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodPrototype;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodReference;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

public class MethodReferenceJp extends AMethodReference {

    private final MethodReference methodReference;

    public MethodReferenceJp(MethodReference methodReference, SmaliWeaver weaver) {
        super(new ExpressionJp(methodReference, weaver), weaver);
        this.methodReference = methodReference;
    }

    @Override
    public SmaliNode getNode() {
        return this.methodReference;
    }

    @Override
    public ATypeDescriptor getParentClassDescriptorImpl() {
        return SmaliJoinpoints.create(this.methodReference.getParentClassDescriptor(),
                getWeaverEngine(), ATypeDescriptor.class);
    }

    @Override
    public String getNameImpl() {
        return this.methodReference.getMethodName();
    }

    @Override
    public AMethodPrototype getPrototypeImpl() {
        return SmaliJoinpoints.create(this.methodReference.getPrototype(), getWeaverEngine(), AMethodPrototype.class);
    }
}
