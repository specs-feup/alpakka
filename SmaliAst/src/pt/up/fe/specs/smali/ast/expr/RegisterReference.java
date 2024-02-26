package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class RegisterReference extends Expression {

	public static final DataKey<String> REGISTER = KeyFactory.string("register");

	public RegisterReference(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		return get(REGISTER);
	}

}
