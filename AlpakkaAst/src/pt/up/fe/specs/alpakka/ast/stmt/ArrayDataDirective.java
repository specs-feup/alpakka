package pt.up.fe.specs.alpakka.ast.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;

import java.util.Collection;
import java.util.List;

public class ArrayDataDirective extends Statement {

    public static final DataKey<Integer> ELEMENT_WIDTH = KeyFactory.integer("elementWidth");

    public ArrayDataDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public List<Literal> getElements() {
        return getChildren(Literal.class);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLine());

        var width = get(ELEMENT_WIDTH);

        sb.append(".array-data " + width + "\n");

        for (var element : getElements()) {
            sb.append(indentCode(element.getCode()) + "\n");
        }

        sb.append(".end array-data");

        return sb.toString();
    }

}
