package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

/**
 * Auto-Generated class for join point AMethodNode
 * This class is overwritten by the Weaver Generator.
 * 
 * Method definition
 * @author Lara Weaver Generator
 */
public abstract class AMethodNode extends ASmaliWeaverJoinPoint {

    /**
     * Get value on attribute isStatic
     * @return the attribute's value
     */
    public abstract Boolean getIsStaticImpl();

    /**
     * Get value on attribute isStatic
     * @return the attribute's value
     */
    public final Object getIsStatic() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "isStatic", Optional.empty());
        	}
        	Boolean result = this.getIsStaticImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "isStatic", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "isStatic", e);
        }
    }

    /**
     * Get value on attribute name
     * @return the attribute's value
     */
    public abstract String getNameImpl();

    /**
     * Get value on attribute name
     * @return the attribute's value
     */
    public final Object getName() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "name", Optional.empty());
        	}
        	String result = this.getNameImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "name", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "name", e);
        }
    }

    /**
     * Get value on attribute prototype
     * @return the attribute's value
     */
    public abstract AMethodPrototype getPrototypeImpl();

    /**
     * Get value on attribute prototype
     * @return the attribute's value
     */
    public final Object getPrototype() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "prototype", Optional.empty());
        	}
        	AMethodPrototype result = this.getPrototypeImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "prototype", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "prototype", e);
        }
    }

    /**
     * Get value on attribute referenceName
     * @return the attribute's value
     */
    public abstract String getReferenceNameImpl();

    /**
     * Get value on attribute referenceName
     * @return the attribute's value
     */
    public final Object getReferenceName() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "referenceName", Optional.empty());
        	}
        	String result = this.getReferenceNameImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "referenceName", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "referenceName", e);
        }
    }

    /**
     * Get value on attribute registersDirective
     * @return the attribute's value
     */
    public abstract ARegistersDirective getRegistersDirectiveImpl();

    /**
     * Get value on attribute registersDirective
     * @return the attribute's value
     */
    public final Object getRegistersDirective() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "registersDirective", Optional.empty());
        	}
        	ARegistersDirective result = this.getRegistersDirectiveImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "registersDirective", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "registersDirective", e);
        }
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "methodNode";
    }
    /**
     * 
     */
    protected enum MethodNodeAttributes {
        ISSTATIC("isStatic"),
        NAME("name"),
        PROTOTYPE("prototype"),
        REFERENCENAME("referenceName"),
        REGISTERSDIRECTIVE("registersDirective"),
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
        private MethodNodeAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<MethodNodeAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(MethodNodeAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
