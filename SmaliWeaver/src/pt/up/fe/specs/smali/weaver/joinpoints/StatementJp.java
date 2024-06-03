package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.Statement;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AStatement;

public class StatementJp extends AStatement {

    private final Statement statement;

    public StatementJp(Statement statement) {
        this.statement = statement;
    }

    @Override
    public SmaliNode getNode() {
        return this.statement;
    }

    @Override
    public AStatement getNextStatementImpl() {
        return SmaliJoinpoints.create(this.statement.getNextStatement(), AStatement.class);
    }

    @Override
    public AStatement getPrevStatementImpl() {
        return SmaliJoinpoints.create(this.statement.getPreviousStatement(), AStatement.class);
    }
}
