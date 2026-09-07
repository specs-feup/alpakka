package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.Placeholder;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APlaceholder;

public class SmaliPlaceholder<Self extends SmaliPlaceholder<Self>> extends APlaceholder<Self> {

    public SmaliPlaceholder(Placeholder node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public Placeholder getNodeImpl() {
        return (Placeholder) super.getNodeImpl();
    }

    @Override
    public String getKindImpl() {
        return this.getNodeImpl().get(Placeholder.KIND);
    }
}
