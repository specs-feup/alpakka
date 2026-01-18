package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.ClassType;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassType;

public class ClassTypeJp extends AClassType {

    private final ClassType classType;

    public ClassTypeJp(ClassType classType, SmaliWeaver weaver) {
        super(new TypeDescriptorJp(classType, weaver), weaver);
        this.classType = classType;
    }

    @Override
    public String getClassNameImpl() {
        return classType.getClassName();
    }

    @Override
    public String getPackageNameImpl() {
        return classType.getPackageName();
    }

    @Override
    public AClassNode getDeclImpl() {
        return SmaliJoinpoints.create(classType.getDeclaration(), getWeaverEngine(), AClassNode.class);
    }

    @Override
    public SmaliNode getNode() {
        return classType;
    }

}
