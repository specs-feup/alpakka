package pt.up.fe.specs.smali.ast;

import java.util.HashMap;
import java.util.Map;

public enum HiddenApiRestriction implements Modifier {

    WHITELIST,
    GREYLIST,
    BLACKLIST,
    GREYLIST_MAX_O("greylist-max-o"),
    GREYLIST_MAX_P("greylist-max-p"),
    GREYLIST_MAX_Q("greylist-max-q"),
    GREYLIST_MAX_R("greylist-max-r"),
    CORE_PLATFORM_API("core-platform-api"),
    TEST_API("test-api");

    private static final Map<String, HiddenApiRestriction> BY_LABEL = new HashMap<>();

    static {
        for (HiddenApiRestriction restriction : HiddenApiRestriction.values()) {
            BY_LABEL.put(restriction.getLabel(), restriction);
        }
    }

    private String label;

    private HiddenApiRestriction(String label) {
        this.label = label;
    }

    private HiddenApiRestriction() {
        this.label = name().toLowerCase();
    }

    public String getLabel() {
        return label;
    }

    public static HiddenApiRestriction getFromLabel(String label) {
        return BY_LABEL.get(label);
    }
}
