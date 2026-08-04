package pt.up.fe.specs.alpakka.ast.type;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

public class NullType extends Type {

    public NullType(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        throw new RuntimeException("NullType should not generate code");
    }
}
