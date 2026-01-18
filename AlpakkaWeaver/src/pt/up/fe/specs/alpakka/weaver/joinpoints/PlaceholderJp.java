package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.Placeholder;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.APlaceholder;

public class PlaceholderJp extends APlaceholder {

	private final Placeholder placeholder;

	public PlaceholderJp(Placeholder placeholder, SmaliWeaver weaver) {
		super(weaver);
		this.placeholder = placeholder;
	}

	@Override
	public SmaliNode getNode() {
		return placeholder;
	}

	@Override
	public String getKindImpl() {
		return placeholder.get(Placeholder.KIND);
	}

}
