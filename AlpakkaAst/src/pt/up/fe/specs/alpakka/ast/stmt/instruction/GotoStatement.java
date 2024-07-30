package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.LabelRef;

public class GotoStatement extends Instruction {

	public GotoStatement(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	public LabelRef getLabel() {
		return (LabelRef) getChildren().get(0);
	}

}
