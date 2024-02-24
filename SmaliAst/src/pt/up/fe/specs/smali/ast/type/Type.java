package pt.up.fe.specs.smali.ast.type;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public abstract class Type extends SmaliNode {

	public Type(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}
}
