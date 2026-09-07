package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.literal.PrimitiveLiteral;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APrimitiveLiteral;

public class SmaliPrimitiveLiteral<Self extends SmaliPrimitiveLiteral<Self>> extends APrimitiveLiteral<Self> {

    public SmaliPrimitiveLiteral(PrimitiveLiteral node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public PrimitiveLiteral getNodeImpl() {
        return (PrimitiveLiteral) super.getNodeImpl();
    }

    @Override
    public String getSetValueImpl(String value) {
        return this.getNodeImpl().setValue(value);
    }
}
