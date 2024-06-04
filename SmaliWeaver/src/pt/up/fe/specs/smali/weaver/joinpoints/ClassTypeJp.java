package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.ClassType;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AClassNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AClassType;

public class ClassTypeJp extends AClassType {

    private final ClassType classType;

    public ClassTypeJp(ClassType classType) {
        super(new TypeDescriptorJp(classType));
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
        return SmaliJoinpoints.create(classType.getDeclaration(), AClassNode.class);
    }

    @Override
    public SmaliNode getNode() {
        return classType;
    }

}
