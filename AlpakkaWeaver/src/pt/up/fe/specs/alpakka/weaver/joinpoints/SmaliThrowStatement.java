package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.instruction.ThrowStatement;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AThrowStatement;

public class SmaliThrowStatement<Self extends SmaliThrowStatement<Self>> extends AThrowStatement<Self> {

    public SmaliThrowStatement(ThrowStatement node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public ThrowStatement getNodeImpl() {
        return (ThrowStatement) super.getNodeImpl();
    }
}
