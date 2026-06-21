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

    /** Base operator name (e.g. "add", "sub", "mul", "div"), or null if this opcode is not a binary operator. */
    public String getOperatorName() {
        try {
            return getOperator().getString();
        } catch (Exception e) {
            return null;
        }
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

        // Preserve the original addressing/encoding variant (e.g. "/lit8",
        // "/2addr") so literal and two-address forms round-trip. For target
        // operators that have no such opcode (e.g. sub-int/lit8), setOpcode
        // rejects the unknown name and the caller falls back (literal negation).
        String opcode = getOpCodeName();
        int slash = opcode.indexOf('/');
        String variant = slash >= 0 ? opcode.substring(slash) : "";
        String newOpcodeName = operator.getOpcodeName(currentType) + variant;
        setOpcode(newOpcodeName);
    }
}
