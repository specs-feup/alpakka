package pt.up.fe.specs.alpakka.ast.expr.literal;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

public class SubannotationDirective extends Literal {

    public SubannotationDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();
        var classDescriptor = get(TYPE).orElseThrow();

        sb.append(".subannotation ").append(classDescriptor.getCode()).append("\n");

        for (var child : getChildren()) {
            sb.append(indentCode(child.getCode())).append("\n");
        }

        sb.append(".end subannotation");

        return sb.toString();
    }

}
