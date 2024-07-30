package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.SparseSwitchElement;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ASparseSwitchElement;

public class SparseSwitchElementJp extends ASparseSwitchElement {

    private final SparseSwitchElement element;

    public SparseSwitchElementJp(SparseSwitchElement element) {
        super(new ExpressionJp(element));
        this.element = element;
    }

    @Override
    public SmaliNode getNode() {
        return this.element;
    }

    @Override
    public ALabelReference getLabelImpl() {
        return SmaliJoinpoints.create(this.element.getLabel(), ALabelReference.class);
    }
}
