package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

public class TypeDescriptorJp extends ATypeDescriptor {

    private final TypeDescriptor type;

    public TypeDescriptorJp(TypeDescriptor type) {
        super(new LiteralJp(type));
        this.type = type;
    }

    @Override
    public SmaliNode getNode() {
        return this.type;
    }
}
