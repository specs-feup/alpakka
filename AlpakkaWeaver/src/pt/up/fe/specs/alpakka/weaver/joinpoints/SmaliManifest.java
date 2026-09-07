package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.Manifest;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AManifest;

public class SmaliManifest<Self extends SmaliManifest<Self>> extends AManifest<Self> {

    public SmaliManifest(Manifest node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public Manifest getNodeImpl() {
        return (Manifest) super.getNodeImpl();
    }

    @Override
    public String getPackageNameImpl() {
        return this.getNodeImpl().getPackageName();
    }

    @Override
    public String[] getActivitiesImpl() {
        return this.getNodeImpl().getActivities().toArray(new String[0]);
    }

    @Override
    public String[] getServicesImpl() {
        return this.getNodeImpl().getServices().toArray(new String[0]);
    }
}
