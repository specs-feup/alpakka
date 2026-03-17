package pt.up.fe.specs.alpakka.ast.stmt.instruction;

public enum OperandType {
    INT("int"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double");

    private final String suffix;

    OperandType(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public static OperandType fromOpcodeName(String opcodeName) {

        String lowerName = opcodeName.toLowerCase();

        if (lowerName.endsWith("-long")) {
            return LONG;
        } else if (lowerName.endsWith("-float")) {
            return FLOAT;
        } else if (lowerName.endsWith("-double")) {
            return DOUBLE;
        } else if (lowerName.endsWith("-int")) {
            return INT;
        }

        throw new IllegalArgumentException("Unknown operand type in opcode: " + opcodeName);
    }
}
