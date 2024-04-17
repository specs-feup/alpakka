///////////////////////////////////////////////////
// This file is generated by build-interfaces.js //
///////////////////////////////////////////////////

/* eslint-disable @typescript-eslint/ban-types */
/* eslint-disable @typescript-eslint/no-explicit-any */
/* eslint-disable @typescript-eslint/no-unsafe-assignment */
/* eslint-disable @typescript-eslint/no-unsafe-call */
/* eslint-disable @typescript-eslint/no-unsafe-member-access */
/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable @typescript-eslint/no-duplicate-type-constituents */

import {
  LaraJoinPoint,
  type JoinpointMapperType,
  registerJoinpointMapper,
  wrapJoinPoint,
  unwrapJoinPoint,
} from "lara-js/api/LaraJoinPoint.js";

export class Joinpoint extends LaraJoinPoint {
  /**
   * String representation of the ast
   */
  get ast(): string { return wrapJoinPoint(this._javaObject.getAst()) }
  /**
   * Returns an array with the children of the node, ignoring null nodes
   */
  get children(): Joinpoint[] { return wrapJoinPoint(this._javaObject.getChildren()) }
  /**
   * String with the code represented by this node
   */
  get code(): string { return wrapJoinPoint(this._javaObject.getCode()) }
  /**
   * Retrieves all descendants of the join point
   */
  get descendants(): Joinpoint[] { return wrapJoinPoint(this._javaObject.getDescendants()) }
  /**
   * The id of the node
   */
  get id(): string { return wrapJoinPoint(this._javaObject.getId()) }
  /**
   * Returns the parent node in the AST, or undefined if it is the root node
   */
  get parent(): Joinpoint { return wrapJoinPoint(this._javaObject.getParent()) }
  /**
   * Returns the 'program' joinpoint
   */
  get root(): Program { return wrapJoinPoint(this._javaObject.getRoot()) }
  /**
   * Looks for an ancestor joinpoint name, walking back on the AST
   */
  getAncestor(type: string): Joinpoint { return wrapJoinPoint(this._javaObject.getAncestor(unwrapJoinPoint(type))); }
  /**
   * Retrieves the descendants of the given type
   */
  getDescendants(type: string): Joinpoint[] { return wrapJoinPoint(this._javaObject.getDescendants(unwrapJoinPoint(type))); }
  /**
   * Retrieves the descendants of the given type, including the node itself
   */
  getDescendantsAndSelf(type: string): Joinpoint[] { return wrapJoinPoint(this._javaObject.getDescendantsAndSelf(unwrapJoinPoint(type))); }
  /**
   * Removes the node associated to this joinpoint from the AST
   */
  detach(): Joinpoint { return wrapJoinPoint(this._javaObject.detach()); }
  /**
   * Inserts the given join point after this join point
   */
  insertAfter(node: Joinpoint): Joinpoint;
  /**
   * Overload which accepts a string
   */
  insertAfter(code: string): Joinpoint;
  /**
   * Inserts the given join point after this join point
   */
  insertAfter(p1: Joinpoint | string): Joinpoint | Joinpoint { return wrapJoinPoint(this._javaObject.insertAfter(unwrapJoinPoint(p1))); }
  /**
   * Inserts the given join point before this join point
   */
  insertBefore(node: Joinpoint): Joinpoint;
  /**
   * Overload which accepts a string
   */
  insertBefore(node: string): Joinpoint;
  /**
   * Inserts the given join point before this join point
   */
  insertBefore(p1: Joinpoint | string): Joinpoint | Joinpoint { return wrapJoinPoint(this._javaObject.insertBefore(unwrapJoinPoint(p1))); }
  /**
   * Replaces this node with the given node
   */
  replaceWith(node: Joinpoint): Joinpoint;
  /**
   * Overload which accepts a string
   */
  replaceWith(node: string): Joinpoint;
  /**
   * Overload which accepts a list of join points
   */
  replaceWith(node: Joinpoint[]): Joinpoint;
  /**
   * Replaces this node with the given node
   */
  replaceWith(p1: Joinpoint | string | Joinpoint[]): Joinpoint | Joinpoint | Joinpoint { return wrapJoinPoint(this._javaObject.replaceWith(unwrapJoinPoint(p1))); }
  /**
   * Overload which accepts a list of strings
   */
  replaceWithStrings(node: string[]): Joinpoint { return wrapJoinPoint(this._javaObject.replaceWithStrings(unwrapJoinPoint(node))); }
}

  /**
   * Class definition
   */
export class ClassNode extends Joinpoint {
  get attributes(): string[] { return wrapJoinPoint(this._javaObject.getAttributes()) }
  get selects(): string[] { return wrapJoinPoint(this._javaObject.getSelects()) }
  get actions(): string[] { return wrapJoinPoint(this._javaObject.getActions()) }
  def(attribute: string, value: object): void { return wrapJoinPoint(this._javaObject.def(unwrapJoinPoint(attribute), unwrapJoinPoint(value))); }
}

  /**
   * Expression
   */
