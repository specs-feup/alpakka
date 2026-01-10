package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.ArrayType;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AArrayType;

public class ArrayTypeJp extends AArrayType {

    private final ArrayType arrayType;

    public ArrayTypeJp(ArrayType arrayType, SmaliWeaver weaver) {
        super(new TypeDescriptorJp(arrayType, weaver), weaver);
        this.arrayType = arrayType;
    }

    @Override
    public SmaliNode getNode() {
        return this.arrayType;
    }

}
