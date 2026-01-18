package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.CatchDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ACatch;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

public class CatchJp extends ACatch {

    private final CatchDirective catchDir;

    public CatchJp(CatchDirective catchDir, SmaliWeaver weaver) {
        super(new StatementJp(catchDir, weaver), weaver);
        this.catchDir = catchDir;
    }

    @Override
    public SmaliNode getNode() {
        return this.catchDir;
    }

    @Override
    public ATypeDescriptor getExceptionImpl() {
        return SmaliJoinpoints.create(this.catchDir.getExceptionTypeDescriptor(), getWeaverEngine(), ATypeDescriptor.class);
    }

    @Override
    public ALabelReference getTryStartImpl() {
        return SmaliJoinpoints.create(this.catchDir.getTryStartLabelRef(), getWeaverEngine(), ALabelReference.class);
    }

    @Override
    public ALabelReference getTryEndImpl() {
        return SmaliJoinpoints.create(this.catchDir.getTryEndLabelRef(), getWeaverEngine(), ALabelReference.class);
    }

    @Override
    public ALabelReference getCatchImpl() {
        return SmaliJoinpoints.create(this.catchDir.getCatchLabelRef(), getWeaverEngine(), ALabelReference.class);
    }
}
