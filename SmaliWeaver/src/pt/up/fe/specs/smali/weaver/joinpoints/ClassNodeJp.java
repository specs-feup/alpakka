package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.ClassNode;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AClassNode;

public class ClassNodeJp extends AClassNode {

	private final ClassNode classNode;

	public ClassNodeJp(ClassNode classNode) {
		this.classNode = classNode;
	}

	@Override
	public SmaliNode getNode() {
		return classNode;
	}

}
