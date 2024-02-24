package pt.up.fe.specs.smali.ast;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

public class RegisterReference extends SmaliNode {

	public static final DataKey<String> REGISTER = KeyFactory.string("register");

	public RegisterReference(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		return get(REGISTER);
	}

}
