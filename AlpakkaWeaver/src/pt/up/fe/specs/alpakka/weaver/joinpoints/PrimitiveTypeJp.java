package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.PrimitiveType;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APrimitiveType;

public class PrimitiveTypeJp extends APrimitiveType {

    private final PrimitiveType primitiveType;

    public PrimitiveTypeJp(PrimitiveType primitiveType) {
        super(new TypeDescriptorJp(primitiveType));
        this.primitiveType = primitiveType;
    }

    @Override
    public SmaliNode getNode() {
        return this.primitiveType;
    }

}
