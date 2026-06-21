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

        // Strip any addressing/encoding variant suffix (e.g. "/lit8", "/lit16",
        // "/2addr") so the operand type can be determined for every form.
        int slash = lowerName.indexOf('/');
        if (slash >= 0) {
            lowerName = lowerName.substring(0, slash);
        }

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
