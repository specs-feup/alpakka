package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.SmaliNode;

import java.util.Collection;
import java.util.Objects;

public abstract class BinaryOp extends Instruction {

    public BinaryOp(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    public BinaryOperator getOperator() {
        return BinaryOperator.fromName(getOpCodeName());
    }

    public OperandType getOperandType() {
        return OperandType.fromOpcodeName(getOpCodeName());
    }

    public void setOperator(String operatorName) {
        Objects.requireNonNull(operatorName);

        if (operatorName.isBlank()) {
            throw new IllegalArgumentException("Operator name cannot be blank");
        }

        BinaryOperator operator = BinaryOperator.fromName(operatorName);

        setOperator(operator);
    }

    public void setOperator(BinaryOperator operator) {
        Objects.requireNonNull(operator);

        BinaryOperator currentOperator = getOperator();

        OperandType currentType = getOperandType();

        // Check that the new operator is the same type (arithmetic or bitwise) as the current operator
        // TODO: test if this is necessary, or if SMALI already enforces this
        if (currentOperator.getType() != operator.getType()) {
            throw new IllegalArgumentException(
                    "Cannot change from " + currentOperator.getType() + " operator '" + currentOperator +
                            "' to " + operator.getType() + " operator '" + operator + "'");
        }

        String newOpcodeName = operator.getOpcodeName(currentType);
        setOpcode(newOpcodeName);
    }
}
