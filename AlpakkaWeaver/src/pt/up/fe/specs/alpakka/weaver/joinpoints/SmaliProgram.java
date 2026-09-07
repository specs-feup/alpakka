package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.App;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AManifest;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AProgram;

public class SmaliProgram<Self extends SmaliProgram<Self>> extends AProgram<Self> {

    public SmaliProgram(App node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public App getNodeImpl() {
        return (App) super.getNodeImpl();
    }

    @Override
    public AManifest<?> getManifestImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getManifest(), getWeaverEngine(), AManifest.class);
    }

    @Override
    public AClassNode<?>[] getClassesImpl() {
        return this.getNodeImpl().getClasses().stream()
                .map(node -> SmaliJoinpoints.create(node, getWeaverEngine(), AClassNode.class))
                .toArray(AClassNode<?>[]::new);
    }

    @Override
    public void buildApkImpl(String outputName) {
        this.getNodeImpl().buildApk(outputName);
    }
}
