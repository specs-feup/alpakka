package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.type.Type;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

public class TypeDescriptorJp extends ATypeDescriptor {

    private final Type type;

    public TypeDescriptorJp(Type type, SmaliWeaver weaver) {
        super(weaver);
        this.type = type;
    }

    @Override
    public SmaliNode getNode() {
        return this.type;
    }
}
