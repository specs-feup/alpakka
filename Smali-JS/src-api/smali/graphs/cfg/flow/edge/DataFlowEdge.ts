import FlowEdge from "../../flow/edge/FlowEdge.js";
import BaseEdge from "../../graph/BaseEdge.js";
import { EdgeBuilder, EdgeTypeGuard } from "../../graph/Edge.js";

namespace DataFlowEdge {
  export class Class<
    D extends Data = Data,
    S extends ScratchData = ScratchData,
  > extends FlowEdge.Class<D, S> {}

  export class Builder
    extends FlowEdge.Builder
    implements EdgeBuilder<Data, ScratchData>
  {
    constructor() {
      super(FlowEdge.Type.DATA_FLOW);
    }

    buildData(data: BaseEdge.Data): Data {
      return {
        ...(super.buildData(data) as FlowEdge.Data & {
          flowEdgeType: FlowEdge.Type.DATA_FLOW;
        }),
      };
    }

    buildScratchData(scratchData: BaseEdge.ScratchData): ScratchData {
      return {
        ...super.buildScratchData(scratchData),
      };
    }
  }

  export const TypeGuard: EdgeTypeGuard<Data, ScratchData> = {
    isDataCompatible(data: BaseEdge.Data): data is Data {
      if (!FlowEdge.TypeGuard.isDataCompatible(data)) return false;
      const d = data as Data;
      if (d.flowEdgeType !== FlowEdge.Type.DATA_FLOW) return false;
      return true;
    },

    isScratchDataCompatible(
      scratchData: BaseEdge.ScratchData,
    ): scratchData is ScratchData {
      if (!FlowEdge.TypeGuard.isScratchDataCompatible(scratchData))
        return false;
      return true;
    },
  };

  export interface Data extends FlowEdge.Data {
    flowEdgeType: FlowEdge.Type.DATA_FLOW;
  }

  export interface ScratchData extends FlowEdge.ScratchData {}
}

export default DataFlowEdge;
