package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALiteral;

public class SmaliLiteral<Self extends SmaliLiteral<Self>> extends ALiteral<Self> {

    public SmaliLiteral(Literal node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public Literal getNodeImpl() {
        return (Literal) super.getNodeImpl();
    }
}
