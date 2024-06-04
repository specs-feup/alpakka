package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public abstract class Statement extends SmaliNode {

    public Statement(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    protected String getLineDirective() {
        var attributes = get(ATTRIBUTES);
        var lineDirective = (LineDirective) attributes.get("lineDirective");

        return lineDirective != null ? lineDirective.getCode() + "\n" : "";
    }

    public Statement getNextStatement() {
        var nextChild = getParent().getChildren().get(getParent().getChildren().indexOf(this) + 1);

        return nextChild instanceof Statement ? (Statement) nextChild : null;
    }

    public Statement getPreviousStatement() {
        var previousChild = getParent().getChildren().get(getParent().getChildren().indexOf(this) - 1);

        return previousChild instanceof Statement ? (Statement) previousChild : null;
    }

}
