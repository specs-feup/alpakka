import ControlFlowEdge from "../../../flow/edge/ControlFlowEdge.js";
import FlowNode from "../../../flow/node/FlowNode.js";
import BaseEdge from "../../../graph/BaseEdge.js";
import BaseNode from "../../../graph/BaseNode.js";
import { NodeBuilder, NodeTypeGuard } from "../../../graph/Node.js";
import {
  IfComparison,
  IfComparisonWithZero,
  Switch,
} from "../../../../../../Joinpoints.js";

namespace ConditionNode {
  export class Class<
    D extends Data = Data,
    S extends ScratchData = ScratchData,
  > extends FlowNode.Class<D, S> {
    get trueEdge(): ControlFlowEdge.Class {
      // Data and scratchdata should be ControlFlowEdge
      const edge = this.graph.getEdgeById(
        this.data.trueEdgeId,
      )! as BaseEdge.Class<ControlFlowEdge.Data, ControlFlowEdge.ScratchData>;
      return edge.as(ControlFlowEdge.Class);
    }

    get trueNode(): FlowNode.Class {
      const node = this.trueEdge.target as BaseNode.Class<
        FlowNode.Data,
        FlowNode.ScratchData
      >;
      return node.as(FlowNode.Class);
    }

    set trueNode(node: FlowNode.Class) {
      this.trueEdge.target = node;
    }

    get falseEdge(): ControlFlowEdge.Class {
      // Data and scratchdata should be ControlFlowEdge
      const edge = this.graph.getEdgeById(
        this.data.falseEdgeId,
      )! as BaseEdge.Class<ControlFlowEdge.Data, ControlFlowEdge.ScratchData>;
      return edge.as(ControlFlowEdge.Class);
    }

    get falseNode(): FlowNode.Class {
      const node = this.falseEdge.target as BaseNode.Class<
        FlowNode.Data,
        FlowNode.ScratchData
      >;
      return node.as(FlowNode.Class);
    }

    set falseNode(node: FlowNode.Class) {
      this.falseEdge.target = node;
    }

    override get jp():
      | IfComparison
      | IfComparisonWithZero
      | Switch
      | undefined {
      return this.scratchData.$jp;
    }
  }

  export class Builder
    extends FlowNode.Builder
    implements NodeBuilder<Data, ScratchData>
  {
    #truePath: ControlFlowEdge.Class;
    #falsePath: ControlFlowEdge.Class;

    constructor(
      truePath: ControlFlowEdge.Class,
      falsePath: ControlFlowEdge.Class,
      $jp?: IfComparison | IfComparisonWithZero | Switch,
    ) {
      super(FlowNode.Type.CONDITION, $jp);
      this.#truePath = truePath;
      this.#falsePath = falsePath;
    }

    buildData(data: BaseNode.Data): Data {
      return {
        ...(super.buildData(data) as FlowNode.Data & {
          flowNodeType: FlowNode.Type.CONDITION;
        }),
        trueEdgeId: this.#truePath.id,
        falseEdgeId: this.#falsePath.id,
      };
    }

    buildScratchData(scratchData: BaseNode.ScratchData): ScratchData {
      return {
        ...(super.buildScratchData(scratchData) as FlowNode.ScratchData & {
          $jp: IfComparison | IfComparisonWithZero | Switch | undefined;
        }),
      };
    }
  }

  export const TypeGuard: NodeTypeGuard<Data, ScratchData> = {
    isDataCompatible(data: BaseNode.Data): data is Data {
      if (!FlowNode.TypeGuard.isDataCompatible(data)) return false;
      const d = data as Data;
      if (d.flowNodeType !== FlowNode.Type.CONDITION) return false;
      if (typeof d.trueEdgeId !== "string") return false;
      if (typeof d.falseEdgeId !== "string") return false;
      return true;
    },

    isScratchDataCompatible(
      scratchData: BaseNode.ScratchData,
    ): scratchData is ScratchData {
      if (!FlowNode.TypeGuard.isScratchDataCompatible(scratchData))
        return false;
      return true;
    },
  };

  export interface Data extends FlowNode.Data {
    trueEdgeId: string;
    falseEdgeId: string;
    flowNodeType: FlowNode.Type.CONDITION;
  }

  export interface ScratchData extends FlowNode.ScratchData {
    $jp: IfComparison | IfComparisonWithZero | Switch | undefined;
  }
}

export default ConditionNode;
