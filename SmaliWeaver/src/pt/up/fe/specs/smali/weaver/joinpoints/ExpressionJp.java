package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.Expression;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AExpression;

public class ExpressionJp extends AExpression {

    private final Expression expression;

    public ExpressionJp(Expression expression) {
        this.expression = expression;
    }

    @Override
    public SmaliNode getNode() {
        return expression;
    }

}
