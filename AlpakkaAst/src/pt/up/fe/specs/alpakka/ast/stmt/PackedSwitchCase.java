package pt.up.fe.specs.alpakka.ast.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a single case in a packed-switch construct, grouping the case entry
 * label and its body statements into a single node.
 *
 * <p>Child order:
 * <ol>
 *   <li>{@link Label} — the case entry label (e.g. {@code :pswitch_0_0})
 *   <li>body statements (zero or more)
 * </ol>
 */
public class PackedSwitchCase extends Statement {

    public PackedSwitchCase(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public Label getLabel() {
        return getChildrenOf(Label.class).get(0);
    }

    public List<Statement> getBodyStatements() {
        return getChildren(Statement.class, 1);
    }

    @Override
    public String getCode() {
        return getChildren().stream()
                .map(SmaliNode::getCode)
                .collect(Collectors.joining("\n"));
    }
}
