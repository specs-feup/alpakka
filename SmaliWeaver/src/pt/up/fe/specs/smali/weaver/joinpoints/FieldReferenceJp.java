package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.FieldReference;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AFieldReference;

public class FieldReferenceJp extends AFieldReference {

    private final FieldReference fieldReference;

    public FieldReferenceJp(FieldReference fieldReference) {
        super(new ExpressionJp(fieldReference));
        this.fieldReference = fieldReference;
    }

    @Override
    public SmaliNode getNode() {
        return this.fieldReference;
    }
}
