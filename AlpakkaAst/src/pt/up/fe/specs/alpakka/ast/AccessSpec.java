package pt.up.fe.specs.alpakka.ast;

import java.util.HashMap;
import java.util.Map;

public enum AccessSpec implements Modifier {

    PUBLIC,
    PRIVATE,
    PROTECTED,
    STATIC,
    FINAL,
    SYNCHRONIZED,
    BRIDGE,
    VARARGS,
    NATIVE,
    ABSTRACT,
    STRICTFP,
    SYNTHETIC,
    CONSTRUCTOR,
    DECLARED_SYNCHRONIZED("declared-synchronized"),
    INTERFACE,
    ENUM,
    ANNOTATION,
    VOLATILE,
    TRANSIENT;

    private static final Map<String, AccessSpec> BY_LABEL = new HashMap<>();

    static {
        for (AccessSpec access : AccessSpec.values()) {
            BY_LABEL.put(access.getLabel(), access);
        }
    }

    private final String label;

    AccessSpec(String label) {
        this.label = label;
    }

    private AccessSpec() {
        this.label = name().toLowerCase();
    }

    public String getLabel() {
        return label;
    }

    public static AccessSpec getFromLabel(String label) {
        return BY_LABEL.get(label);
    }
}
