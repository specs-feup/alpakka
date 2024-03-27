package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.Literal;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ALiteral;

public class LiteralJp extends ALiteral {

    private final Literal literal;

    public LiteralJp(Literal literal) {
        super(new ExpressionJp(literal));
        this.literal = literal;
    }

    @Override
    public SmaliNode getNode() {
        return this.literal;
    }
}
