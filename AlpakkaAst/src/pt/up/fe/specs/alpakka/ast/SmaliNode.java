package pt.up.fe.specs.alpakka.ast;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.Opcodes;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.treenode.DataNode;
import pt.up.fe.specs.alpakka.ast.context.SmaliContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            HashMap::new);

    /// DATAKEYS END

    public SmaliNode(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public static Opcode getOpcode(String opcodeName) {
        var opcode = Opcodes.getDefault().getOpcodeByName(opcodeName);
        Objects.requireNonNull(opcode, "Opcode not found for name: " + opcodeName);
        return opcode;
    }

    @Override
    protected Class<SmaliNode> getBaseClass() {

        return SmaliNode.class;
    }

    public abstract String getCode();

    protected String indentCode(String code) {
        var lines = code.split("\n");
        var sb = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            sb.append("\t" + lines[i]);
            if (i < lines.length - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public SmaliContext getContext() {
        return get(CONTEXT);
    }

}
