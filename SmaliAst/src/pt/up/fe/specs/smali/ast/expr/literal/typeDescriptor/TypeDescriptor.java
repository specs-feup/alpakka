package pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.literal.Literal;

public abstract class TypeDescriptor extends Literal {

    public TypeDescriptor(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
        this.setType(this);
    }
}
