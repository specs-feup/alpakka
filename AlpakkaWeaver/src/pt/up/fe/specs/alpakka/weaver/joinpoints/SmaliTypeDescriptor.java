package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.type.Type;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

public class SmaliTypeDescriptor<Self extends SmaliTypeDescriptor<Self>> extends ATypeDescriptor<Self> {

    public SmaliTypeDescriptor(Type node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public Type getNodeImpl() {
        return (Type) super.getNodeImpl();
    }
}
