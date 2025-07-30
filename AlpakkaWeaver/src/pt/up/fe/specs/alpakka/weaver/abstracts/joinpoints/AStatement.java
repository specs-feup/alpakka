package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

/**
 * Auto-Generated class for join point AStatement
 * This class is overwritten by the Weaver Generator.
 * 
 * Statement
 * @author Lara Weaver Generator
 */
public abstract class AStatement extends ASmaliWeaverJoinPoint {

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
