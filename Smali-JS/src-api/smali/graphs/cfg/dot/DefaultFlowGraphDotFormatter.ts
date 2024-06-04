import DotFormatter from "./DotFormatter.js";
import ControlFlowEdge from "../flow/edge/ControlFlowEdge.js";
import ConditionNode from "../flow/node/condition/ConditionNode.js";
import UnknownInstructionNode from "../flow/node/instruction/UnknownInstructionNode.js";
import BaseEdge from "../graph/BaseEdge.js";
import BaseNode from "../graph/BaseNode.js";
import ReturnNode from "../flow/node/instruction/ReturnNode.js";
import StatementNode from "../flow/node/instruction/StatementNode.js";
import InstructionNode from "../flow/node/instruction/InstructionNode.js";
import FunctionEntryNode from "../flow/node/instruction/FunctionEntryNode.js";
import FunctionExitNode from "../flow/node/instruction/FunctionExitNode.js";
import LabelNode from "../flow/node/instruction/LabelNode.js";
import GotoNode from "../flow/node/instruction/GotoNode.js";
import SwitchNode from "../flow/node/instruction/SwitchNode.js";
import ThrowNode from "../flow/node/instruction/ThrowNode.js";
import CaseNode from "../flow/node/condition/CaseNode.js";
import IfComparisonNode from "../flow/node/condition/IfComparisonNode.js";
import TryCatchNode from "../flow/node/condition/TryCatchNode.js";

export default class DefaultFlowGraphDotFormatter extends DotFormatter {
  override formatNode(node: BaseNode.Class): DotFormatter.Node {
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

  override formatEdge(edge: BaseEdge.Class): DotFormatter.Edge {
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
