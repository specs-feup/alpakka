package pt.up.fe.specs.smali.ast.type;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class PrimitiveType extends Type {

	public static final DataKey<String> TYPE = KeyFactory.string("type");

	public PrimitiveType(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		return get(TYPE);
	}

}
