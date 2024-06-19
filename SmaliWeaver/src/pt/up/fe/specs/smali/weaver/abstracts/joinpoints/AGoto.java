package pt.up.fe.specs.smali.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Auto-Generated class for join point AGoto
 * This class is overwritten by the Weaver Generator.
 * 
 * Smali instruction formats 10t, 20t, 30t
 * @author Lara Weaver Generator
 */
public abstract class AGoto extends AInstruction {

    protected AInstruction aInstruction;

    /**
     * 
     */
    public AGoto(AInstruction aInstruction){
        super(aInstruction);
        this.aInstruction = aInstruction;
    }
    /**
     * Get value on attribute label
     * @return the attribute's value
     */
    public abstract ALabelReference getLabelImpl();

    /**
     * Get value on attribute label
     * @return the attribute's value
     */
    public final Object getLabel() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "label", Optional.empty());
        	}
        	ALabelReference result = this.getLabelImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "label", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "label", e);
        }
    }

    /**
     * 
     */
    public void defLabelImpl(ALabelReference value) {
        throw new UnsupportedOperationException("Join point "+get_class()+": Action def label with type ALabelReference not implemented ");
    }

    /**
     * Get value on attribute canThrow
     * @return the attribute's value
     */
    @Override
    public Boolean getCanThrowImpl() {
        return this.aInstruction.getCanThrowImpl();
    }

    /**
     * Get value on attribute setsResult
     * @return the attribute's value
     */
    @Override
    public Boolean getSetsResultImpl() {
        return this.aInstruction.getSetsResultImpl();
    }

    /**
     * Get value on attribute setsRegister
     * @return the attribute's value
     */
    @Override
    public Boolean getSetsRegisterImpl() {
        return this.aInstruction.getSetsRegisterImpl();
    }

    /**
     * Get value on attribute opCodeName
     * @return the attribute's value
     */
    @Override
    public String getOpCodeNameImpl() {
        return this.aInstruction.getOpCodeNameImpl();
    }

    /**
     * Get value on attribute nextStatement
     * @return the attribute's value
     */
    @Override
    public AStatement getNextStatementImpl() {
        return this.aInstruction.getNextStatementImpl();
    }

    /**
     * Get value on attribute prevStatement
     * @return the attribute's value
     */
    @Override
    public AStatement getPrevStatementImpl() {
        return this.aInstruction.getPrevStatementImpl();
    }

    /**
     * Get value on attribute line
     * @return the attribute's value
     */
    @Override
    public ALineDirective getLineImpl() {
        return this.aInstruction.getLineImpl();
    }

    /**
     * 
     */
    public void defNextStatementImpl(AStatement value) {
        this.aInstruction.defNextStatementImpl(value);
    }

    /**
     * 
     */
    public void defPrevStatementImpl(AStatement value) {
        this.aInstruction.defPrevStatementImpl(value);
    }

    /**
     * 
     */
    public void defLineImpl(ALineDirective value) {
        this.aInstruction.defLineImpl(value);
    }

    /**
     * Get value on attribute parent
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getParentImpl() {
        return this.aInstruction.getParentImpl();
    }

    /**
     * Get value on attribute getDescendantsArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getDescendantsArrayImpl(String type) {
        return this.aInstruction.getDescendantsArrayImpl(type);
    }

    /**
     * Get value on attribute getDescendantsAndSelfArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getDescendantsAndSelfArrayImpl(String type) {
        return this.aInstruction.getDescendantsAndSelfArrayImpl(type);
    }

    /**
     * Get value on attribute ast
     * @return the attribute's value
     */
    @Override
    public String getAstImpl() {
        return this.aInstruction.getAstImpl();
    }

    /**
     * Get value on attribute code
     * @return the attribute's value
     */
    @Override
    public String getCodeImpl() {
        return this.aInstruction.getCodeImpl();
    }

    /**
     * Get value on attribute childrenArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getChildrenArrayImpl() {
        return this.aInstruction.getChildrenArrayImpl();
    }

    /**
     * Get value on attribute root
     * @return the attribute's value
     */
    @Override
    public AProgram getRootImpl() {
        return this.aInstruction.getRootImpl();
    }

    /**
     * Get value on attribute getAncestor
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getAncestorImpl(String type) {
        return this.aInstruction.getAncestorImpl(type);
    }

    /**
     * Get value on attribute getChild
     * @return the attribute's value
     */
    @Override
    public AJoinPoint getChildImpl(int index) {
        return this.aInstruction.getChildImpl(index);
    }

    /**
     * Get value on attribute id
     * @return the attribute's value
     */
    @Override
    public String getIdImpl() {
        return this.aInstruction.getIdImpl();
    }

    /**
     * Get value on attribute descendantsArrayImpl
     * @return the attribute's value
     */
    @Override
    public AJoinPoint[] getDescendantsArrayImpl() {
        return this.aInstruction.getDescendantsArrayImpl();
    }

    /**
     * Replaces this node with the given node
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint node) {
        return this.aInstruction.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(String node) {
        return this.aInstruction.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a list of join points
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithImpl(AJoinPoint[] node) {
        return this.aInstruction.replaceWithImpl(node);
    }

    /**
     * Overload which accepts a list of strings
     * @param node 
     */
    @Override
    public AJoinPoint replaceWithStringsImpl(String[] node) {
        return this.aInstruction.replaceWithStringsImpl(node);
    }

    /**
     * Inserts the given join point before this join point
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        return this.aInstruction.insertBeforeImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param node 
     */
    @Override
    public AJoinPoint insertBeforeImpl(String node) {
        return this.aInstruction.insertBeforeImpl(node);
    }

    /**
     * Inserts the given join point after this join point
     * @param node 
     */
    @Override
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        return this.aInstruction.insertAfterImpl(node);
    }

    /**
     * Overload which accepts a string
     * @param code 
     */
    @Override
    public AJoinPoint insertAfterImpl(String code) {
        return this.aInstruction.insertAfterImpl(code);
    }

    /**
     * Removes the node associated to this joinpoint from the AST
     */
    @Override
    public AJoinPoint detachImpl() {
        return this.aInstruction.detachImpl();
    }

    /**
     * 
     * @param position 
     * @param code 
     */
    @Override
    public AJoinPoint[] insertImpl(String position, String code) {
        return this.aInstruction.insertImpl(position, code);
    }

    /**
     * 
     * @param position 
     * @param code 
     */
    @Override
    public AJoinPoint[] insertImpl(String position, JoinPoint code) {
        return this.aInstruction.insertImpl(position, code);
    }

    /**
     * 
     */
    @Override
    public Optional<? extends AInstruction> getSuper() {
        return Optional.of(this.aInstruction);
    }

    /**
     * 
     */
    @Override
    public final List<? extends JoinPoint> select(String selectName) {
        List<? extends JoinPoint> joinPointList;
        switch(selectName) {
        	default:
        		joinPointList = this.aInstruction.select(selectName);
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
        case "label": {
        	if(value instanceof ALabelReference){
        		this.defLabelImpl((ALabelReference)value);
        		return;
        	}
        	this.unsupportedTypeForDef(attribute, value);
        }
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
    protected final void fillWithAttributes(List<String> attributes) {
        this.aInstruction.fillWithAttributes(attributes);
        attributes.add("label");
    }

    /**
     * 
     */
    @Override
    protected final void fillWithSelects(List<String> selects) {
        this.aInstruction.fillWithSelects(selects);
    }

    /**
     * 
     */
    @Override
    protected final void fillWithActions(List<String> actions) {
        this.aInstruction.fillWithActions(actions);
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "goto";
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
        return this.aInstruction.instanceOf(joinpointClass);
    }
    /**
     * 
     */
    protected enum GotoAttributes {
        LABEL("label"),
        CANTHROW("canThrow"),
        SETSRESULT("setsResult"),
        SETSREGISTER("setsRegister"),
        OPCODENAME("opCodeName"),
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
        private GotoAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<GotoAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(GotoAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
