package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AType;

public class TypeJp extends AType {

	private final TypeDescriptor type;

	public TypeJp(TypeDescriptor type) {
		this.type = type;
	}

	@Override
	public SmaliNode getNode() {
		return this.type;
	}
}
