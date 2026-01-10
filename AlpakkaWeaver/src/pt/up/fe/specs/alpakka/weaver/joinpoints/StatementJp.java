package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.Statement;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALineDirective;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AStatement;

public class StatementJp extends AStatement {

    private final Statement statement;

    public StatementJp(Statement statement, SmaliWeaver weaver) {
        super(weaver);
        this.statement = statement;
    }

    @Override
    public SmaliNode getNode() {
        return this.statement;
    }

    @Override
    public AStatement getNextStatementImpl() {
        return SmaliJoinpoints.create(this.statement.getNextStatement(), getWeaverEngine(), AStatement.class);
    }

    @Override
    public AStatement getPrevStatementImpl() {
        return SmaliJoinpoints.create(this.statement.getPreviousStatement(), getWeaverEngine(), AStatement.class);
    }

    @Override
    public ALineDirective getLineImpl() {
        return SmaliJoinpoints.create(this.statement.getLineDirective(), getWeaverEngine(), ALineDirective.class);
    }
}
