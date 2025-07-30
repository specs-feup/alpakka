package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

/**
 * Auto-Generated class for join point AMethodPrototype
 * This class is overwritten by the Weaver Generator.
 * 
 * Method prototype
 * @author Lara Weaver Generator
 */
public abstract class AMethodPrototype extends ALiteral {

    protected ALiteral aLiteral;

    /**
     * 
     */
    public AMethodPrototype(ALiteral aLiteral){
        super(aLiteral);
        this.aLiteral = aLiteral;
    }
    /**
     * Get value on attribute parameters
     * @return the attribute's value
     */
    public abstract ATypeDescriptor[] getParametersArrayImpl();

    /**
     * Get value on attribute parameters
     * @return the attribute's value
     */
    public Object getParametersImpl() {
        ATypeDescriptor[] aTypeDescriptorArrayImpl0 = getParametersArrayImpl();
        Object nativeArray0 = aTypeDescriptorArrayImpl0;
        return nativeArray0;
    }

    /**
     * Get value on attribute parameters
     * @return the attribute's value
     */
    public final Object getParameters() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "parameters", Optional.empty());
        	}
        	Object result = this.getParametersImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "parameters", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "parameters", e);
        }
    }

    /**
     * Get value on attribute returnType
     * @return the attribute's value
     */
    public abstract ATypeDescriptor getReturnTypeImpl();

    /**
     * Get value on attribute returnType
     * @return the attribute's value
     */
    public final Object getReturnType() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "returnType", Optional.empty());
        	}
        	ATypeDescriptor result = this.getReturnTypeImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "returnType", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "returnType", e);
        }
    }

    /**
     * Get value on attribute ast
     * @return the attribute's value
     */
    @Override
    public String getAstImpl() {
        return this.aLiteral.getAstImpl();
    }

    /**
     * Get value on attribute childrenArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getChildrenArrayImpl() {
        return this.aLiteral.getChildrenArrayImpl();
    }

    /**
     * Get value on attribute code
     * @return the attribute's value
     */
    @Override
    public String getCodeImpl() {
        return this.aLiteral.getCodeImpl();
    }

    /**
     * Get value on attribute descendantsArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getDescendantsArrayImpl() {
        return this.aLiteral.getDescendantsArrayImpl();
    }

    /**
     * Get value on attribute getAncestor
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getAncestorImpl(String type) {
        return this.aLiteral.getAncestorImpl(type);
    }

    /**
     * Get value on attribute getChild
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getChildImpl(int index) {
        return this.aLiteral.getChildImpl(index);
    }

    /**
     * Get value on attribute getDescendantsArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getDescendantsArrayImpl(String type) {
        return this.aLiteral.getDescendantsArrayImpl(type);
    }

    /**
     * Get value on attribute getDescendantsAndSelfArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getDescendantsAndSelfArrayImpl(String type) {
        return this.aLiteral.getDescendantsAndSelfArrayImpl(type);
    }

    /**
     * Get value on attribute id
     * @return the attribute's value
     */
    @Override
    public String getIdImpl() {
        return this.aLiteral.getIdImpl();
    }

    /**
     * Get value on attribute parent
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getParentImpl() {
        return this.aLiteral.getParentImpl();
    }

    /**
     * Get value on attribute root
     * @return the attribute's value
     */
    @Override
    public AProgram getRootImpl() {
        return this.aLiteral.getRootImpl();
    }

    /**
     * Removes the node associated to this joinpoint from the AST
     */
    @Override
    public AJoinPoint detachImpl() {
        return this.aLiteral.detachImpl();
    }

    /**
     * 
     * @param position 
     * @param code 
     */
    @Override
    public AJoinPoint[] insertImpl(String position, String code) {
        return this.aLiteral.insertImpl(position, code);
    }

    /**
     * 
     * @param position 
     * @param code 
     */
    @Override
    public AJoinPoint[] insertImpl(String position, JoinPoint code) {
        return this.aLiteral.insertImpl(position, code);
    }

    /**
     * Inserts the given join point after this join point
     * @param node 
     */
    @Override
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        return this.aLiteral.insertAfterImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param code 
     */
    @Override
    public AJoinPoint insertAfterImpl(String code) {
        return this.aLiteral.insertAfterImpl(code);
    }

    /**
     * Inserts the given join point before this join point
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        return this.aLiteral.insertBeforeImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(String node) {
        return this.aLiteral.insertBeforeImpl(node);
    }

    /**
     * Replaces this node with the given node
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint node) {
        return this.aLiteral.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(String node) {
        return this.aLiteral.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a list of join points
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint[] node) {
        return this.aLiteral.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a list of strings
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithStringsImpl(String[] node) {
        return this.aLiteral.replaceWithStringsImpl(node);
    }

    /**
     * 
     */
    @Override
    public Optional<? extends ALiteral> getSuper() {
        return Optional.of(this.aLiteral);
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "methodPrototype";
    }

    /**
     * Defines if this joinpoint is an instanceof a given joinpoint class
     * @return True if this join point is an instanceof the given class
     */
    @Override
    public final boolean instanceOf(String joinpointClass) {
        boolean isInstance = get_class().equals(joinpointClass);
        if(isInstance) {
        	return true;
        }
        return this.aLiteral.instanceOf(joinpointClass);
    }
    /**
     * 
     */
    protected enum MethodPrototypeAttributes {
        PARAMETERS("parameters"),
        RETURNTYPE("returnType"),
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
        private MethodPrototypeAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<MethodPrototypeAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(MethodPrototypeAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
