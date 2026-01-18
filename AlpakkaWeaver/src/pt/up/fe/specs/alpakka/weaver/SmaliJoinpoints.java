/**
 * Copyright 2016 SPeCS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.alpakka.weaver;

import pt.up.fe.specs.alpakka.ast.*;
import pt.up.fe.specs.alpakka.ast.expr.*;
import pt.up.fe.specs.alpakka.ast.stmt.*;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.*;
import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;
import pt.up.fe.specs.alpakka.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.alpakka.ast.expr.literal.PrimitiveLiteral;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.ArrayType;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.ClassType;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.PrimitiveType;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;
import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.alpakka.weaver.joinpoints.*;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.classmap.BiFunctionClassMap;

public class SmaliJoinpoints {

    private static final BiFunctionClassMap<SmaliNode, SmaliWeaver, ASmaliWeaverJoinPoint> JOINPOINT_FACTORY;
    static {
        JOINPOINT_FACTORY = new BiFunctionClassMap<>();
        JOINPOINT_FACTORY.put(App.class, ProgramJp::new);
        JOINPOINT_FACTORY.put(Manifest.class, ManifestJp::new);
        JOINPOINT_FACTORY.put(ClassNode.class, ClassNodeJp::new);
        JOINPOINT_FACTORY.put(FieldNode.class, FieldNodeJp::new);
        JOINPOINT_FACTORY.put(MethodNode.class, MethodNodeJp::new);
        JOINPOINT_FACTORY.put(ClassType.class, ClassTypeJp::new);
        JOINPOINT_FACTORY.put(ArrayType.class, ArrayTypeJp::new);
        JOINPOINT_FACTORY.put(PrimitiveType.class, PrimitiveTypeJp::new);
        JOINPOINT_FACTORY.put(TypeDescriptor.class, TypeDescriptorJp::new);
        JOINPOINT_FACTORY.put(MethodPrototype.class, MethodPrototypeJp::new);
        JOINPOINT_FACTORY.put(PrimitiveLiteral.class, PrimitiveLiteralJp::new);
        JOINPOINT_FACTORY.put(Literal.class, LiteralJp::new);
        JOINPOINT_FACTORY.put(RegisterReference.class, RegisterReferenceJp::new);
        JOINPOINT_FACTORY.put(MethodReference.class, MethodReferenceJp::new);
        JOINPOINT_FACTORY.put(FieldReference.class, FieldReferenceJp::new);
        JOINPOINT_FACTORY.put(LabelRef.class, LabelReferenceJp::new);
        JOINPOINT_FACTORY.put(SparseSwitchElement.class, SparseSwitchElementJp::new);
        JOINPOINT_FACTORY.put(RegisterRange.class, RegisterRangeJp::new);
        JOINPOINT_FACTORY.put(RegisterList.class, RegisterListJp::new);
        JOINPOINT_FACTORY.put(Expression.class, ExpressionJp::new);
        JOINPOINT_FACTORY.put(PackedSwitchDirective.class, PackedSwitchJp::new);
        JOINPOINT_FACTORY.put(SparseSwitchDirective.class, SparseSwitchJp::new);
        JOINPOINT_FACTORY.put(ReturnStatement.class, ReturnStatementJp::new);
        JOINPOINT_FACTORY.put(ThrowStatement.class, ThrowStatementJp::new);
        JOINPOINT_FACTORY.put(SwitchStatement.class, SwitchJp::new);
        JOINPOINT_FACTORY.put(GotoStatement.class, GotoJp::new);
        JOINPOINT_FACTORY.put(InstructionFormat21t.class, IfComparisonWithZeroJp::new);
        JOINPOINT_FACTORY.put(InstructionFormat22t.class, IfComparisonJp::new);
        JOINPOINT_FACTORY.put(Instruction.class, InstructionJp::new);
        JOINPOINT_FACTORY.put(CatchDirective.class, CatchJp::new);
        JOINPOINT_FACTORY.put(RegistersDirective.class, RegistersDirectiveJp::new);
        JOINPOINT_FACTORY.put(Label.class, LabelJp::new);
        JOINPOINT_FACTORY.put(LineDirective.class, LineDirectiveJp::new);
        JOINPOINT_FACTORY.put(Statement.class, StatementJp::new);
        JOINPOINT_FACTORY.put(Resource.class, ResourceNodeJp::new);
        JOINPOINT_FACTORY.put(Placeholder.class, PlaceholderJp::new);
    }

    public static ASmaliWeaverJoinPoint createFromLara(Object node, SmaliWeaver weaver) {
        if (!(node instanceof SmaliNode)) {
            throw new RuntimeException(
                    "Expected input to be a ClavaNode, is " + node.getClass().getSimpleName() + ": " + node);
        }

        return create((SmaliNode) node, weaver);
    }

    public static ASmaliWeaverJoinPoint create(SmaliNode node, SmaliWeaver weaver) {
        if (node == null) {
            SpecsLogs.debug("CxxJoinpoints: tried to create join point from null node, returning undefined");
            return null;
        }

        return JOINPOINT_FACTORY.apply(node, weaver);
    }

    public static <T extends AJoinPoint> T create(SmaliNode node, SmaliWeaver weaver, Class<T> targetClass) {
        if (targetClass == null) {
            throw new RuntimeException("Check if you meant to call 'create' with a single argument");
        }

        return targetClass.cast(create(node, weaver));
    }

}
