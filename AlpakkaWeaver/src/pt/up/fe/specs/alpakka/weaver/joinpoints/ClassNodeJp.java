package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.ClassNode;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AClassType;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AFieldNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodNode;

public class ClassNodeJp extends AClassNode {

	private final ClassNode classNode;

	public ClassNodeJp(ClassNode classNode) {
		this.classNode = classNode;
	}

	@Override
	public SmaliNode getNode() {
		return classNode;
	}

	@Override
	public AMethodNode[] getMethodsArrayImpl() {
		return classNode.getMethods().stream()
				.map(method -> SmaliJoinpoints.create(method, AMethodNode.class))
				.toArray(AMethodNode[]::new);
	}

	@Override
	public AFieldNode[] getFieldsArrayImpl() {
		return classNode.getFields().stream()
				.map(field -> SmaliJoinpoints.create(field, AFieldNode.class))
				.toArray(AFieldNode[]::new);
	}

	@Override
	public AClassType getClassDescriptorImpl() {
		return SmaliJoinpoints.create(classNode.getClassDescriptor(), AClassType.class);
	}

	@Override
	public AClassType getSuperClassDescriptorImpl() {
		return SmaliJoinpoints.create(classNode.getSuperClass(), AClassType.class);
	}
}
