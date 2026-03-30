package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.Opcodes;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.Statement;

import java.util.Collection;

public abstract class Instruction extends Statement {

    public final static DataKey<Opcode> OPCODE = KeyFactory.enumeration("opcode", Opcode.class);

    public Instruction(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);

        // TODO: Remove after ATTRIBUTES refactoring. Guarantees that OPCODE is always set
        if (hasValue(ATTRIBUTES)) {
            var opcodeName = get(ATTRIBUTES).get("instruction").toString();
            set(OPCODE, getOpcode(opcodeName));
        }
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLine());

        sb.append(getOpCodeName()).append(" ");

        var children = getChildren();

        for (var child : children) {
            sb.append(child.getCode());
            if (children.indexOf(child) < children.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    private Opcode getOpcode() {
        return SmaliNode.getOpcode(getOpCodeName());
    }

    public boolean canThrow() {
        var opcode = getOpcode();

        return opcode != null && opcode.canThrow();
    }

    public boolean setsResult() {
        var opcode = getOpcode();

        return opcode != null && opcode.setsResult();
    }

    public boolean setsRegister() {
        var opcode = getOpcode();

        return opcode != null && opcode.setsRegister();
    }

    public String getOpCodeName() {
        return get(OPCODE).name;
    }

    private boolean isOpcodeCompatibleFormat(Opcode opcode) {
        return opcode.format == get(OPCODE).format;
    }

    public void setOpcode(Opcode opcode) {
        if (opcode == null) {
            throw new NullPointerException("opcode");
        }
        if (!isOpcodeCompatibleFormat(opcode)) {
            throw new IllegalArgumentException(
                    "Opcode '" + opcode.name + "' is not compatible");
        }

        set(OPCODE, opcode);
    }

    public void setOpcode(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        var opcodes = Opcodes.getDefault();
        var opcode = opcodes.getOpcodeByName(name);
        if (opcode == null) {
            throw new IllegalArgumentException("Unknown opcode: '" + name + "'");
        }
        setOpcode(opcode);
    }

}
