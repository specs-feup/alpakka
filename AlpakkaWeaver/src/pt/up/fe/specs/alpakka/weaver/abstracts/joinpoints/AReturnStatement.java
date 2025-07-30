package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.JoinPoint;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

/**
 * Auto-Generated class for join point AReturnStatement
 * This class is overwritten by the Weaver Generator.
 * 
 * Smali instruction formats 10x, 11x
 * @author Lara Weaver Generator
 */
public abstract class AReturnStatement extends AInstruction {

    protected AInstruction aInstruction;

    /**
     * 
     */
    public AReturnStatement(AInstruction aInstruction){
        super(aInstruction);
        this.aInstruction = aInstruction;
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
     * Get value on attribute opCodeName
     * @return the attribute's value
     */
    @Override
    public String getOpCodeNameImpl() {
        return this.aInstruction.getOpCodeNameImpl();
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
     * Get value on attribute setsResult
     * @return the attribute's value
     */
    @Override
    public Boolean getSetsResultImpl() {
        return this.aInstruction.getSetsResultImpl();
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
     * Get value on attribute ast
     * @return the attribute's value
     */
    @Override
    public String getAstImpl() {
        return this.aInstruction.getAstImpl();
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
     * Get value on attribute code
     * @return the attribute's value
     */
    @Override
    public String getCodeImpl() {
        return this.aInstruction.getCodeImpl();
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
     * Get value on attribute id
     * @return the attribute's value
     */
    @Override
    public String getIdImpl() {
        return this.aInstruction.getIdImpl();
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
     * Get value on attribute root
     * @return the attribute's value
     */
    @Override
    public AProgram getRootImpl() {
        return this.aInstruction.getRootImpl();
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
     * 
     */
    @Override
    public Optional<? extends AInstruction> getSuper() {
        return Optional.of(this.aInstruction);
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "returnStatement";
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
    protected enum ReturnStatementAttributes {
        CANTHROW("canThrow"),
        OPCODENAME("opCodeName"),
        SETSREGISTER("setsRegister"),
        SETSRESULT("setsResult"),
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
        private ReturnStatementAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<ReturnStatementAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(ReturnStatementAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
