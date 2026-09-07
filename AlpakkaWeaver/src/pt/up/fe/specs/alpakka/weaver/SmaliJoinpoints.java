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

package pt.up.fe.specs.alpakka.weaver;

import pt.up.fe.specs.alpakka.ast.App;
import pt.up.fe.specs.alpakka.ast.ClassNode;
import pt.up.fe.specs.alpakka.ast.FieldNode;
import pt.up.fe.specs.alpakka.ast.Manifest;
import pt.up.fe.specs.alpakka.ast.MethodNode;
import pt.up.fe.specs.alpakka.ast.Placeholder;
import pt.up.fe.specs.alpakka.ast.Resource;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.expr.Expression;
import pt.up.fe.specs.alpakka.ast.expr.FieldReference;
import pt.up.fe.specs.alpakka.ast.expr.LabelRef;
import pt.up.fe.specs.alpakka.ast.expr.MethodReference;
import pt.up.fe.specs.alpakka.ast.expr.RegisterList;
import pt.up.fe.specs.alpakka.ast.expr.RegisterRange;
import pt.up.fe.specs.alpakka.ast.expr.RegisterReference;
import pt.up.fe.specs.alpakka.ast.expr.SparseSwitchElement;
import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;
import pt.up.fe.specs.alpakka.ast.expr.literal.PrimitiveLiteral;
import pt.up.fe.specs.alpakka.ast.stmt.CatchDirective;
import pt.up.fe.specs.alpakka.ast.stmt.Label;
import pt.up.fe.specs.alpakka.ast.stmt.LineDirective;
import pt.up.fe.specs.alpakka.ast.stmt.PackedSwitchDirective;
import pt.up.fe.specs.alpakka.ast.stmt.RegistersDirective;
import pt.up.fe.specs.alpakka.ast.stmt.SparseSwitchDirective;
import pt.up.fe.specs.alpakka.ast.stmt.Statement;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.BinaryOp;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.GotoStatement;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.Instruction;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.InstructionFormat21t;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.InstructionFormat22t;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.ReturnStatement;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.SwitchStatement;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.ThrowStatement;
import pt.up.fe.specs.alpakka.ast.type.ArrayType;
import pt.up.fe.specs.alpakka.ast.type.ClassType;
import pt.up.fe.specs.alpakka.ast.type.MethodPrototype;
import pt.up.fe.specs.alpakka.ast.type.PrimitiveType;
import pt.up.fe.specs.alpakka.ast.type.Type;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AJoinpoint;
import pt.up.fe.specs.alpakka.weaver.joinpoints.*;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.classmap.BiFunctionClassMap;
import pt.up.fe.specs.util.classmap.ClassMap;

public class SmaliJoinpoints {

    private static final BiFunctionClassMap<SmaliNode, SmaliWeaver, AJoinpoint<?>> JOINPOINT_FACTORY;
    private static final ClassMap<SmaliNode, String> JOINPOINT_NAMES;

