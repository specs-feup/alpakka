package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.RegisterList;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ARegisterList;

public class RegisterListJp extends ARegisterList {

    private final RegisterList rList;

    public RegisterListJp(RegisterList rList, SmaliWeaver weaver) {
        super(new ExpressionJp(rList, weaver), weaver);
        this.rList = rList;
    }

    @Override
    public SmaliNode getNode() {
        return this.rList;
    }
}
