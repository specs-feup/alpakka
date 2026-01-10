package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.LineDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALineDirective;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALiteral;

public class LineDirectiveJp extends ALineDirective {

    private final LineDirective line;

    public LineDirectiveJp(LineDirective line, SmaliWeaver weaver) {
        super(new StatementJp(line, weaver), weaver);
        this.line = line;
    }

    @Override
    public SmaliNode getNode() {
        return this.line;
    }

    @Override
    public ALiteral getValueImpl() {
        return SmaliJoinpoints.create(this.line.getValue(), getWeaverEngine(), ALiteral.class);
    }
}
