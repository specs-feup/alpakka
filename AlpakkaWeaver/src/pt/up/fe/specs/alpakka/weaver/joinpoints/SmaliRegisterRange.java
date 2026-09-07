package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.RegisterRange;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ARegisterRange;

public class SmaliRegisterRange<Self extends SmaliRegisterRange<Self>> extends ARegisterRange<Self> {

    public SmaliRegisterRange(RegisterRange node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public RegisterRange getNodeImpl() {
        return (RegisterRange) super.getNodeImpl();
    }
}
