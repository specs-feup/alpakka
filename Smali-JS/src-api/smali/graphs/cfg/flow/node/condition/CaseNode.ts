import BaseNode from "../../../graph/BaseNode.js";
import { NodeBuilder, NodeTypeGuard } from "../../../graph/Node.js";
import { LabelReference } from "../../../../../../Joinpoints.js";
import ConditionNode from "./ConditionNode.js";
import ControlFlowEdge from "../../edge/ControlFlowEdge.js";

namespace CaseNode {
  export class Class<
    D extends Data = Data,
    S extends ScratchData = ScratchData,
  > extends ConditionNode.Class<D, S> {
    override get jp(): LabelReference {
      return this.scratchData.$jp;
    }
  }

  export class Builder
    extends ConditionNode.Builder
    implements NodeBuilder<Data, ScratchData>
  {
    constructor(
      truePath: ControlFlowEdge.Class,
      falsePath: ControlFlowEdge.Class,
      $jp: LabelReference,
    ) {
      super(ConditionNode.Type.SWITCH_CASE, truePath, falsePath, $jp);
    }

    buildData(data: BaseNode.Data): Data {
      return {
        ...(super.buildData(data) as ConditionNode.Data & {
          conditionFlowNodeType: ConditionNode.Type.SWITCH_CASE;
        }),
      };
    }

    buildScratchData(scratchData: BaseNode.ScratchData): ScratchData {
      return {
        ...(super.buildScratchData(scratchData) as ConditionNode.Data & {
          $jp: LabelReference;
        }),
      };
    }
  }

  export const TypeGuard: NodeTypeGuard<Data, ScratchData> = {
    isDataCompatible(data: BaseNode.Data): data is Data {
      if (!ConditionNode.TypeGuard.isDataCompatible(data)) return false;
      const d = data as Data;
      if (d.conditionFlowNodeType !== ConditionNode.Type.SWITCH_CASE)
        return false;
      return true;
    },

    isScratchDataCompatible(
      scratchData: BaseNode.ScratchData,
    ): scratchData is ScratchData {
      if (!ConditionNode.TypeGuard.isScratchDataCompatible(scratchData))
        return false;
      return true;
    },
  };

  export interface Data extends ConditionNode.Data {
    conditionFlowNodeType: ConditionNode.Type.SWITCH_CASE;
  }

  export interface ScratchData extends ConditionNode.ScratchData {
    $jp: LabelReference;
  }
}

export default CaseNode;
