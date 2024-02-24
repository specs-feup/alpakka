package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.RegisterReference;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ARegisterReference;

public class RegisterReferenceJp extends ARegisterReference {

	private final RegisterReference registerReference;

	public RegisterReferenceJp(RegisterReference registerReference) {
		this.registerReference = registerReference;
	}

	@Override
	public SmaliNode getNode() {
		return this.registerReference;
	}
}
