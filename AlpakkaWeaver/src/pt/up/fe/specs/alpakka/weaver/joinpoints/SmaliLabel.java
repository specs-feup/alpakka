package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.Label;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabel;

public class SmaliLabel<Self extends SmaliLabel<Self>> extends ALabel<Self> {

    public SmaliLabel(Label node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public Label getNodeImpl() {
        return (Label) super.getNodeImpl();
    }

    @Override
    public String getNameImpl() {
        return this.getNodeImpl().getLabelName();
    }
}
