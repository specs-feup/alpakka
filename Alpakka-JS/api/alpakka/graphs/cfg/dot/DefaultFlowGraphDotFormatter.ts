import DotFormatter from "./DotFormatter.ts";
import type { Node, Edge } from "./DotFormatter.ts";
import * as ControlFlowEdge from "../flow/edge/ControlFlowEdge.ts";
import * as ConditionNode from "../flow/node/condition/ConditionNode.ts";
import * as UnknownInstructionNode from "../flow/node/instruction/UnknownInstructionNode.ts";
import * as BaseEdge from "../graph/BaseEdge.ts";
import * as BaseNode from "../graph/BaseNode.ts";
import * as ReturnNode from "../flow/node/instruction/ReturnNode.ts";
import * as StatementNode from "../flow/node/instruction/StatementNode.ts";
import * as InstructionNode from "../flow/node/instruction/InstructionNode.ts";
import * as FunctionEntryNode from "../flow/node/instruction/FunctionEntryNode.ts";
import * as FunctionExitNode from "../flow/node/instruction/FunctionExitNode.ts";
import * as LabelNode from "../flow/node/instruction/LabelNode.ts";
import * as GotoNode from "../flow/node/instruction/GotoNode.ts";
import * as SwitchNode from "../flow/node/instruction/SwitchNode.ts";
import * as ThrowNode from "../flow/node/instruction/ThrowNode.ts";
import * as CaseNode from "../flow/node/condition/CaseNode.ts";
import * as IfComparisonNode from "../flow/node/condition/IfComparisonNode.ts";
import * as TryCatchNode from "../flow/node/condition/TryCatchNode.ts";

export default class DefaultFlowGraphDotFormatter extends DotFormatter {
  override formatNode(node: BaseNode.Class): Node {
    let label;
    let shape = "box";

    if (node.is(CaseNode.TypeGuard)) {
      const caseNode = node.as(CaseNode.Class);
      shape = "diamond";
      label = `Case: \n${caseNode.jp.code}`;
    } else if (node.is(IfComparisonNode.TypeGuard)) {
      const ifNode = node.as(IfComparisonNode.Class);
      shape = "diamond";
      label = `Condition: \n${ifNode.jp.code}`;
    } else if (node.is(TryCatchNode.TypeGuard)) {
      const tryNode = node.as(TryCatchNode.Class);
      shape = "diamond";
      label = `Try: \n${tryNode.jp.code}`;
    } else if (node.is(FunctionEntryNode.TypeGuard)) {
      const functionEntryNode = node.as(FunctionEntryNode.Class);
      label = `Function Entry \n(${functionEntryNode.jp.name})`;
    } else if (node.is(FunctionExitNode.TypeGuard)) {
      const functionExitNode = node.as(FunctionExitNode.Class);
      label = `Function Exit \n(${functionExitNode.jp.name})`;
    } else if (node.is(StatementNode.TypeGuard)) {
      const stmtNode = node.as(StatementNode.Class);
      label = `Statement: \n${stmtNode.jp.code}`;
    } else if (node.is(SwitchNode.TypeGuard)) {
      const switchNode = node.as(SwitchNode.Class);
      label = `Switch Stmt: \n${switchNode.jp.code}`;
    } else if (node.is(ReturnNode.TypeGuard)) {
      const returnNode = node.as(ReturnNode.Class);
      label = `Return Stmt: \n${returnNode.jp.code}`;
    } else if (node.is(ThrowNode.TypeGuard)) {
      const throwNode = node.as(ThrowNode.Class);
      label = `Throw Stmt: \n${throwNode.jp.code}`;
    } else if (node.is(LabelNode.TypeGuard)) {
      const labelNode = node.as(LabelNode.Class);
      label = `Label: \n${labelNode.jp.name}`;
    } else if (node.is(GotoNode.TypeGuard)) {
      const gotoNode = node.as(GotoNode.Class);
      label = `Goto: \n${gotoNode.jp.label.decl.name}`;
    } else if (node.is(UnknownInstructionNode.TypeGuard)) {
      const unknownInstructionNode = node.as(UnknownInstructionNode.Class);
      if (unknownInstructionNode.jp !== undefined) {
        label = `Unknown Instruction: \n${unknownInstructionNode.jp.code}`;
      } else {
        label = `Unknown Instruction`;
      }
    } else {
      label = "Not flow node";
    }

    return {
      id: node.id,
      attrs: {
        label,
        shape,
      },
    };
  }

  override formatEdge(edge: BaseEdge.Class): Edge {
    let color = "black";
    if (edge.is(ControlFlowEdge.TypeGuard)) {
      if (edge.source.is(ConditionNode.TypeGuard)) {
        if (edge.id === edge.source.as(ConditionNode.Class).trueEdge.id) {
          color = "green";
        } else if (
          edge.id === edge.source.as(ConditionNode.Class).falseEdge.id
        ) {
          color = "red";
        } else {
          color = "blue";
        }
      } else if (edge.source.is(InstructionNode.TypeGuard)) {
        if (edge.id === edge.source.as(InstructionNode.Class).nextEdge?.id) {
          color = "green";
        } else {
          color = "blue";
        }
      } else {
        color = "blue";
      }
    }

    return {
      source: edge.source.id,
      target: edge.target.id,
      attrs: {
        color,
      },
    };
  }
}
