package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.CatchDirective;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ACatch;

public class CatchJp extends ACatch {

    private final CatchDirective catchDir;

    public CatchJp(CatchDirective catchDir) {
        super(new StatementJp(catchDir));
        this.catchDir = catchDir;
    }

    @Override
    public SmaliNode getNode() {
        return this.catchDir;
    }
}
