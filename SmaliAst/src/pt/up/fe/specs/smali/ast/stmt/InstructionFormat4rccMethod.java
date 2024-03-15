package pt.up.fe.specs.smali.ast.stmt;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class InstructionFormat4rccMethod extends Instruction {

	public InstructionFormat4rccMethod(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}
}
