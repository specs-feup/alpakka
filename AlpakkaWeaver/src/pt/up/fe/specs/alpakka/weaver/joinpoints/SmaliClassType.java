package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.type.ClassType;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassType;

public class SmaliClassType<Self extends SmaliClassType<Self>> extends AClassType<Self> {

    public SmaliClassType(ClassType node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public ClassType getNodeImpl() {
        return (ClassType) super.getNodeImpl();
    }

    @Override
    public String getClassNameImpl() {
        return this.getNodeImpl().getClassName();
    }

    @Override
    public String getPackageNameImpl() {
        return this.getNodeImpl().getPackageName();
    }

    @Override
    public AClassNode<?> getDeclImpl() {
        return SmaliJoinpoints.create(this.getNodeImpl().getDeclaration(), getWeaverEngine(), AClassNode.class);
    }
}
