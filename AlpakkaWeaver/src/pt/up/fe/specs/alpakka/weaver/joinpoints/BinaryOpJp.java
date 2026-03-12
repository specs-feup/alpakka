package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.Instruction;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ABinaryOp;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AInstruction;

public class BinaryOpJp extends ABinaryOp {

  private final Instruction opInstruction;

  public BinaryOpJp(Instruction opInstruction) {
    super(new InstructionJp(opInstruction));
    this.opInstruction = opInstruction;
  }

  @Override
  public SmaliNode getNode() {
    return opInstruction;
  }

  @Override
  public void setOpImpl(String operation) {
    opInstruction.setOpcode(operation);
  }

}