    static {
        JOINPOINT_FACTORY = new BiFunctionClassMap<>();
        JOINPOINT_NAMES = new ClassMap<>();
        JOINPOINT_FACTORY.put(App.class, SmaliProgram::new);
        JOINPOINT_NAMES.put(App.class, "program");
        JOINPOINT_FACTORY.put(Manifest.class, SmaliManifest::new);
        JOINPOINT_NAMES.put(Manifest.class, "manifest");
        JOINPOINT_FACTORY.put(ClassNode.class, SmaliClassNode::new);
        JOINPOINT_NAMES.put(ClassNode.class, "classNode");
        JOINPOINT_FACTORY.put(FieldNode.class, SmaliFieldNode::new);
        JOINPOINT_NAMES.put(FieldNode.class, "fieldNode");
        JOINPOINT_FACTORY.put(MethodNode.class, SmaliMethodNode::new);
        JOINPOINT_NAMES.put(MethodNode.class, "methodNode");
        JOINPOINT_FACTORY.put(ClassType.class, SmaliClassType::new);
        JOINPOINT_NAMES.put(ClassType.class, "classType");
        JOINPOINT_FACTORY.put(ArrayType.class, SmaliArrayType::new);
        JOINPOINT_NAMES.put(ArrayType.class, "arrayType");
        JOINPOINT_FACTORY.put(PrimitiveType.class, SmaliPrimitiveType::new);
        JOINPOINT_NAMES.put(PrimitiveType.class, "primitiveType");
        JOINPOINT_FACTORY.put(Type.class, SmaliTypeDescriptor::new);
        JOINPOINT_NAMES.put(Type.class, "typeDescriptor");
        JOINPOINT_FACTORY.put(MethodPrototype.class, SmaliMethodPrototype::new);
        JOINPOINT_NAMES.put(MethodPrototype.class, "methodPrototype");
        JOINPOINT_FACTORY.put(PrimitiveLiteral.class, SmaliPrimitiveLiteral::new);
        JOINPOINT_NAMES.put(PrimitiveLiteral.class, "primitiveLiteral");
        JOINPOINT_FACTORY.put(Literal.class, SmaliLiteral::new);
        JOINPOINT_NAMES.put(Literal.class, "literal");
        JOINPOINT_FACTORY.put(RegisterReference.class, SmaliRegisterReference::new);
        JOINPOINT_NAMES.put(RegisterReference.class, "registerReference");
        JOINPOINT_FACTORY.put(MethodReference.class, SmaliMethodReference::new);
        JOINPOINT_NAMES.put(MethodReference.class, "methodReference");
        JOINPOINT_FACTORY.put(FieldReference.class, SmaliFieldReference::new);
        JOINPOINT_NAMES.put(FieldReference.class, "fieldReference");
        JOINPOINT_FACTORY.put(LabelRef.class, SmaliLabelReference::new);
        JOINPOINT_NAMES.put(LabelRef.class, "labelReference");
        JOINPOINT_FACTORY.put(SparseSwitchElement.class, SmaliSparseSwitchElement::new);
        JOINPOINT_NAMES.put(SparseSwitchElement.class, "sparseSwitchElement");
        JOINPOINT_FACTORY.put(RegisterRange.class, SmaliRegisterRange::new);
        JOINPOINT_NAMES.put(RegisterRange.class, "registerRange");
        JOINPOINT_FACTORY.put(RegisterList.class, SmaliRegisterList::new);
        JOINPOINT_NAMES.put(RegisterList.class, "registerList");
        JOINPOINT_FACTORY.put(Expression.class, SmaliExpression::new);
        JOINPOINT_NAMES.put(Expression.class, "expression");
        JOINPOINT_FACTORY.put(PackedSwitchDirective.class, SmaliPackedSwitch::new);
        JOINPOINT_NAMES.put(PackedSwitchDirective.class, "packedSwitch");
        JOINPOINT_FACTORY.put(SparseSwitchDirective.class, SmaliSparseSwitch::new);
        JOINPOINT_NAMES.put(SparseSwitchDirective.class, "sparseSwitch");
        JOINPOINT_FACTORY.put(ReturnStatement.class, SmaliReturnStatement::new);
        JOINPOINT_NAMES.put(ReturnStatement.class, "returnStatement");
        JOINPOINT_FACTORY.put(ThrowStatement.class, SmaliThrowStatement::new);
        JOINPOINT_NAMES.put(ThrowStatement.class, "throwStatement");
        JOINPOINT_FACTORY.put(SwitchStatement.class, SmaliSwitch::new);
        JOINPOINT_NAMES.put(SwitchStatement.class, "switch");
        JOINPOINT_FACTORY.put(GotoStatement.class, SmaliGoto::new);
        JOINPOINT_NAMES.put(GotoStatement.class, "goto");
        JOINPOINT_FACTORY.put(InstructionFormat21t.class, SmaliIfComparisonWithZero::new);
        JOINPOINT_NAMES.put(InstructionFormat21t.class, "ifComparisonWithZero");
        JOINPOINT_FACTORY.put(InstructionFormat22t.class, SmaliIfComparison::new);
        JOINPOINT_NAMES.put(InstructionFormat22t.class, "ifComparison");
        JOINPOINT_FACTORY.put(BinaryOp.class, SmaliBinaryOp::new);
        JOINPOINT_NAMES.put(BinaryOp.class, "binaryOp");
        JOINPOINT_FACTORY.put(Instruction.class, SmaliInstruction::new);
        JOINPOINT_NAMES.put(Instruction.class, "instruction");
        JOINPOINT_FACTORY.put(CatchDirective.class, SmaliCatch::new);
        JOINPOINT_NAMES.put(CatchDirective.class, "catch");
        JOINPOINT_FACTORY.put(RegistersDirective.class, SmaliRegistersDirective::new);
        JOINPOINT_NAMES.put(RegistersDirective.class, "registersDirective");
        JOINPOINT_FACTORY.put(Label.class, SmaliLabel::new);
        JOINPOINT_NAMES.put(Label.class, "label");
        JOINPOINT_FACTORY.put(LineDirective.class, SmaliLineDirective::new);
        JOINPOINT_NAMES.put(LineDirective.class, "lineDirective");
        JOINPOINT_FACTORY.put(Statement.class, SmaliStatement::new);
        JOINPOINT_NAMES.put(Statement.class, "statement");
        JOINPOINT_FACTORY.put(Resource.class, SmaliResourceNode::new);
        JOINPOINT_NAMES.put(Resource.class, "resourceNode");
        JOINPOINT_FACTORY.put(Placeholder.class, SmaliPlaceholder::new);
        JOINPOINT_NAMES.put(Placeholder.class, "placeholder");
    }

    /**
     * Maps a node to the corresponding join point name in the weaver specification.
     */
    public static String getJoinPointName(SmaliNode node) {
        return JOINPOINT_NAMES.get(node);
    }

    public static AJoinpoint<?> createFromLara(Object node, SmaliWeaver weaver) {
        if (!(node instanceof SmaliNode)) {
            throw new RuntimeException(
                    "Expected input to be a SmaliNode, is " + node.getClass().getSimpleName() + ": " + node);
        }

        return create((SmaliNode) node, weaver);
    }

    public static AJoinpoint<?> create(SmaliNode node, SmaliWeaver weaver) {
        if (node == null) {
            SpecsLogs.debug("SmaliJoinpoints: tried to create join point from null node, returning undefined");
            return null;
        }

        return JOINPOINT_FACTORY.apply(node, weaver);
    }

    public static <T extends AJoinpoint<?>> T create(SmaliNode node, SmaliWeaver weaver, Class<T> targetClass) {
        if (targetClass == null) {
            throw new RuntimeException("Check if you meant to call 'create' with a single argument");
        }

        return targetClass.cast(create(node, weaver));
    }

}
