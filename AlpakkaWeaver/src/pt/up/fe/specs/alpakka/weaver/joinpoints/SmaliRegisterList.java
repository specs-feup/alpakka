package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.RegisterList;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ARegisterList;

public class SmaliRegisterList<Self extends SmaliRegisterList<Self>> extends ARegisterList<Self> {

    public SmaliRegisterList(RegisterList node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public RegisterList getNodeImpl() {
        return (RegisterList) super.getNodeImpl();
    }
}
