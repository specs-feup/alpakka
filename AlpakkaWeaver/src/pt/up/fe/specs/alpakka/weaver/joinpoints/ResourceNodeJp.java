package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.Resource;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AResourceNode;

public class ResourceNodeJp extends AResourceNode {

	private final Resource resource;

	public ResourceNodeJp(Resource resource, SmaliWeaver weaver) {
		super(weaver);
		this.resource = resource;
	}

	@Override
	public SmaliNode getNode() {
		return resource;
	}

}
