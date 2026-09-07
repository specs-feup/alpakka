package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.expr.FieldReference;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AFieldReference;

public class SmaliFieldReference<Self extends SmaliFieldReference<Self>> extends AFieldReference<Self> {

    public SmaliFieldReference(FieldReference node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public FieldReference getNodeImpl() {
        return (FieldReference) super.getNodeImpl();
    }
}
