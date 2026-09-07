/**
 * Copyright 2016 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.alpakka.weaver.joinpoints;

import java.util.stream.Stream;

import org.lara.interpreter.weaver.interf.enums.InsertPosition;

import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.weaver.SmaliJoinpoints;
import pt.up.fe.specs.alpakka.weaver.SmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AJoinpoint;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AProgram;
import pt.up.fe.specs.util.treenode.NodeInsertUtils;

/**
 * Editable class which contains the implementation shared by all join points.
 * This class will NOT be overwritten by the generator.
 */
public class SmaliJoinpoint<Self extends SmaliJoinpoint<Self>> extends AJoinpoint<Self> {

    public SmaliJoinpoint(SmaliNode node, SmaliWeaver weaver) {
        super(node, weaver);
    }

    @Override
    public SmaliWeaver getWeaverEngine() {
        return (SmaliWeaver) super.getWeaverEngine();
    }

    @Override
    public SmaliNode getNodeImpl() {
        return (SmaliNode) super.getNodeImpl();
    }

    @Override
    public boolean getSameImpl(AJoinpoint<?> other) {
        return this.get_class().equals(other.get_class()) && this.getNodeImpl().equals(other.getNodeImpl());
    }

    @Override
    public boolean getCompareNodesImpl(AJoinpoint<?> aJoinPoint) {
        return this.getNodeImpl().equals(aJoinPoint.getNodeImpl());
    }

    @Override
    public boolean getEqualsImpl(Self jp) {
        if (!(jp instanceof AJoinpoint)) {
            return false;
        }

        return this.getSameImpl(jp);
    }

    @Override
    public AProgram<?> getRootImpl() {
        return SmaliJoinpoints.create(getWeaverEngine().getRootNode(), getWeaverEngine(), AProgram.class);
    }

    @Override
    public String getIdImpl() {
        return getNodeImpl().get(SmaliNode.ID);
    }

    @Override
    public String getAstImpl() {
        return getNodeImpl().toTree();
    }

    @Override
    public String getCodeImpl() {
        return getNodeImpl().getCode();
    }

    @Override
    public Integer getLineImpl() {
        // The Smali AST has no source location information
        return null;
    }

    @Override
    public Integer getColumnImpl() {
        // The Smali AST has no source location information
        return null;
    }

    @Override
    public AJoinpoint<?> getParentImpl() {
        var node = getNodeImpl();
        if (!node.hasParent()) {
            return null;
        }

        var currentParent = node.getParent();

        return SmaliJoinpoints.create(currentParent, getWeaverEngine());
    }

    @Override
    public AJoinpoint<?> getJpParent() {
        return getParentImpl();
    }

    @Override
    public AJoinpoint<?> getGetAncestorImpl(String type) {
        return null;
    }

    @Override
    public AJoinpoint<?>[] getChildrenImpl() {
        return getNodeImpl().getChildrenStream()
                .map(node -> SmaliJoinpoints.create(node, getWeaverEngine()))
                .toArray(AJoinpoint<?>[]::new);
    }

    @Override
    public AJoinpoint<?>[] getDescendantsImpl() {
        return getNodeImpl().getDescendantsStream()
                .map(node -> SmaliJoinpoints.create(node, getWeaverEngine()))
                .toArray(AJoinpoint<?>[]::new);
    }

    @Override
    public AJoinpoint<?>[] getGetDescendantsImpl(String type) {
        return getNodeImpl().getDescendantsStream()
                .map(node -> SmaliJoinpoints.create(node, getWeaverEngine()))
                .filter(jp -> jp.instanceOf(type))
                .toArray(AJoinpoint<?>[]::new);
    }

    @Override
    public AJoinpoint<?>[] getGetDescendantsAndSelfImpl(String type) {
        return getNodeImpl().getDescendantsAndSelfStream()
                .map(node -> SmaliJoinpoints.create(node, getWeaverEngine()))
                .toArray(AJoinpoint<?>[]::new);
    }

    @Override
    public AJoinpoint<?>[] getScopeNodesImpl() {
        return getChildrenImpl();
    }

    @Override
    public AJoinpoint<?> getGetChildImpl(int index) {
        return getNodeImpl().getChildren().stream()
                .skip(index)
                .findFirst()
                .map(node -> SmaliJoinpoints.create(node, getWeaverEngine()))
                .orElse(null);
    }

    @Override
    public Stream<AJoinpoint<?>> getJpChildrenStream() {
        return getNodeImpl().getChildrenStream()
                .map(node -> (AJoinpoint<?>) SmaliJoinpoints.create(node, getWeaverEngine()));
    }

    @Override
    public AJoinpoint<?>[] insertImpl(InsertPosition position, String code) {
        return new AJoinpoint<?>[] { switch (position) {
            case BEFORE -> insertBeforeImpl(code);
            case AFTER -> insertAfterImpl(code);
            case REPLACE -> throw new UnsupportedOperationException(
                    get_class() + ": Action insert with position '" + position + "' not implemented");
        } };
    }

    @Override
    public AJoinpoint<?>[] insertImpl(InsertPosition position, AJoinpoint<?> joinpoint) {
        return new AJoinpoint<?>[] { switch (position) {
            case BEFORE -> insertBeforeImpl(joinpoint);
            case AFTER -> insertAfterImpl(joinpoint);
            case REPLACE -> throw new UnsupportedOperationException(
                    get_class() + ": Action insert with position '" + position + "' not implemented");
        } };
    }

    @Override
    public AJoinpoint<?> insertBeforeImpl(AJoinpoint<?> node) {
        NodeInsertUtils.insertBefore(this.getNodeImpl(), node.getNodeImpl());

        return SmaliJoinpoints.create(node.getNodeImpl(), getWeaverEngine());
    }

    @Override
    public AJoinpoint<?> insertBeforeImpl(String code) {
        return insertBeforeImpl(toJpToBeInserted(code));
    }

    @Override
    public AJoinpoint<?> insertAfterImpl(AJoinpoint<?> node) {
        NodeInsertUtils.insertAfter(this.getNodeImpl(), node.getNodeImpl());

        return SmaliJoinpoints.create(node.getNodeImpl(), getWeaverEngine());
    }

    @Override
    public AJoinpoint<?> insertAfterImpl(String code) {
        return insertAfterImpl(toJpToBeInserted(code));
    }

    private AJoinpoint<?> toJpToBeInserted(String code) {
        var context = getNodeImpl().getContext();

        return SmaliJoinpoints.create(context.getFactory().literalStmt(code), getWeaverEngine());
    }

    // Actions that are not supported by this weaver, same behavior as the
    // default implementations of the old generator

    @Override
    public AJoinpoint<?> detachImpl() {
        throw new UnsupportedOperationException(get_class() + ": Action detach not implemented ");
    }

    @Override
    public AJoinpoint<?> replaceWithImpl(AJoinpoint<?> node) {
        throw new UnsupportedOperationException(get_class() + ": Action replaceWith not implemented ");
    }

    @Override
    public AJoinpoint<?> replaceWithImpl(String node) {
        throw new UnsupportedOperationException(get_class() + ": Action replaceWith not implemented ");
    }

    @Override
    public AJoinpoint<?> replaceWithImpl(AJoinpoint<?>[] node) {
        throw new UnsupportedOperationException(get_class() + ": Action replaceWith not implemented ");
    }

    @Override
    public AJoinpoint<?> replaceWithStringsImpl(String[] node) {
        throw new UnsupportedOperationException(get_class() + ": Action replaceWithStrings not implemented ");
    }
}
