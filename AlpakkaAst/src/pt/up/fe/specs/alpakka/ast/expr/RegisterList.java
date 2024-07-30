package pt.up.fe.specs.alpakka.ast.expr;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;

public class RegisterList extends Expression {

	public RegisterList(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var children = getChildren();

		var sb = new StringBuilder();

		sb.append("{");

		for (var child : children) {
			sb.append(child.getCode());
			if (children.indexOf(child) < children.size() - 1)
				sb.append(", ");
		}

		sb.append("}");

		return sb.toString();
	}

}