export class Expression extends Joinpoint {
  get attributes(): string[] { return wrapJoinPoint(this._javaObject.getAttributes()) }
  get selects(): string[] { return wrapJoinPoint(this._javaObject.getSelects()) }
  get actions(): string[] { return wrapJoinPoint(this._javaObject.getActions()) }
  def(attribute: string, value: object): void { return wrapJoinPoint(this._javaObject.def(unwrapJoinPoint(attribute), unwrapJoinPoint(value))); }
}

  /**
   * Field definition
   */
export class FieldNode extends Joinpoint {
  get attributes(): string[] { return wrapJoinPoint(this._javaObject.getAttributes()) }
  get selects(): string[] { return wrapJoinPoint(this._javaObject.getSelects()) }
  get actions(): string[] { return wrapJoinPoint(this._javaObject.getActions()) }
  def(attribute: string, value: object): void { return wrapJoinPoint(this._javaObject.def(unwrapJoinPoint(attribute), unwrapJoinPoint(value))); }
}

  /**
   * Literal
   */
export class Literal extends Expression {
}

  /**
   * Method definition
   */
export class MethodNode extends Joinpoint {
  get attributes(): string[] { return wrapJoinPoint(this._javaObject.getAttributes()) }
  get selects(): string[] { return wrapJoinPoint(this._javaObject.getSelects()) }
  get actions(): string[] { return wrapJoinPoint(this._javaObject.getActions()) }
  def(attribute: string, value: object): void { return wrapJoinPoint(this._javaObject.def(unwrapJoinPoint(attribute), unwrapJoinPoint(value))); }
}

  /**
   * Method prototype
   */
export class MethodPrototype extends Literal {
}

  /**
   * Placeholder node
   */
export class Placeholder extends Joinpoint {
  get kind(): string { return wrapJoinPoint(this._javaObject.getKind()) }
  get attributes(): string[] { return wrapJoinPoint(this._javaObject.getAttributes()) }
  get selects(): string[] { return wrapJoinPoint(this._javaObject.getSelects()) }
  get actions(): string[] { return wrapJoinPoint(this._javaObject.getActions()) }
  def(attribute: string, value: object): void { return wrapJoinPoint(this._javaObject.def(unwrapJoinPoint(attribute), unwrapJoinPoint(value))); }
}

  /**
   * App node
   */
export class Program extends Joinpoint {
  get attributes(): string[] { return wrapJoinPoint(this._javaObject.getAttributes()) }
  get selects(): string[] { return wrapJoinPoint(this._javaObject.getSelects()) }
  get actions(): string[] { return wrapJoinPoint(this._javaObject.getActions()) }
  def(attribute: string, value: object): void { return wrapJoinPoint(this._javaObject.def(unwrapJoinPoint(attribute), unwrapJoinPoint(value))); }
}

  /**
   * Register reference
   */
export class RegisterReference extends Expression {
}

  /**
   * Statement
   */
export class Statement extends Joinpoint {
  get attributes(): string[] { return wrapJoinPoint(this._javaObject.getAttributes()) }
  get selects(): string[] { return wrapJoinPoint(this._javaObject.getSelects()) }
  get actions(): string[] { return wrapJoinPoint(this._javaObject.getActions()) }
  def(attribute: string, value: object): void { return wrapJoinPoint(this._javaObject.def(unwrapJoinPoint(attribute), unwrapJoinPoint(value))); }
}

  /**
   * Type descriptor
   */
export class TypeDescriptor extends Literal {
}

  /**
   * Array descriptor
   */
export class ArrayType extends TypeDescriptor {
}

  /**
   * Class descriptor
   */
export class ClassType extends TypeDescriptor {
  get className(): string { return wrapJoinPoint(this._javaObject.getClassName()) }
  get packageName(): string { return wrapJoinPoint(this._javaObject.getPackageName()) }
}

  /**
   * Instruction
   */
export class Instruction extends Statement {
}

  /**
   * Instruction format 10x
   */
export class InstructionFormat10x extends Instruction {
}

  /**
   * Instruction format 21c field
   */
export class InstructionFormat21cField extends Instruction {
}

  /**
   * Instruction format 21c string
   */
export class InstructionFormat21cString extends Instruction {
}

  /**
   * Instruction format 35c method
   */
export class InstructionFormat35cMethod extends Instruction {
}

  /**
   * Primitive descriptor
   */
export class PrimitiveType extends TypeDescriptor {
}

const JoinpointMapper: JoinpointMapperType = {
  joinpoint: Joinpoint,
  classNode: ClassNode,
  expression: Expression,
  fieldNode: FieldNode,
  literal: Literal,
  methodNode: MethodNode,
  methodPrototype: MethodPrototype,
  placeholder: Placeholder,
  program: Program,
  registerReference: RegisterReference,
  statement: Statement,
  typeDescriptor: TypeDescriptor,
  arrayType: ArrayType,
  classType: ClassType,
  instruction: Instruction,
  instructionFormat10x: InstructionFormat10x,
  instructionFormat21cField: InstructionFormat21cField,
  instructionFormat21cString: InstructionFormat21cString,
  instructionFormat35cMethod: InstructionFormat35cMethod,
  primitiveType: PrimitiveType,
};

let registered = false;
if (!registered) {
  registerJoinpointMapper(JoinpointMapper);
  registered = true;
}
