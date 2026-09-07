package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.stmt.CatchDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ACatch;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

public class SmaliCatch<Self extends SmaliCatch<Self>> extends ACatch<Self> {

    public SmaliCatch(CatchDirective node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public CatchDirective getNodeImpl() {
        return (CatchDirective) super.getNodeImpl();
    }

    @Override
    public ATypeDescriptor<?> getExceptionImpl() {
        return this.getNodeImpl().getExceptionTypeDescriptor()
                .map(type -> SmaliJoinpoints.create(type, getWeaverEngine(), ATypeDescriptor.class))
                .orElse(null);
    }

    @Override
    public ALabelReference<?> getTryStartImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getTryStartLabelRef(), getWeaverEngine(),
                ALabelReference.class);
    }

    @Override
    public ALabelReference<?> getTryEndImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getTryEndLabelRef(), getWeaverEngine(),
                ALabelReference.class);
    }

    @Override
    public ALabelReference<?> getCatchImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getCatchLabelRef(), getWeaverEngine(),
                ALabelReference.class);
    }
}
