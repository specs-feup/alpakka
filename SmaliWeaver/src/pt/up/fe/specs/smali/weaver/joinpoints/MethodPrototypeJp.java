package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AMethodPrototype;

public class MethodPrototypeJp extends AMethodPrototype {

	private final MethodPrototype methodPrototype;

	public MethodPrototypeJp(MethodPrototype methodPrototype) {
		this.methodPrototype = methodPrototype;
	}

	@Override
	public SmaliNode getNode() {
		return this.methodPrototype;
	}

}
