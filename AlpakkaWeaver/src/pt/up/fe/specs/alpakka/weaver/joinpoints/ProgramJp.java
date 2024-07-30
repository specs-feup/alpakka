package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.App;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AManifest;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AProgram;

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

    @Override
    public Void buildApkImpl(String outputName) {
        this.program.buildApk(outputName);
        return null;
    }
}
