package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.RegisterRange;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ARegisterRange;

public class RegisterRangeJp extends ARegisterRange {

    private final RegisterRange rRange;

    public RegisterRangeJp(RegisterRange rRange) {
        super(new ExpressionJp(rRange));
        this.rRange = rRange;
    }

    @Override
    public SmaliNode getNode() {
        return this.rRange;
    }
}
