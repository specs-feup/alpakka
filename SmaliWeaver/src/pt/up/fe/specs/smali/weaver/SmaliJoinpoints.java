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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.smali.weaver;

import pt.up.fe.specs.smali.ast.ClassNode;
import pt.up.fe.specs.smali.ast.MethodNode;
import pt.up.fe.specs.smali.ast.Placeholder;
import pt.up.fe.specs.smali.ast.RegisterReference;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.Instruction;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat10x;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21cField;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21cString;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat35cMethod;
import pt.up.fe.specs.smali.ast.stmt.LocalsNode;
import pt.up.fe.specs.smali.ast.stmt.RegistersNode;
import pt.up.fe.specs.smali.ast.type.ArrayType;
import pt.up.fe.specs.smali.ast.type.ClassType;
import pt.up.fe.specs.smali.ast.type.MethodPrototype;
import pt.up.fe.specs.smali.ast.type.PrimitiveType;
import pt.up.fe.specs.smali.ast.type.Type;
import pt.up.fe.specs.smali.weaver.abstracts.ASmaliWeaverJoinPoint;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AJoinPoint;
import pt.up.fe.specs.smali.weaver.joinpoints.ArrayTypeJp;
import pt.up.fe.specs.smali.weaver.joinpoints.ClassNodeJp;
import pt.up.fe.specs.smali.weaver.joinpoints.ClassTypeJp;
import pt.up.fe.specs.smali.weaver.joinpoints.InstructionFormat10xJp;
import pt.up.fe.specs.smali.weaver.joinpoints.InstructionFormat21cFieldJp;
import pt.up.fe.specs.smali.weaver.joinpoints.InstructionFormat21cStringJp;
import pt.up.fe.specs.smali.weaver.joinpoints.InstructionFormat35cMethodJp;
import pt.up.fe.specs.smali.weaver.joinpoints.InstructionJp;
import pt.up.fe.specs.smali.weaver.joinpoints.LocalsNodeJp;
import pt.up.fe.specs.smali.weaver.joinpoints.MethodNodeJp;
import pt.up.fe.specs.smali.weaver.joinpoints.MethodPrototypeJp;
import pt.up.fe.specs.smali.weaver.joinpoints.PlaceholderJp;
import pt.up.fe.specs.smali.weaver.joinpoints.PrimitiveTypeJp;
import pt.up.fe.specs.smali.weaver.joinpoints.RegisterReferenceJp;
import pt.up.fe.specs.smali.weaver.joinpoints.RegistersNodeJp;
import pt.up.fe.specs.smali.weaver.joinpoints.TypeJp;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

public class SmaliJoinpoints {

	private static final FunctionClassMap<SmaliNode, ASmaliWeaverJoinPoint> JOINPOINT_FACTORY;
	static {
		JOINPOINT_FACTORY = new FunctionClassMap<>();
		JOINPOINT_FACTORY.put(ClassNode.class, ClassNodeJp::new);
		JOINPOINT_FACTORY.put(MethodNode.class, MethodNodeJp::new);
		JOINPOINT_FACTORY.put(Type.class, TypeJp::new);
		JOINPOINT_FACTORY.put(ClassType.class, ClassTypeJp::new);
		JOINPOINT_FACTORY.put(ArrayType.class, ArrayTypeJp::new);
		JOINPOINT_FACTORY.put(MethodPrototype.class, MethodPrototypeJp::new);
		JOINPOINT_FACTORY.put(PrimitiveType.class, PrimitiveTypeJp::new);
		JOINPOINT_FACTORY.put(RegisterReference.class, RegisterReferenceJp::new);
		JOINPOINT_FACTORY.put(RegistersNode.class, RegistersNodeJp::new);
		JOINPOINT_FACTORY.put(LocalsNode.class, LocalsNodeJp::new);
		JOINPOINT_FACTORY.put(InstructionFormat10x.class, InstructionFormat10xJp::new);
		JOINPOINT_FACTORY.put(InstructionFormat21cField.class, InstructionFormat21cFieldJp::new);
		JOINPOINT_FACTORY.put(InstructionFormat21cString.class, InstructionFormat21cStringJp::new);
		JOINPOINT_FACTORY.put(InstructionFormat35cMethod.class, InstructionFormat35cMethodJp::new);
		JOINPOINT_FACTORY.put(Instruction.class, InstructionJp::new);
		JOINPOINT_FACTORY.put(Placeholder.class, PlaceholderJp::new);
	}

	public static ASmaliWeaverJoinPoint createFromLara(Object node) {
		if (!(node instanceof SmaliNode)) {
			throw new RuntimeException(
					"Expected input to be a ClavaNode, is " + node.getClass().getSimpleName() + ": " + node);
		}

		return create((SmaliNode) node);
	}

	public static ASmaliWeaverJoinPoint create(SmaliNode node) {
		if (node == null) {
			SpecsLogs.debug("CxxJoinpoints: tried to create join point from null node, returning undefined");
			return null;
		}

		return JOINPOINT_FACTORY.apply(node);
	}

	public static <T extends AJoinPoint> T create(SmaliNode node, Class<T> targetClass) {
		if (targetClass == null) {
			throw new RuntimeException("Check if you meant to call 'create' with a single argument");
		}

		return targetClass.cast(create(node));
	}

}
