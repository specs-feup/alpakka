package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.FieldNode;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AFieldNode;

public class FieldNodeJp extends AFieldNode {

    private final FieldNode fieldNode;

    public FieldNodeJp(FieldNode fieldNode) {
        this.fieldNode = fieldNode;
    }

    @Override
    public SmaliNode getNode() {
        return fieldNode;
    }

}
