package pt.up.fe.specs.smali.ast.type;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class ArrayType extends Type {

	public ArrayType(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var builder = new StringBuilder();
		builder.append("[");
		builder.append(getChildren().get(0).getCode());
		return builder.toString();
	}

}
