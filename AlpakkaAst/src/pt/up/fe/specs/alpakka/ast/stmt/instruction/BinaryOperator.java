package pt.up.fe.specs.alpakka.ast.stmt.instruction;

public enum BinaryOperator {
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

    public String getOpName() {
        return opName;
    }

    public OperatorType getType() {
        return type;
    }

    public String getOpcodeName(OperandType operandType) {
        return opName + "-" + operandType.getSuffix();
    }

    public static BinaryOperator fromName(String name) {
        if (name == null) {
            return null;
        }
        
        String lowerName = name.toLowerCase();
        
        for (BinaryOperator op : BinaryOperator.values()) {
            if (lowerName.contains(op.opName)) {
                return op;
            }
        }
        
        return null;
    }

    public enum OperatorType {
        ARITHMETIC,
        BITWISE
    }

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
            if (opcodeName == null) {
                return null;
            }
            
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
            
            return null;
        }
    }
}
