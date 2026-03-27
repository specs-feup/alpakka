package pt.up.fe.specs.alpakka.ast.stmt;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;
import java.util.Optional;

public abstract class Statement extends SmaliNode {

    public static final DataKey<Optional<LineDirective>> LINE_DIRECTIVE = KeyFactory.optional("lineDirective");

    public Statement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    protected String getLine() {
        var lineDirective = getLineDirective();

        return lineDirective.map(node -> node.getCode() + "\n").orElse("");
    }

    public Statement getNextStatement() {
        var currentStatementIndex = getParent().getChildren().indexOf(this);
        if (currentStatementIndex < 0 || currentStatementIndex >= getParent().getChildren().size() - 1) {
            return null;
        }

        var nextChild = getParent().getChildren().get(currentStatementIndex + 1);

        return nextChild instanceof Statement ? (Statement) nextChild : null;
    }

    public Statement getPreviousStatement() {
        var currentStatementIndex = getParent().getChildren().indexOf(this);
        if (currentStatementIndex <= 0 || currentStatementIndex > getParent().getChildren().size() - 1) {
            return null;
        }

        var previousChild = getParent().getChildren().get(currentStatementIndex - 1);

        return previousChild instanceof Statement ? (Statement) previousChild : null;
    }

    public Optional<LineDirective> getLineDirective() {
        // TODO: remove when no ATTRIBUTES are used
        if (hasValue(ATTRIBUTES) && get(ATTRIBUTES).get("lineDirective") != null) {
            return Optional.of((LineDirective) get(ATTRIBUTES).get("lineDirective"));
        }

        return get(LINE_DIRECTIVE);
    }

}
