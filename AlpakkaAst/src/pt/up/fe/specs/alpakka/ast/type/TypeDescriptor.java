package pt.up.fe.specs.alpakka.ast.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

public abstract class TypeDescriptor extends SmaliNode {

    public TypeDescriptor(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }
}
