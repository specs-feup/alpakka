package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.instruction.ReturnStatement;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AReturnStatement;

public class SmaliReturnStatement<Self extends SmaliReturnStatement<Self>> extends AReturnStatement<Self> {

    public SmaliReturnStatement(ReturnStatement node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public ReturnStatement getNodeImpl() {
        return (ReturnStatement) super.getNodeImpl();
    }
}
