package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.type.PrimitiveType;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.APrimitiveType;

public class PrimitiveTypeJp extends APrimitiveType {

	private final PrimitiveType primitiveType;

	public PrimitiveTypeJp(PrimitiveType primitiveType) {
		this.primitiveType = primitiveType;
	}

	@Override
	public SmaliNode getNode() {
		return this.primitiveType;
	}

}
