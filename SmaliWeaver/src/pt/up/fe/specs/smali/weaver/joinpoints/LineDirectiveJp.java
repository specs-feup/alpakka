package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.LineDirective;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ALineDirective;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ALiteral;

public class LineDirectiveJp extends ALineDirective {

    private final LineDirective line;

    public LineDirectiveJp(LineDirective line) {
        super(new StatementJp(line));
        this.line = line;
    }

    @Override
    public SmaliNode getNode() {
        return this.line;
    }

    @Override
    public ALiteral getValueImpl() {
        return SmaliJoinpoints.create(this.line.getValue(), ALiteral.class);
    }
}
