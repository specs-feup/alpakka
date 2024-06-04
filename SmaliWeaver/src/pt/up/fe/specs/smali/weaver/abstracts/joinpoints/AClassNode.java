package pt.up.fe.specs.smali.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import pt.up.fe.specs.smali.weaver.abstracts.ASmaliWeaverJoinPoint;
import java.util.List;
import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Auto-Generated class for join point AClassNode
 * This class is overwritten by the Weaver Generator.
 * 
 * Class definition
 * @author Lara Weaver Generator
 */
public abstract class AClassNode extends ASmaliWeaverJoinPoint {

    /**
     * Get value on attribute methods
     * @return the attribute's value
     */
    public abstract AMethodNode[] getMethodsArrayImpl();

    /**
     * Get value on attribute methods
     * @return the attribute's value
     */
    public Object getMethodsImpl() {
        AMethodNode[] aMethodNodeArrayImpl0 = getMethodsArrayImpl();
        Object nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(aMethodNodeArrayImpl0);
        return nativeArray0;
    }

    /**
     * Get value on attribute methods
     * @return the attribute's value
     */
    public final Object getMethods() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "methods", Optional.empty());
        	}
        	Object result = this.getMethodsImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "methods", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "methods", e);
        }
    }

    /**
     * Get value on attribute fields
     * @return the attribute's value
     */
    public abstract AFieldNode[] getFieldsArrayImpl();

    /**
     * Get value on attribute fields
     * @return the attribute's value
     */
    public Object getFieldsImpl() {
        AFieldNode[] aFieldNodeArrayImpl0 = getFieldsArrayImpl();
        Object nativeArray0 = getWeaverEngine().getScriptEngine().toNativeArray(aFieldNodeArrayImpl0);
        return nativeArray0;
    }

    /**
     * Get value on attribute fields
     * @return the attribute's value
     */
    public final Object getFields() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "fields", Optional.empty());
        	}
        	Object result = this.getFieldsImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "fields", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "fields", e);
        }
    }

    /**
     * Get value on attribute classDescriptor
     * @return the attribute's value
     */
    public abstract AClassType getClassDescriptorImpl();

    /**
     * Get value on attribute classDescriptor
     * @return the attribute's value
     */
    public final Object getClassDescriptor() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "classDescriptor", Optional.empty());
        	}
        	AClassType result = this.getClassDescriptorImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "classDescriptor", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "classDescriptor", e);
        }
    }

    /**
     * 
     */
    public void defClassDescriptorImpl(AClassType value) {
        throw new UnsupportedOperationException("Join point "+get_class()+": Action def classDescriptor with type AClassType not implemented ");
    }

    /**
     * Get value on attribute superClassDescriptor
     * @return the attribute's value
     */
    public abstract AClassType getSuperClassDescriptorImpl();

    /**
     * Get value on attribute superClassDescriptor
     * @return the attribute's value
     */
    public final Object getSuperClassDescriptor() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "superClassDescriptor", Optional.empty());
        	}
        	AClassType result = this.getSuperClassDescriptorImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "superClassDescriptor", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "superClassDescriptor", e);
        }
    }

    /**
     * 
     */
    public void defSuperClassDescriptorImpl(AClassType value) {
        throw new UnsupportedOperationException("Join point "+get_class()+": Action def superClassDescriptor with type AClassType not implemented ");
    }

    /**
     * 
     */
    @Override
    public final List<? extends JoinPoint> select(String selectName) {
        List<? extends JoinPoint> joinPointList;
        switch(selectName) {
        	default:
        		joinPointList = super.select(selectName);
        		break;
        }
        return joinPointList;
    }

    /**
     * 
     */
    @Override
    public final void defImpl(String attribute, Object value) {
        switch(attribute){
        case "classDescriptor": {
        	if(value instanceof AClassType){
        		this.defClassDescriptorImpl((AClassType)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        case "superClassDescriptor": {
        	if(value instanceof AClassType){
        		this.defSuperClassDescriptorImpl((AClassType)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        default: throw new UnsupportedOperationException("Join point "+get_class()+": attribute '"+attribute+"' cannot be defined");
        }
    }

    /**
     * 
     */
    @Override
    protected final void fillWithAttributes(List<String> attributes) {
        super.fillWithAttributes(attributes);
        attributes.add("methods");
        attributes.add("fields");
        attributes.add("classDescriptor");
        attributes.add("superClassDescriptor");
    }

    /**
     * 
     */
    @Override
    protected final void fillWithSelects(List<String> selects) {
        super.fillWithSelects(selects);
    }

    /**
     * 
     */
    @Override
    protected final void fillWithActions(List<String> actions) {
        super.fillWithActions(actions);
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "classNode";
    }
    /**
     * 
     */
    protected enum ClassNodeAttributes {
        METHODS("methods"),
        FIELDS("fields"),
        CLASSDESCRIPTOR("classDescriptor"),
        SUPERCLASSDESCRIPTOR("superClassDescriptor"),
        PARENT("parent"),
        GETDESCENDANTS("getDescendants"),
        GETDESCENDANTSANDSELF("getDescendantsAndSelf"),
        AST("ast"),
        CODE("code"),
        CHILDREN("children"),
        ROOT("root"),
        GETANCESTOR("getAncestor"),
        GETCHILD("getChild"),
        ID("id"),
        DESCENDANTS("descendants");
        private String name;

        /**
         * 
         */
        private ClassNodeAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<ClassNodeAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(ClassNodeAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
