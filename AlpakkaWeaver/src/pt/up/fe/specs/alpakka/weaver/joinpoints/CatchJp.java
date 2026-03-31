package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.CatchDirective;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ACatch;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ALabelReference;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ATypeDescriptor;

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
        return this.catchDir.getExceptionTypeDescriptor().map(type -> SmaliJoinpoints.create(type, ATypeDescriptor.class)).orElse(null);
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
