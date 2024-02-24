package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.Placeholder;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.APlaceholder;

public class PlaceholderJp extends APlaceholder {

	private final Placeholder placeholder;

	public PlaceholderJp(Placeholder placeholder) {
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
