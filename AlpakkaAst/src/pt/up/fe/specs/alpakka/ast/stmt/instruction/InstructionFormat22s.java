package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import java.util.Collection;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;

/**
 * Instruction format 22s: two registers with 16-bit immediate (vAA, vBB, #+CCCC).
 */
public class InstructionFormat22s extends BinaryOp {

	public InstructionFormat22s(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

}
