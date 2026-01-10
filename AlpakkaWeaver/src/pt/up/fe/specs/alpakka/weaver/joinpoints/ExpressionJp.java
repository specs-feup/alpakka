package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.Expression;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AExpression;

public class ExpressionJp extends AExpression {

    private final Expression expression;

    public ExpressionJp(Expression expression, SmaliWeaver weaver) {
        super(weaver);
        this.expression = expression;
    }

    @Override
    public SmaliNode getNode() {
        return expression;
    }

}
