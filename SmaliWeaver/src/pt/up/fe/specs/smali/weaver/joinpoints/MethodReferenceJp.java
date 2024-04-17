package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.MethodReference;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AMethodReference;

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
}
