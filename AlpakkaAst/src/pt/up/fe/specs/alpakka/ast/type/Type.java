package pt.up.fe.specs.alpakka.ast.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

/**
 * Represents a type in SMALI.
 */
public abstract class Type extends SmaliNode {

    public Type(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }
}
