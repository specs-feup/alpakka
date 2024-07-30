package pt.up.fe.specs.alpakka.ast.expr;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;

public class RegisterRange extends Expression {

    public RegisterRange(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var children = getChildren();

        var sb = new StringBuilder();

        sb.append("{");

        if (!children.isEmpty()) {
            sb.append(children.get(0).getCode());

            if (children.size() > 1) {
                sb.append(" .. ");
                sb.append(children.get(1).getCode());
            }
        }

        sb.append("}");

        return sb.toString();
    }

}
