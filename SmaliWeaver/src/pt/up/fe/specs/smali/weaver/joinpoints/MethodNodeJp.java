package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.MethodNode;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AMethodNode;

public class MethodNodeJp extends AMethodNode {

	private final MethodNode methodNode;

	public MethodNodeJp(MethodNode methodNode) {
		this.methodNode = methodNode;
	}

	@Override
	public SmaliNode getNode() {
		return methodNode;
	}

}
