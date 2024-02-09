package pt.up.fe.specs.smali.ast;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.treenode.DataNode;

import pt.up.fe.specs.smali.ast.context.SmaliContext;

public abstract class SmaliNode extends DataNode<SmaliNode> {

    /// DATAKEYS BEGIN

    /**
     * Id of the node.
     */
    public final static DataKey<String> ID = KeyFactory.string("id");

    /**
     * Context of the tree.
     */
    public final static DataKey<SmaliContext> CONTEXT = KeyFactory.object("context", SmaliContext.class);

    /// DATAKEYS END

    public SmaliNode(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    protected Class<SmaliNode> getBaseClass() {

        return SmaliNode.class;
    }

    public abstract String getCode();

}
