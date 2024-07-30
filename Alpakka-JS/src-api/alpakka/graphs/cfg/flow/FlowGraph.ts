import FlowGraphGenerator from "./FlowGraphGenerator.js";
import FlowNode from "./node/FlowNode.js";
import BaseGraph from "../graph/BaseGraph.js";
import Graph, { GraphBuilder, GraphTypeGuard } from "../graph/Graph.js";
import {
  IfComparison,
  IfComparisonWithZero,
  Instruction,
  LabelReference,
  MethodNode,
  Program,
  Statement,
} from "../../../../Joinpoints.js";
import InstructionNode from "./node/instruction/InstructionNode.js";
import FunctionEntryNode from "./node/instruction/FunctionEntryNode.js";
import FunctionExitNode from "./node/instruction/FunctionExitNode.js";
import ControlFlowEdge from "./edge/ControlFlowEdge.js";
import IfComparisonNode from "./node/condition/IfComparisonNode.js";
import TryCatchNode from "./node/condition/TryCatchNode.js";
import CaseNode from "./node/condition/CaseNode.js";

namespace FlowGraph {
  export class Class<
    D extends Data = Data,
    S extends ScratchData = ScratchData,
  > extends BaseGraph.Class<D, S> {
    addFunction(
      $jp: MethodNode,
      bodyHead: FlowNode.Class | undefined,
      bodyTail: InstructionNode.Class[],
      // params: VarDeclarationNode.Class[] = [],
    ): [FunctionEntryNode.Class, FunctionExitNode.Class?] {
      const function_entry = this.addNode()
        .init(new FunctionEntryNode.Builder($jp))
        .as(FunctionEntryNode.Class);
      this.data.functions.set($jp.referenceName, function_entry.id);

      const function_exit = this.addNode()
        .init(new FunctionExitNode.Builder($jp))
        .as(FunctionExitNode.Class);
      function_exit.insertBefore(function_entry);

      //   for (const param of params) {
      //     function_exit.insertBefore(param);
      // }

      if (bodyHead !== undefined) {
        function_exit.insertSubgraphBefore(bodyHead, bodyTail);
      }

      if (bodyTail.length === 0) {
        function_exit.removeFromFlow();
        function_exit.remove();
        return [function_entry];
      }

      return [function_entry, function_exit];
    }

    addCondition(
      $jp: IfComparison | IfComparisonWithZero,
      iftrue: FlowNode.Class,
      iffalse: FlowNode.Class,
    ): IfComparisonNode.Class {
      const ifnode = this.addNode();
      const iftrueEdge = this.addEdge(ifnode, iftrue).init(
        new ControlFlowEdge.Builder(),
      );
      const iffalseEdge = this.addEdge(ifnode, iffalse).init(
        new ControlFlowEdge.Builder(),
      );
      return ifnode
        .init(new IfComparisonNode.Builder(iftrueEdge, iffalseEdge, $jp))
        .as(IfComparisonNode.Class);
    }

    addSwitchCase(
      $jp: LabelReference,
      iftrue: FlowNode.Class,
      iffalse: FlowNode.Class,
    ): CaseNode.Class {
      const caseNode = this.addNode();
      const iftrueEdge = this.addEdge(caseNode, iftrue).init(
        new ControlFlowEdge.Builder(),
      );
      const iffalseEdge = this.addEdge(caseNode, iffalse).init(
        new ControlFlowEdge.Builder(),
      );
      return caseNode
        .init(new CaseNode.Builder(iftrueEdge, iffalseEdge, $jp))
        .as(CaseNode.Class);
    }

    addTryCatch(
      $jp: Instruction,
      iftrue: FlowNode.Class,
      iffalse: FlowNode.Class,
    ): TryCatchNode.Class {
      const tryNode = this.addNode();
      const iftrueEdge = this.addEdge(tryNode, iftrue).init(
        new ControlFlowEdge.Builder(),
      );
      const iffalseEdge = this.addEdge(tryNode, iffalse).init(
        new ControlFlowEdge.Builder(),
      );
      return tryNode
        .init(new TryCatchNode.Builder(iftrueEdge, iffalseEdge, $jp))
        .as(TryCatchNode.Class);
    }

    getFunction(name: string): FunctionEntryNode.Class | undefined {
      const id = this.data.functions.get(name);
      if (id === undefined) return undefined;
      const node = this.getNodeById(id);
      if (node === undefined || !node.is(FunctionEntryNode.TypeGuard)) {
        return undefined;
      }
      return node.as(FunctionEntryNode.Class);
    }

    get functions(): FunctionEntryNode.Class[] {
      const nodes: FunctionEntryNode.Class[] = [];
      for (const id of this.data.functions.values()) {
        const node = this.getNodeById(id);
        if (node === undefined || !node.is(FunctionEntryNode.TypeGuard)) {
          continue;
        }
        nodes.push(node.as(FunctionEntryNode.Class));
      }
      return nodes;
    }

    // /**
    //  * Returns the graph node where the given statement belongs.
    //  *
    //  * @param $stmt - A statement join point, or a string with the id of the join point
    //  */
    getNode($stmt: Statement | string): FlowNode.Class | undefined {
      // If string, assume it is id
      const id: string = typeof $stmt === "string" ? $stmt : $stmt.id;

      const node = this.getNodeById(id);
      if (node === undefined || !node.is(FlowNode.TypeGuard)) {
        return undefined;
      }
      return node.as(FlowNode.Class);
    }
  }

  export class Builder
    extends BaseGraph.Builder
    implements GraphBuilder<Data, ScratchData>
  {
    override buildData(data: BaseGraph.Data): Data {
      return {
        ...super.buildData(data),
        functions: new Map(),
      };
    }

    override buildScratchData(scratchData: BaseGraph.ScratchData): ScratchData {
      return {
        ...super.buildScratchData(scratchData),
      };
    }
  }

  export const TypeGuard: GraphTypeGuard<Data, ScratchData> = {
    isDataCompatible(data: BaseGraph.Data): data is Data {
      if (!BaseGraph.TypeGuard.isDataCompatible(data)) return false;
      return true;
    },

    isScratchDataCompatible(
      sData: BaseGraph.ScratchData,
    ): sData is ScratchData {
      if (!BaseGraph.TypeGuard.isScratchDataCompatible(sData)) return false;
      return true;
    },
  };

  export interface Data extends BaseGraph.Data {
    // Maps function name to its entry node id
    functions: Map<string, string>;
  }

  export interface ScratchData extends BaseGraph.Data {}

  // ---------------------

  export function generate(
    $jp: Program | MethodNode,
    graph?: BaseGraph.Class,
  ): FlowGraph.Class {
    const flowGraph = (graph ?? Graph.create())
      .init(new FlowGraph.Builder())
      .as(FlowGraph.Class);
    return new FlowGraphGenerator($jp, flowGraph).build();
  }
}

export default FlowGraph;
