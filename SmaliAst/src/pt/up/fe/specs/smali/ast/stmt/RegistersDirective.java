package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.Literal;

public class RegistersDirective extends Statement {

    public RegistersDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var attributes = get(ATTRIBUTES);

        var type = (String) attributes.get("type");
        var value = (Literal) attributes.get("value");

        sb.append(getLineDirective());

        if (type.equals("I_REGISTERS")) {
            sb.append(".registers ");
        } else if (type.equals("I_LOCALS")) {
            sb.append(".locals ");
        }

        sb.append(value.getCode());

        return sb.toString();
    }

}
