package pt.up.fe.specs.alpakka.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.PrimitiveLiteral;

public class RegistersDirective extends Statement {

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

        sb.append(value.getCode());

        return sb.toString();
    }

    public String getType() {
        return (String) get(SmaliNode.ATTRIBUTES).get("type");
    }

    public PrimitiveLiteral getValue() {
        return (PrimitiveLiteral) get(SmaliNode.ATTRIBUTES).get("value");
    }

}
