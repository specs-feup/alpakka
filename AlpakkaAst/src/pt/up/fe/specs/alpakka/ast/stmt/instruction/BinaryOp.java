package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.SmaliNode;

public abstract class BinaryOp extends Instruction {

    public BinaryOp(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public BinaryOperator getOperator() {
        return BinaryOperator.fromName(getOpCodeName());
    }

    public BinaryOperator.OperandType getOperandType() {
        return BinaryOperator.OperandType.fromOpcodeName(getOpCodeName());
    }

    public void setOperator(String operatorName) {
        if (operatorName.isBlank()) {
            throw new IllegalArgumentException("Operator name cannot be blank");
        }

        BinaryOperator operator = BinaryOperator.fromName(operatorName);
        if (operator == null) {
            throw new IllegalArgumentException("Invalid operator name: " + operatorName);
        }

        setOperator(operator);
    }

    public void setOperator(BinaryOperator operator) {
        if (operator == null) {
            throw new NullPointerException("operator");
        }
        
        BinaryOperator currentOperator = getOperator();
        if (currentOperator == null) {
            throw new IllegalStateException("Current operator cannot be determined");
        }

        BinaryOperator.OperandType currentType = getOperandType();
        if (currentType == null) {
            throw new IllegalStateException("Current operator does not have a valid operand type");
        }

        // Check that the new operator is the same type (arithmetic or bitwise) as the current operator
        if (currentOperator.getType() != operator.getType()) {
            throw new IllegalArgumentException(
                "Cannot change from " + currentOperator.getType() + " operator '" + currentOperator +
                "' to " + operator.getType() + " operator '" + operator + "'");
        }

        String newOpcodeName = operator.getOpcodeName(currentType);
        setOpcode(newOpcodeName);
    }
}
