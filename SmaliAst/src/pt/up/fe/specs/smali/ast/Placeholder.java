package pt.up.fe.specs.smali.ast;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

public class Placeholder extends SmaliNode {

	public static final DataKey<String> KIND = KeyFactory.string("kind");

	public Placeholder(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		return "<PLACEHOLDER> " + get(KIND) + " </PLACEHOLDER>";
	}

}
