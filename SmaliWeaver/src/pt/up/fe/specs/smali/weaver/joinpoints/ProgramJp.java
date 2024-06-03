package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.App;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AClassNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ALabelReference;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AManifest;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AProgram;

public class ProgramJp extends AProgram {

    private final App program;

    public ProgramJp(App program) {
        this.program = program;
    }

    @Override
    public SmaliNode getNode() {
        return this.program;
    }

    @Override
    public AManifest getManifestImpl() {
        return SmaliJoinpoints.create(this.program.getManifest(), AManifest.class);
    }

    @Override
    public AClassNode[] getClassesArrayImpl() {
        return this.program.getClasses().stream()
                .map(node -> SmaliJoinpoints.create(node, AClassNode.class))
                .toArray(AClassNode[]::new);
    }
}
