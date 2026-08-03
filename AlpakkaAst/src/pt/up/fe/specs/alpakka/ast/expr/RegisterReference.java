package pt.up.fe.specs.alpakka.ast.expr;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

public class RegisterReference extends Expression {

    public static final DataKey<String> REGISTER = KeyFactory.string("register");

    // TODO: Algorithm that annotates register references with the corresponding type
    public RegisterReference(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        return get(REGISTER);
    }

}
