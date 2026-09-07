package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.RegistersDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ARegistersDirective;

public class SmaliRegistersDirective<Self extends SmaliRegistersDirective<Self>> extends ARegistersDirective<Self> {

    public SmaliRegistersDirective(RegistersDirective node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public RegistersDirective getNodeImpl() {
        return (RegistersDirective) super.getNodeImpl();
    }

    @Override
    public String getTypeImpl() {
        return this.getNodeImpl().getType();
    }

    @Override
    public Integer getValueImpl() {
        return this.getNodeImpl().getValue();
    }
}
