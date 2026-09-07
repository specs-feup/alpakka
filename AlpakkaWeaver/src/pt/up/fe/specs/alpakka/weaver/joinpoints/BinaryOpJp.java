package pt.up.fe.specs.alpakka.weaver.joinpoints;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.BinaryOp;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.ABinaryOp;

public class BinaryOpJp extends ABinaryOp {

  private final BinaryOp opInstruction;

  public BinaryOpJp(BinaryOp opInstruction, SmaliWeaver weaver) {
    super(new InstructionJp(opInstruction, weaver), weaver);
    this.opInstruction = opInstruction;
  }

  @Override
  public SmaliNode getNode() {
    return opInstruction;
  }

  @Override
  public void setOperatorImpl(String operator) {
    this.opInstruction.setOperator(operator);
  }

}
