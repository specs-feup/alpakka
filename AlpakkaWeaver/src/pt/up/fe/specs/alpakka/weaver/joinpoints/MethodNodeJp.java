package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.MethodNode;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodNode;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AMethodPrototype;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ARegistersDirective;

public class MethodNodeJp extends AMethodNode {

	private final MethodNode methodNode;

	public MethodNodeJp(MethodNode methodNode, SmaliWeaver weaver) {
		super(weaver);
		this.methodNode = methodNode;
	}

	@Override
	public SmaliNode getNode() {
		return methodNode;
	}

	@Override
	public String getNameImpl() {
		return methodNode.getMethodReferenceName();
	}

	@Override
	public String getReferenceNameImpl() {
		return methodNode.getMethodReferenceName();
	}

	@Override
	public AMethodPrototype getPrototypeImpl() {
		return SmaliJoinpoints.create(this.methodNode.getPrototype(), getWeaverEngine(), AMethodPrototype.class);
	}

	@Override
	public ARegistersDirective getRegistersDirectiveImpl() {
		return SmaliJoinpoints.create(this.methodNode.getRegistersDirective(), getWeaverEngine(), ARegistersDirective.class);
	}

	@Override
	public Boolean getIsStaticImpl() {
		return methodNode.isStatic();
	}
}
