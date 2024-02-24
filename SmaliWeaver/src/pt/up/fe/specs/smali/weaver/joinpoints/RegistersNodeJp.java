package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.RegistersNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ARegistersNode;

public class RegistersNodeJp extends ARegistersNode {

	private final RegistersNode registersNode;

	public RegistersNodeJp(RegistersNode registersNode) {
		this.registersNode = registersNode;
	}

	@Override
	public SmaliNode getNode() {
		return this.registersNode;
	}

}
