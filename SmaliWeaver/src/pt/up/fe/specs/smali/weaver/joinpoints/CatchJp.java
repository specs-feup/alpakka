package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.CatchDirective;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ACatch;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ALabelReference;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ATypeDescriptor;

public class CatchJp extends ACatch {

    private final CatchDirective catchDir;

    public CatchJp(CatchDirective catchDir) {
        super(new StatementJp(catchDir));
        this.catchDir = catchDir;
    }

    @Override
    public SmaliNode getNode() {
        return this.catchDir;
    }

    @Override
    public ATypeDescriptor getExceptionImpl() {
        return SmaliJoinpoints.create(this.catchDir.getExceptionTypeDescriptor(), ATypeDescriptor.class);
    }

    @Override
    public ALabelReference getTryStartImpl() {
        return SmaliJoinpoints.create(this.catchDir.getTryStartLabelRef(), ALabelReference.class);
    }

    @Override
    public ALabelReference getTryEndImpl() {
        return SmaliJoinpoints.create(this.catchDir.getTryEndLabelRef(), ALabelReference.class);
    }

    @Override
    public ALabelReference getCatchImpl() {
        return SmaliJoinpoints.create(this.catchDir.getCatchLabelRef(), ALabelReference.class);
    }
}
