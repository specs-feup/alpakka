package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.Statement;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALineDirective;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AStatement;

public class SmaliStatement<Self extends SmaliStatement<Self>> extends AStatement<Self> {

    public SmaliStatement(Statement node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public Statement getNodeImpl() {
        return (Statement) super.getNodeImpl();
    }

    @Override
    public AStatement<?> getNextStatementImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getNextStatement(), getWeaverEngine(), AStatement.class);
    }

    @Override
    public AStatement<?> getPrevStatementImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getPreviousStatement(), getWeaverEngine(), AStatement.class);
    }

    @Override
    public ALineDirective<?> getLineDirectiveImpl() {
        return this.getNodeImpl()
                .getLineDirective()
                .map(node -> SmaliJoinpoints.create(node, getWeaverEngine(), ALineDirective.class))
                .orElse(null);
    }
}
