package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.RegistersDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APrimitiveLiteral;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ARegistersDirective;

public class RegistersDirectiveJp extends ARegistersDirective {

    private final RegistersDirective registers;

    public RegistersDirectiveJp(RegistersDirective registers, SmaliWeaver weaver) {
        super(new StatementJp(registers, weaver), weaver);
        this.registers = registers;
    }

    @Override
    public SmaliNode getNode() {
        return this.registers;
    }

    @Override
    public String getTypeImpl() {
        return this.registers.getType();
    }

    @Override
    public APrimitiveLiteral getValueImpl() {
        return SmaliJoinpoints.create(this.registers.getValue(), getWeaverEngine(), APrimitiveLiteral.class);
    }
}
