package pt.up.fe.specs.alpakka.ast.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

public class RegistersDirective extends Statement {

    public static final DataKey<String> TYPE = KeyFactory.string("type");

    public static final DataKey<Integer> VALUE = KeyFactory.integer("value");

    public RegistersDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        var type = getType();
        var value = getValue();

        sb.append(getLine());

        if (type.equals("I_REGISTERS")) {
            sb.append(".registers ");
        } else if (type.equals("I_LOCALS")) {
            sb.append(".locals ");
        }

        sb.append(value);

        return sb.toString();
    }

    public String getType() {
        return get(TYPE);
    }

    public Integer getValue() {
        return get(VALUE);
    }

    public void setValue(int value) {
        set(VALUE, value);
    }

}
