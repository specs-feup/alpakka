package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.Expression;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AExpression;

public class SmaliExpression<Self extends SmaliExpression<Self>> extends AExpression<Self> {

    public SmaliExpression(Expression node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public Expression getNodeImpl() {
        return (Expression) super.getNodeImpl();
    }
}
