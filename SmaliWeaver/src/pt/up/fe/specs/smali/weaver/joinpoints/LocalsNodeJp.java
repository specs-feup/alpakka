package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.LocalsNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ALocalsNode;

public class LocalsNodeJp extends ALocalsNode {

	private final LocalsNode localsNode;

	public LocalsNodeJp(LocalsNode localsNode) {
		this.localsNode = localsNode;
	}

	@Override
	public SmaliNode getNode() {
		return this.localsNode;
	}
}
