package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import java.util.Collection;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;

/**
 * Instruction format 22b: two registers with 8-bit immediate (vAA, vBB, #+CC).
 */
public class InstructionFormat22b extends BinaryOp {

	public InstructionFormat22b(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

}
