package pt.up.fe.specs.smali.weaver.abstracts;

import java.util.List;
import java.util.stream.Stream;

import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.interf.SelectOp;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.SmaliJoinpoints;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Abstract class which can be edited by the developer. This class will not be
 * overwritten.
 * 
 * @author Lara Weaver Generator
 */
public abstract class ASmaliWeaverJoinPoint extends AJoinPoint {

	/**
	 * Compares the two join points based on their node reference of the used
	 * compiler/parsing tool.<br>
	 * This is the default implementation for comparing two join points. <br>
	 * <b>Note for developers:</b> A weaver may override this implementation in the
	 * editable abstract join point, so the changes are made for all join points, or
	 * override this method in specific join points.
	 */
	@Override
	public boolean compareNodes(AJoinPoint aJoinPoint) {
		return this.getNode().equals(aJoinPoint.getNode());
	}

	/**
	 * Generic select function, used by the default select implementations.
	 */
	@Override
	public <T extends AJoinPoint> List<? extends T> select(Class<T> joinPointClass, SelectOp op) {
		throw new RuntimeException(
				"Generic select function not implemented yet. Implement it in order to use the default implementations of select");
	}

	@Override
	public String getIdImpl() {
		return getNode().get(SmaliNode.ID);
	}

	@Override
	public Stream<JoinPoint> getJpChildrenStream() {
		return getNode().getChildrenStream().map(node -> SmaliJoinpoints.create(node));
	}

	@Override
	public String getAstImpl() {
		return getNode().toTree();
	}

	@Override
	public String getCodeImpl() {
		return getNode().getCode();
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
