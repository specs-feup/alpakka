package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.Resource;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AResourceNode;

public class ResourceNodeJp extends AResourceNode {

	private final Resource resource;

	public ResourceNodeJp(Resource resource) {
		this.resource = resource;
	}

	@Override
	public SmaliNode getNode() {
		return resource;
	}

}
