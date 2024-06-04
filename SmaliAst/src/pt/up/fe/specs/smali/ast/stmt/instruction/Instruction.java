package pt.up.fe.specs.smali.ast.stmt.instruction;

import java.util.Collection;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.dexlib2.Opcodes;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.Statement;

public abstract class Instruction extends Statement {

    public Instruction(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var sb = new StringBuilder();

        sb.append(getLineDirective());

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
        // TODO: Specify the sdk version
        var opcodes = Opcodes.getDefault();
        return opcodes.getOpcodeByName(getOpCodeName());
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
        return get(ATTRIBUTES).get("instruction").toString();
    }

}
