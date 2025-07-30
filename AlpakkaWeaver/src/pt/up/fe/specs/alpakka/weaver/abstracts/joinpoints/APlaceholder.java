package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

/**
 * Auto-Generated class for join point APlaceholder
 * This class is overwritten by the Weaver Generator.
 * 
 * Placeholder node
 * @author Lara Weaver Generator
 */
public abstract class APlaceholder extends ASmaliWeaverJoinPoint {

    /**
     * Get value on attribute kind
     * @return the attribute's value
     */
    public abstract String getKindImpl();

    /**
     * Get value on attribute kind
     * @return the attribute's value
     */
    public final Object getKind() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "kind", Optional.empty());
        	}
        	String result = this.getKindImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "kind", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "kind", e);
        }
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "placeholder";
    }
    /**
     * 
     */
    protected enum PlaceholderAttributes {
        KIND("kind"),
        AST("ast"),
        CHILDREN("children"),
        CODE("code"),
        DESCENDANTS("descendants"),
        GETANCESTOR("getAncestor"),
        GETCHILD("getChild"),
        GETDESCENDANTS("getDescendants"),
        GETDESCENDANTSANDSELF("getDescendantsAndSelf"),
        ID("id"),
        PARENT("parent"),
        ROOT("root");
        private String name;

        /**
         * 
         */
        private PlaceholderAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<PlaceholderAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(PlaceholderAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
