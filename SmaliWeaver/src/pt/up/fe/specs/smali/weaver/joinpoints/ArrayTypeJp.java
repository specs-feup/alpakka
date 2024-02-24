package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.type.ArrayType;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AArrayType;

public class ArrayTypeJp extends AArrayType {

	private final ArrayType arrayType;

	public ArrayTypeJp(ArrayType arrayType) {
		this.arrayType = arrayType;
	}

	@Override
	public SmaliNode getNode() {
		return this.arrayType;
	}

}
