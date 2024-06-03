package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.MethodReference;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AMethodNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AMethodPrototype;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AMethodReference;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ATypeDescriptor;

public class MethodReferenceJp extends AMethodReference {

    private final MethodReference methodReference;

    public MethodReferenceJp(MethodReference methodReference) {
        super(new ExpressionJp(methodReference));
        this.methodReference = methodReference;
    }

    @Override
    public SmaliNode getNode() {
        return this.methodReference;
    }

    @Override
    public ATypeDescriptor getReferenceTypeDescriptorImpl() {
        return SmaliJoinpoints.create(this.methodReference.getReferenceTypeDescriptor(), ATypeDescriptor.class);
    }

    @Override
    public String getMemberNameImpl() {
        return this.methodReference.getMemberName();
    }

    @Override
    public AMethodPrototype getPrototypeImpl() {
        return SmaliJoinpoints.create(this.methodReference.getPrototype(), AMethodPrototype.class);
    }
}
