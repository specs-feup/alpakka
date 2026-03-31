package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.LineDirective;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALineDirective;

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
    public Integer getValueImpl() {
        return this.line.getValue();
    }
}
