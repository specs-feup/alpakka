package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.RegisterReference;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ARegisterReference;

public class SmaliRegisterReference<Self extends SmaliRegisterReference<Self>> extends ARegisterReference<Self> {

    public SmaliRegisterReference(RegisterReference node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public RegisterReference getNodeImpl() {
        return (RegisterReference) super.getNodeImpl();
    }
}
