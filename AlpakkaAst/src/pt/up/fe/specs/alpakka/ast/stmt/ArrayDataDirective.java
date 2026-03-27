package pt.up.fe.specs.alpakka.ast.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;

import java.util.Collection;

public class ArrayDataDirective extends Statement {

    // TODO: Literal should be a child
    public static final DataKey<Literal> ELEMENT_WIDTH = KeyFactory.object("elementWidth", Literal.class);

    public ArrayDataDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLine());

        var width = get(ELEMENT_WIDTH);

        sb.append(".array-data " + width.getCode() + "\n");

        for (int i = 0; i < getChildren().size(); i++) {
            sb.append(indentCode(getChildren().get(i).getCode()) + "\n");
        }

        sb.append(".end array-data");

        return sb.toString();
    }

}
