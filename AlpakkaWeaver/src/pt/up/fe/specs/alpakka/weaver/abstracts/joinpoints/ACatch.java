package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

/**
 * Auto-Generated class for join point ACatch
 * This class is overwritten by the Weaver Generator.
 * 
 * Catch directive
 * @author Lara Weaver Generator
 */
public abstract class ACatch extends AStatement {

    protected AStatement aStatement;

    /**
     * 
     */
    public ACatch(AStatement aStatement){
        this.aStatement = aStatement;
    }
    /**
     * Get value on attribute _catch
     * @return the attribute's value
     */
    public abstract ALabelReference getCatchImpl();

    /**
     * Get value on attribute _catch
     * @return the attribute's value
     */
    public final Object getCatch() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "catch", Optional.empty());
        	}
        	ALabelReference result = this.getCatchImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "catch", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "catch", e);
        }
    }

    /**
     * Get value on attribute exception
     * @return the attribute's value
     */
    public abstract ATypeDescriptor getExceptionImpl();

    /**
     * Get value on attribute exception
     * @return the attribute's value
     */
    public final Object getException() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "exception", Optional.empty());
        	}
        	ATypeDescriptor result = this.getExceptionImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "exception", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "exception", e);
        }
    }

    /**
     * Get value on attribute tryEnd
     * @return the attribute's value
     */
    public abstract ALabelReference getTryEndImpl();

    /**
     * Get value on attribute tryEnd
     * @return the attribute's value
     */
    public final Object getTryEnd() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "tryEnd", Optional.empty());
        	}
        	ALabelReference result = this.getTryEndImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "tryEnd", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "tryEnd", e);
        }
    }

    /**
     * Get value on attribute tryStart
     * @return the attribute's value
     */
    public abstract ALabelReference getTryStartImpl();

    /**
     * Get value on attribute tryStart
     * @return the attribute's value
     */
    public final Object getTryStart() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "tryStart", Optional.empty());
        	}
        	ALabelReference result = this.getTryStartImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "tryStart", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "tryStart", e);
        }
    }

    /**
     * Get value on attribute line
     * @return the attribute's value
     */
    @Override
    public ALineDirective getLineImpl() {
        return this.aStatement.getLineImpl();
    }

    /**
     * Get value on attribute nextStatement
     * @return the attribute's value
     */
    @Override
    public AStatement getNextStatementImpl() {
        return this.aStatement.getNextStatementImpl();
    }

    /**
     * Get value on attribute prevStatement
     * @return the attribute's value
     */
    @Override
    public AStatement getPrevStatementImpl() {
        return this.aStatement.getPrevStatementImpl();
    }

    /**
     * Get value on attribute ast
     * @return the attribute's value
     */
    @Override
    public String getAstImpl() {
        return this.aStatement.getAstImpl();
    }

    /**
     * Get value on attribute childrenArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getChildrenArrayImpl() {
        return this.aStatement.getChildrenArrayImpl();
    }

    /**
     * Get value on attribute code
     * @return the attribute's value
     */
    @Override
    public String getCodeImpl() {
        return this.aStatement.getCodeImpl();
    }

    /**
     * Get value on attribute descendantsArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getDescendantsArrayImpl() {
        return this.aStatement.getDescendantsArrayImpl();
    }

    /**
     * Get value on attribute getAncestor
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getAncestorImpl(String type) {
        return this.aStatement.getAncestorImpl(type);
    }

    /**
     * Get value on attribute getChild
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getChildImpl(int index) {
        return this.aStatement.getChildImpl(index);
    }

    /**
     * Get value on attribute getDescendantsArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getDescendantsArrayImpl(String type) {
        return this.aStatement.getDescendantsArrayImpl(type);
    }

    /**
     * Get value on attribute getDescendantsAndSelfArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getDescendantsAndSelfArrayImpl(String type) {
        return this.aStatement.getDescendantsAndSelfArrayImpl(type);
    }

    /**
     * Get value on attribute id
     * @return the attribute's value
     */
    @Override
    public String getIdImpl() {
        return this.aStatement.getIdImpl();
    }

    /**
     * Get value on attribute parent
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getParentImpl() {
        return this.aStatement.getParentImpl();
    }

    /**
     * Get value on attribute root
     * @return the attribute's value
     */
    @Override
    public AProgram getRootImpl() {
        return this.aStatement.getRootImpl();
    }

    /**
     * Removes the node associated to this joinpoint from the AST
     */
    @Override
    public AJoinPoint detachImpl() {
        return this.aStatement.detachImpl();
    }

    /**
     * 
     * @param position 
     * @param code 
     */
    @Override
    public AJoinPoint[] insertImpl(String position, String code) {
        return this.aStatement.insertImpl(position, code);
    }

    /**
     * 
     * @param position 
     * @param code 
     */
    @Override
    public AJoinPoint[] insertImpl(String position, JoinPoint code) {
        return this.aStatement.insertImpl(position, code);
    }

    /**
     * Inserts the given join point after this join point
     * @param node 
     */
    @Override
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        return this.aStatement.insertAfterImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param code 
     */
    @Override
    public AJoinPoint insertAfterImpl(String code) {
        return this.aStatement.insertAfterImpl(code);
    }

    /**
     * Inserts the given join point before this join point
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        return this.aStatement.insertBeforeImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(String node) {
        return this.aStatement.insertBeforeImpl(node);
    }

    /**
     * Replaces this node with the given node
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint node) {
        return this.aStatement.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(String node) {
        return this.aStatement.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a list of join points
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint[] node) {
        return this.aStatement.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a list of strings
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithStringsImpl(String[] node) {
        return this.aStatement.replaceWithStringsImpl(node);
    }

    /**
     * 
     */
    @Override
    public Optional<? extends AStatement> getSuper() {
        return Optional.of(this.aStatement);
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "catch";
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
        return this.aStatement.instanceOf(joinpointClass);
    }
    /**
     * 
     */
    protected enum CatchAttributes {
        CATCH("catch"),
        EXCEPTION("exception"),
        TRYEND("tryEnd"),
        TRYSTART("tryStart"),
        LINE("line"),
        NEXTSTATEMENT("nextStatement"),
        PREVSTATEMENT("prevStatement"),
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
        private CatchAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<CatchAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(CatchAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
