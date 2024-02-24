package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.type.Type;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AType;

public class TypeJp extends AType {

	private final Type type;

	public TypeJp(Type type) {
		this.type = type;
	}

	@Override
	public SmaliNode getNode() {
		return this.type;
	}
}
