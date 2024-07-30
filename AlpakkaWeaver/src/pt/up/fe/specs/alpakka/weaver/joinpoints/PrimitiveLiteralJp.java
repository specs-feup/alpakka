package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.PrimitiveLiteral;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APrimitiveLiteral;

public class PrimitiveLiteralJp extends APrimitiveLiteral {

    private final PrimitiveLiteral literal;

    public PrimitiveLiteralJp(PrimitiveLiteral literal) {
        super(new LiteralJp(literal));
        this.literal = literal;
    }

    @Override
    public SmaliNode getNode() {
        return this.literal;
    }

    @Override
    public String setValueImpl(String value) {
        return this.literal.setValue(value);
    }
}
