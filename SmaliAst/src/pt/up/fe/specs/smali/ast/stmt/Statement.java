package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public abstract class Statement extends SmaliNode {

    public Statement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    protected String getLine() {
        var lineDirective = getLineDirective();

        return lineDirective != null ? lineDirective.getCode() + "\n" : "";
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

    public LineDirective getLineDirective() {
        return (LineDirective) get(ATTRIBUTES).get("lineDirective");
    }

}
