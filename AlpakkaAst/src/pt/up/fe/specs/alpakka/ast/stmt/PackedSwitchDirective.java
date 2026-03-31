package pt.up.fe.specs.alpakka.ast.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

public class PackedSwitchDirective extends Statement {

    public static final DataKey<String> VALUE = KeyFactory.string("value");

    public PackedSwitchDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLine());

        var value = get(VALUE);

        sb.append(".packed-switch " + value + "\n");

        for (int i = 0; i < getChildren().size(); i++) {
            sb.append(indentCode(getChildren().get(i).getCode()) + "\n");
        }

        sb.append(".end packed-switch");

        return sb.toString();
    }

}
