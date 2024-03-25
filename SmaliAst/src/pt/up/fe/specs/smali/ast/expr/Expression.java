package pt.up.fe.specs.smali.ast.expr;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public abstract class Expression extends SmaliNode {

    public Expression(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

}
