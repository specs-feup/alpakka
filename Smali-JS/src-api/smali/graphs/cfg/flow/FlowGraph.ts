import FlowGraphGenerator from "./FlowGraphGenerator.js";
import FlowNode from "./node/FlowNode.js";
import BaseGraph from "../graph/BaseGraph.js";
import Graph, { GraphBuilder, GraphTypeGuard } from "../graph/Graph.js";
import { MethodNode, Program } from "../../../../Joinpoints.js";
import FunctionNode from "./node/instruction/FunctionNode.js";

namespace FlowGraph {
  export class Class<
    D extends Data = Data,
    S extends ScratchData = ScratchData,
  > extends BaseGraph.Class<D, S> {
    // addFunction(
    //     $jp: FunctionJp,
    //     bodyHead: FlowNode.Class,
    //     bodyTail: InstructionNode.Class[],
    //     params: VarDeclarationNode.Class[] = [],
    // ): [FunctionEntryNode.Class, FunctionExitNode.Class?] {
    //     const function_entry = this.addNode()
    //         .init(new FunctionEntryNode.Builder($jp))
    //         .as(FunctionEntryNode.Class);
    //     this.data.functions.set($jp.name, function_entry.id);

    //     const function_exit = this.addNode()
    //         .init(new FunctionExitNode.Builder($jp))
    //         .as(FunctionExitNode.Class);
    //     function_exit.insertBefore(function_entry);

    //     for (const param of params) {
    //         function_exit.insertBefore(param);
    //     }

    //     function_exit.insertSubgraphBefore(bodyHead, bodyTail);

    //     if (bodyTail.length === 0) {
    //         function_exit.removeFromFlow();
    //         function_exit.remove();
    //         return [function_entry];
    //     }

    //     return [function_entry, function_exit];
    // }

    addMethod(
      $jp: MethodNode,
      body: FlowNode.Class,
      // params: VarDeclarationNode.Class[] = [],
    ): FunctionNode.Class {
      const method = this.addNode()
        .init(new FunctionNode.Builder($jp))
        .as(FunctionNode.Class);
      this.data.functions.set($jp.name, method.id);

      method.nextNode = body;

      return method;
    }

    // addScope($jp: Scope, subGraphs: [FlowNode.Class, InstructionNode.Class[]][]): [ScopeStartNode.Class, ScopeEndNode.Class?] {
    //     const scope_start = this.addNode()
    //         .init(new ScopeStartNode.Builder($jp))
    //         .as(ScopeStartNode.Class);

    //     let current_tail: InstructionNode.Class[] = [scope_start];

    //     for (const [head, tail] of subGraphs) {
    //         for (const tailNode of current_tail) {
    //             tailNode.nextNode = head;
    //         }
    //         current_tail = tail;
    //     }

    //     if (current_tail.length === 0) {
    //         return [scope_start];
    //     }

    //     const scope_end = this.addNode()
    //         .init(new ScopeEndNode.Builder($jp))
    //         .as(ScopeEndNode.Class);

    //     for (const tailNode of current_tail) {
    //         tailNode.nextNode = scope_end;
    //     }

    //     return [scope_start, scope_end];
    // }

    // addCondition(
    //     $jp: If | Loop | Case | undefined,
    //     iftrue: FlowNode.Class,
    //     iffalse: FlowNode.Class,
    // ): ConditionNode.Class {
    //     const ifnode = this.addNode();
    //     const iftrueEdge = this.addEdge(ifnode, iftrue).init(
    //         new ControlFlowEdge.Builder(),
    //     );
    //     const iffalseEdge = this.addEdge(ifnode, iffalse).init(
    //         new ControlFlowEdge.Builder(),
    //     );
    //     return ifnode
    //         .init(new ConditionNode.Builder(iftrueEdge, iffalseEdge, $jp))
    //         .as(ConditionNode.Class);
    // }

    // addLoop(
    //     $jp: Loop,
    //     bodyHead: FlowNode.Class,
    //     bodyTail: InstructionNode.Class[],
    //     afterLoop: FlowNode.Class,
    // ): ConditionNode.Class {
    //     const loopNode = this.addCondition($jp, bodyHead, afterLoop);
    //     for (const tailNode of bodyTail) {
    //         tailNode.nextNode = loopNode;
    //     }

    //     return loopNode;
    // }

    // getFunction(name: string): FunctionEntryNode.Class | undefined {
    //     const id = this.data.functions.get(name);
    //     if (id === undefined) return undefined;
    //     const node = this.getNodeById(id);
    //     if (node === undefined || !node.is(FunctionEntryNode.TypeGuard)) {
    //         return undefined;
    //     }
    //     return node.as(FunctionEntryNode.Class);
    // }

    // get functions(): FunctionEntryNode.Class[] {
    //     const nodes: FunctionEntryNode.Class[] = [];
    //     for (const id of this.data.functions.values()) {
    //         const node = this.getNodeById(id);
    //         if (node === undefined || !node.is(FunctionEntryNode.TypeGuard)) {
    //             continue;
    //         }
    //         nodes.push(node.as(FunctionEntryNode.Class));
    //     }
    //     return nodes;
    // }

    // /**
    //  * Returns the graph node where the given statement belongs.
    //  *
    //  * @param $stmt - A statement join point, or a string with the astId of the join point
    //  */
    // getNode($stmt: Statement | string) {
    //     // If string, assume it is astId
    //     const astId: string = typeof $stmt === "string" ? $stmt : $stmt.astId;

    //     return this.#nodes.get(astId);
    // }
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
