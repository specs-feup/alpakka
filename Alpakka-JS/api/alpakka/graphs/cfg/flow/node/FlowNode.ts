import * as ControlFlowEdge from "../edge/ControlFlowEdge.ts";
import * as InstructionNode from "./instruction/InstructionNode.ts";
// import * as ConditionNode from "../node/condition/ConditionNode.ts";
import * as BaseEdge from "../../graph/BaseEdge.ts";
import * as BaseNode from "../../graph/BaseNode.ts";
import type { NodeBuilder, NodeTypeGuard } from "../../graph/Node.ts";
import { Joinpoint } from "../../../../../Joinpoints.ts";

export class Class<
  D extends Data = Data,
  S extends ScratchData = ScratchData,
> extends BaseNode.Class<D, S> {
  insertBefore(node: InstructionNode.Class) {
    this.insertSubgraphBefore(node, [node]);
  }

  insertSubgraphBefore(head: BaseNode.Class, tail: InstructionNode.Class[]) {
    this.incomers.forEach((edge) => {
      edge.target = head;
    });

    for (const tailNode of tail) {
      tailNode.nextNode = this;
    }
  }

  removeFromFlow() {
    // TODO improve ergonomics with Collection class
    const incomers = this.incomers.filter((edge) =>
      edge.is(ControlFlowEdge.TypeGuard),
    );

    const outgoers = this.outgoers.filter((edge) =>
      edge.is(ControlFlowEdge.TypeGuard),
    );

    if (incomers.length === 0 || outgoers.length === 0) {
      for (const edge of incomers) {
        edge.remove();
      }
      for (const edge of outgoers) {
        edge.remove();
      }
    } else if (outgoers.length === 1) {
      for (const edge of incomers) {
        edge.target = outgoers[0].target;
      }

      outgoers[0].remove();
    } else {
      throw new Error(
        "Cannot remove node with at least one incomer and multiple outgoers.",
      );
    }
  }

  get reachableNodes(): Class[] {
    const result: Class[] = [];
    for (const [node] of this.bfs((e) => e.is(ControlFlowEdge.TypeGuard))) {
      if (node.is(TypeGuard)) {
        result.push(node.as(Class));
      }
    }
    return result;
  }

  get previousEdges(): ControlFlowEdge.Class[] {
    return this.incomers
      .filter((edge) => edge.is(ControlFlowEdge.TypeGuard))
      .map(
        (edge) =>
          edge as BaseEdge.Class<
            ControlFlowEdge.Data,
            ControlFlowEdge.ScratchData
          >,
      )
      .map((edge) => edge.as(ControlFlowEdge.Class));
  }

  get previousNodes(): Class[] {
    return this.previousEdges
      .map((edge) => edge.source)
      .filter((node) => node.is(TypeGuard))
      .map(
        (node) => node as BaseNode.Class<Data, ScratchData>,
      )
      .map((node) => node.as(Class));
  }

  get nextEdges(): ControlFlowEdge.Class[] {
    return this.outgoers
      .filter((edge) => edge.is(ControlFlowEdge.TypeGuard))
      .map(
        (edge) =>
          edge as BaseEdge.Class<
            ControlFlowEdge.Data,
            ControlFlowEdge.ScratchData
          >,
      )
      .map((edge) => edge.as(ControlFlowEdge.Class));
  }

  get nextNodes(): Class[] {
    return this.nextEdges
      .map((edge) => edge.target)
      .filter((node) => node.is(TypeGuard))
      .map(
        (node) => node as BaseNode.Class<Data, ScratchData>,
      )
      .map((node) => node.as(Class));
  }

  get jp(): Joinpoint | undefined {
    return this.scratchData.$jp;
  }
}

export abstract class Builder
  extends BaseNode.Builder
  implements NodeBuilder<Data, ScratchData>
{
  #$jp: Joinpoint | undefined;
  #flowNodeType: Type;

  constructor(type: Type, $jp: Joinpoint | undefined) {
    super();
    this.#$jp = $jp;
    this.#flowNodeType = type;
  }

  override buildData(data: BaseNode.Data): Data {
    return {
      ...super.buildData(data),
      flowNodeType: this.#flowNodeType,
    };
  }

  override buildScratchData(scratchData: BaseNode.ScratchData): ScratchData {
    return {
      ...super.buildScratchData(scratchData),
      $jp: this.#$jp,
    };
  }
}

export const TypeGuard: NodeTypeGuard<Data, ScratchData> = {
  isDataCompatible(data: BaseNode.Data): data is Data {
    if (!BaseNode.TypeGuard.isDataCompatible(data)) return false;
    const d = data as Data;
    if (!Object.values(Type).includes(d.flowNodeType as Type)) return false;
    return true;
  },

  isScratchDataCompatible(
    scratchData: BaseNode.ScratchData,
  ): scratchData is ScratchData {
    if (!BaseNode.TypeGuard.isScratchDataCompatible(scratchData))
      return false;
    const s = scratchData as ScratchData;
    if (s.$jp !== undefined && !(s.$jp instanceof Joinpoint)) return false;
    return true;
  },
};

export interface Data extends BaseNode.Data {
  flowNodeType: Type;
}

export interface ScratchData extends BaseNode.ScratchData {
  $jp: Joinpoint | undefined;
}

// ------------------------------------------------------------

export const Type = {
  INSTRUCTION: "instruction",
  CONDITION: "condition"
} as const;

export type Type = (typeof Type)[keyof typeof Type];
