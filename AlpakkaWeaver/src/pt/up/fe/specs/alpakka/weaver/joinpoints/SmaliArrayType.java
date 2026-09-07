package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.type.ArrayType;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AArrayType;

public class SmaliArrayType<Self extends SmaliArrayType<Self>> extends AArrayType<Self> {

    public SmaliArrayType(ArrayType node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public ArrayType getNodeImpl() {
        return (ArrayType) super.getNodeImpl();
    }
}
