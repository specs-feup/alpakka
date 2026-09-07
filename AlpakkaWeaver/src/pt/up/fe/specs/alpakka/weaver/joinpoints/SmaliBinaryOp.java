package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.instruction.BinaryOp;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ABinaryOp;

public class SmaliBinaryOp<Self extends SmaliBinaryOp<Self>> extends ABinaryOp<Self> {

    public SmaliBinaryOp(BinaryOp node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public BinaryOp getNodeImpl() {
        return (BinaryOp) super.getNodeImpl();
    }

    @Override
    public void setOperatorImpl(String operator) {
        this.getNodeImpl().setOperator(operator);
    }
}
