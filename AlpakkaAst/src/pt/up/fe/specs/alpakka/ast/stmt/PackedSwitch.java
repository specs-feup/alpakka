package pt.up.fe.specs.alpakka.ast.stmt;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.SwitchStatement;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A compound node representing a complete packed-switch construct, including the
 * switch instruction, per-case groups, and the data table.
 *
 * <p>Direct children:
 * <ul>
 *   <li>one {@link SwitchStatement}     — "packed-switch &lt;reg&gt;, :&lt;prefix&gt;_data"
 *   <li>zero or more {@link PackedSwitchCase} — each holds its entry label and body
 *   <li>one {@link Label}               — the data label (":&lt;prefix&gt;_data")
 *   <li>one {@link PackedSwitchDirective} — ".packed-switch 0x&lt;startValue&gt; ... .end packed-switch"
 * </ul>
 */
public class PackedSwitch extends Statement {

    public PackedSwitch(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public SwitchStatement getSwitchStatement() {
        return getChildrenOf(SwitchStatement.class).get(0);
    }

    public List<PackedSwitchCase> getCases() {
        return getChildrenOf(PackedSwitchCase.class);
    }

    public Label getDataLabel() {
        return getChildrenOf(Label.class).get(0);
    }

    public PackedSwitchDirective getDirective() {
        return getChildrenOf(PackedSwitchDirective.class).get(0);
    }

    @Override
    public String getCode() {
        return getChildren().stream()
                .map(SmaliNode::getCode)
                .collect(Collectors.joining("\n"));
    }
}
