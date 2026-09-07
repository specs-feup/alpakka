package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.LineDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALineDirective;

public class SmaliLineDirective<Self extends SmaliLineDirective<Self>> extends ALineDirective<Self> {

    public SmaliLineDirective(LineDirective node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public LineDirective getNodeImpl() {
        return (LineDirective) super.getNodeImpl();
    }

    @Override
    public int getValueImpl() {
        return this.getNodeImpl().getValue();
    }
}
