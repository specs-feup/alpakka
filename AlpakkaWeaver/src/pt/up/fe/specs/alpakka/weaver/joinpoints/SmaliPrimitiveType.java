package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.type.PrimitiveType;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APrimitiveType;

public class SmaliPrimitiveType<Self extends SmaliPrimitiveType<Self>> extends APrimitiveType<Self> {

    public SmaliPrimitiveType(PrimitiveType node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public PrimitiveType getNodeImpl() {
        return (PrimitiveType) super.getNodeImpl();
    }
}
