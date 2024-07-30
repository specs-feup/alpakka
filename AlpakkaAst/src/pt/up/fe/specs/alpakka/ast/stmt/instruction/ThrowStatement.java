package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;

public class ThrowStatement extends Instruction {

	public ThrowStatement(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

}
