package pt.up.fe.specs.alpakka.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;

public class SparseSwitchDirective extends Statement {

    public SparseSwitchDirective(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLine());

        sb.append(".sparse-switch\n");

        var children = getChildren();

        for (var child : children) {
            sb.append(indentCode(child.getCode()) + "\n");
        }

        sb.append(".end sparse-switch");

        return sb.toString();
    }

}
