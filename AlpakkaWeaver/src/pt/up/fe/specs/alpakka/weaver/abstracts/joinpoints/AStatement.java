package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;
import java.util.List;
import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Auto-Generated class for join point AStatement
 * This class is overwritten by the Weaver Generator.
 * 
 * Statement
 * @author Lara Weaver Generator
 */
public abstract class AStatement extends ASmaliWeaverJoinPoint {

    /**
     * Get value on attribute nextStatement
     * @return the attribute's value
     */
    public abstract AStatement getNextStatementImpl();

    /**
     * Get value on attribute nextStatement
     * @return the attribute's value
     */
    public final Object getNextStatement() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "nextStatement", Optional.empty());
        	}
        	AStatement result = this.getNextStatementImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "nextStatement", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "nextStatement", e);
        }
    }

    /**
     * 
     */
    public void defNextStatementImpl(AStatement value) {
        throw new UnsupportedOperationException("Join point "+get_class()+": Action def nextStatement with type AStatement not implemented ");
    }

    /**
     * Get value on attribute prevStatement
     * @return the attribute's value
     */
    public abstract AStatement getPrevStatementImpl();

    /**
     * Get value on attribute prevStatement
     * @return the attribute's value
     */
    public final Object getPrevStatement() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "prevStatement", Optional.empty());
        	}
        	AStatement result = this.getPrevStatementImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "prevStatement", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "prevStatement", e);
        }
    }

    /**
     * 
     */
    public void defPrevStatementImpl(AStatement value) {
        throw new UnsupportedOperationException("Join point "+get_class()+": Action def prevStatement with type AStatement not implemented ");
    }

    /**
     * Get value on attribute line
     * @return the attribute's value
     */
    public abstract ALineDirective getLineImpl();

    /**
     * Get value on attribute line
     * @return the attribute's value
     */
    public final Object getLine() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "line", Optional.empty());
        	}
        	ALineDirective result = this.getLineImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "line", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "line", e);
        }
    }

    /**
     * 
     */
    public void defLineImpl(ALineDirective value) {
        throw new UnsupportedOperationException("Join point "+get_class()+": Action def line with type ALineDirective not implemented ");
    }

    /**
     * 
     */
    @Override
    public List<? extends JoinPoint> select(String selectName) {
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
    public void defImpl(String attribute, Object value) {
        switch(attribute){
        case "nextStatement": {
        	if(value instanceof AStatement){
        		this.defNextStatementImpl((AStatement)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        case "prevStatement": {
        	if(value instanceof AStatement){
        		this.defPrevStatementImpl((AStatement)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
        case "line": {
        	if(value instanceof ALineDirective){
        		this.defLineImpl((ALineDirective)value);
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
    protected void fillWithAttributes(List<String> attributes) {
        super.fillWithAttributes(attributes);
        attributes.add("nextStatement");
        attributes.add("prevStatement");
        attributes.add("line");
    }

    /**
     * 
     */
    @Override
    protected void fillWithSelects(List<String> selects) {
        super.fillWithSelects(selects);
    }

    /**
     * 
     */
    @Override
    protected void fillWithActions(List<String> actions) {
        super.fillWithActions(actions);
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public String get_class() {
        return "statement";
    }
    /**
     * 
     */
    protected enum StatementAttributes {
        NEXTSTATEMENT("nextStatement"),
        PREVSTATEMENT("prevStatement"),
        LINE("line"),
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
        private StatementAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<StatementAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(StatementAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
