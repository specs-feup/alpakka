package pt.up.fe.specs.alpakka.ast.stmt.instruction;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.providers.StringProvider;

public enum BinaryOperator implements StringProvider {
    // Arithmetic operations
    ADD("add", OperatorType.ARITHMETIC),
    SUB("sub", OperatorType.ARITHMETIC),
    MUL("mul", OperatorType.ARITHMETIC),
    DIV("div", OperatorType.ARITHMETIC),
    REM("rem", OperatorType.ARITHMETIC),
    RSUB("rsub", OperatorType.ARITHMETIC),  // Reverse subtract (only formats: 22s, 22b)

    // Bitwise operations
    AND("and", OperatorType.BITWISE),
    OR("or", OperatorType.BITWISE),
    XOR("xor", OperatorType.BITWISE),
    SHL("shl", OperatorType.BITWISE),
    SHR("shr", OperatorType.BITWISE),
    USHR("ushr", OperatorType.BITWISE);

    private final String opName;
    private final OperatorType type;

    BinaryOperator(String opName, OperatorType type) {
        this.opName = opName;
        this.type = type;
    }

    public OperatorType getType() {
        return type;
    }

    public String getOpcodeName(OperandType operandType) {
        return opName + "-" + operandType.getSuffix();
    }

    public static BinaryOperator fromName(String name) {
        return SpecsEnums.getHelper(BinaryOperator.class).fromName(name);
    }

    @Override
    public String getString() {
        return opName;
    }

    public enum OperatorType {
        ARITHMETIC,
        BITWISE
    }

}
