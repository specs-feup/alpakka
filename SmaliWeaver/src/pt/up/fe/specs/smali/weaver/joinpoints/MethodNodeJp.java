package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.MethodNode;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AMethodNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AMethodPrototype;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ARegistersDirective;

public class MethodNodeJp extends AMethodNode {

	private final MethodNode methodNode;

	public MethodNodeJp(MethodNode methodNode) {
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
		return SmaliJoinpoints.create(this.methodNode.getPrototype(), AMethodPrototype.class);
	}

	@Override
	public ARegistersDirective getRegistersDirectiveImpl() {
		return SmaliJoinpoints.create(this.methodNode.getRegistersDirective(), ARegistersDirective.class);
	}

	@Override
	public Boolean getIsStaticImpl() {
		return methodNode.isStatic();
	}
}
