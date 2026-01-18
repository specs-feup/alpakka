package pt.up.fe.specs.alpakka.weaver.abstracts;

import java.util.stream.Stream;

import org.lara.interpreter.weaver.interf.JoinPoint;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AProgram;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.treenode.NodeInsertUtils;

/**
 * Abstract class which can be edited by the developer. This class will not be overwritten.
 * 
 * @author Lara Weaver Generator
 */
public abstract class ASmaliWeaverJoinPoint extends AJoinPoint {

    public ASmaliWeaverJoinPoint(SmaliWeaver weaver) {
        super(weaver);
    }

    @Override
    public SmaliWeaver getWeaverEngine() {
        return (SmaliWeaver) super.getWeaverEngine();
    }

    /**
     * Compares the two join points based on their node reference of the used compiler/parsing tool.<br>
     * This is the default implementation for comparing two join points. <br>
     * <b>Note for developers:</b> A weaver may override this implementation in the editable abstract join point, so the
     * changes are made for all join points, or override this method in specific join points.
     */
    @Override
    public boolean compareNodes(AJoinPoint aJoinPoint) {
        return this.getNode().equals(aJoinPoint.getNode());
    }

    @Override
    public String getIdImpl() {
        return getNode().get(SmaliNode.ID);
    }


    @Override
    public AProgram getRootImpl() {
        return (AProgram) getWeaverEngine().getRootJp();
    }

    @Override
    public AJoinPoint getParentImpl() {
        var node = getNode();
        if (!node.hasParent()) {
            return null;
        }

        var currentParent = node.getParent();

        return SmaliJoinpoints.create(currentParent, getWeaverEngine());
    }

    @Override
    public AJoinPoint getAncestorImpl(String type) {
        return null;
    }

    @Override
    public AJoinPoint[] getDescendantsArrayImpl() {
        return getNode().getDescendants().stream().map(node -> SmaliJoinpoints.create(node, getWeaverEngine())).toArray(AJoinPoint[]::new);
    }

    @Override
    public AJoinPoint[] getDescendantsArrayImpl(String type) {
        return getNode().getDescendantsStream().map(node -> SmaliJoinpoints.create(node, getWeaverEngine()))
                .filter(jp -> jp.instanceOf(type))
                .toArray(AJoinPoint[]::new);
    }

    @Override
    public AJoinPoint[] getDescendantsAndSelfArrayImpl(String type) {
        return getNode().getDescendantsAndSelfStream().map(node -> SmaliJoinpoints.create(node, getWeaverEngine())).toArray(AJoinPoint[]::new);

    }

    @Override
    public Stream<JoinPoint> getJpChildrenStream() {
        return getNode().getChildrenStream().map(node -> SmaliJoinpoints.create(node, getWeaverEngine()));
    }

    @Override
    public AJoinPoint[] getChildrenArrayImpl() {
    return getNode().getChildrenStream().map(node -> SmaliJoinpoints.create(node, getWeaverEngine())).toArray(AJoinPoint[]::new);
    }

    @Override
    public AJoinPoint getChildImpl(int index) {
        return getNode().getChildren().stream()
                .skip(index)
                .findFirst()
                .map(node -> SmaliJoinpoints.create(node, getWeaverEngine()))
                .orElse(null);
    }
    
    @Override
    public String getAstImpl() {
        return getNode().toTree();
    }

    @Override
    public String getCodeImpl() {
        return getNode().getCode();
    }

    @Override
    public AJoinPoint insertBeforeImpl(AJoinPoint node) {
        NodeInsertUtils.insertBefore(this.getNode(), node.getNode());

        return SmaliJoinpoints.create(node.getNode(), getWeaverEngine());
    }

    @Override
    public AJoinPoint insertBeforeImpl(String code) {
        return insertBeforeImpl(toJpToBeInserted(code));
    }

    @Override
    public AJoinPoint insertAfterImpl(AJoinPoint node) {
        NodeInsertUtils.insertAfter(this.getNode(), node.getNode());

        return SmaliJoinpoints.create(node.getNode(), getWeaverEngine());
    }

    @Override
    public AJoinPoint insertAfterImpl(String code) {
        return insertAfterImpl(toJpToBeInserted(code));
    }

    private AJoinPoint toJpToBeInserted(String code) {
        var context = getNode().getContext();

        return SmaliJoinpoints.create(context.getFactory().literalStmt(code), getWeaverEngine());
    }

    /**
     * 
     * @param position
     * @param code
     */
    @Override
    public AJoinPoint[] insertImpl(String position, String code) {
        throw new NotImplementedException("Insertion of code not implemented for " + this.getClass().getSimpleName());
    }

    /**
     * 
     * @param position
     * @param code
     */
    @Override
    public AJoinPoint[] insertImpl(String position, JoinPoint code) {
        // NodeInsertUtils.

        throw new NotImplementedException("Insertion of code not implemented for " + this.getClass().getSimpleName());
    }

}
