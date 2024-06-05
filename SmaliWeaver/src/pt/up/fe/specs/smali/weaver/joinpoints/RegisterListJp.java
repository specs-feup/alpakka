package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.RegisterList;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ARegisterList;

public class RegisterListJp extends ARegisterList {

    private final RegisterList rList;

    public RegisterListJp(RegisterList rList) {
        super(new ExpressionJp(rList));
        this.rList = rList;
    }

    @Override
    public SmaliNode getNode() {
        return this.rList;
    }
}
