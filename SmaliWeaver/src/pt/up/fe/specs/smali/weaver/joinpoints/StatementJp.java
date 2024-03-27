package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.Statement;
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
}
