import InstructionNode from "../../node/instruction/InstructionNode.js";
import BaseNode from "../../../graph/BaseNode.js";
import { NodeBuilder, NodeTypeGuard } from "../../../graph/Node.js";
import { Joinpoint } from "../../../../../../Joinpoints.js";

namespace UnknownInstructionNode {
  export class Class<
    D extends Data = Data,
    S extends ScratchData = ScratchData,
  > extends InstructionNode.Class<D, S> {}

  export class Builder
    extends InstructionNode.Builder
    implements NodeBuilder<Data, ScratchData>
  {
    constructor($jp?: Joinpoint) {
      super(InstructionNode.Type.UNKNOWN, $jp);
    }

    buildData(data: BaseNode.Data): Data {
      return {
        ...(super.buildData(data) as InstructionNode.Data & {
          instructionFlowNodeType: InstructionNode.Type.UNKNOWN;
        }),
      };
    }

    buildScratchData(scratchData: BaseNode.ScratchData): ScratchData {
      return {
        ...super.buildScratchData(scratchData),
      };
    }
  }

  export const TypeGuard: NodeTypeGuard<Data, ScratchData> = {
    isDataCompatible(data: BaseNode.Data): data is Data {
      if (!InstructionNode.TypeGuard.isDataCompatible(data)) return false;
      const d = data as Data;
      if (d.instructionFlowNodeType !== InstructionNode.Type.UNKNOWN)
        return false;
      return true;
    },

    isScratchDataCompatible(
      scratchData: BaseNode.ScratchData,
    ): scratchData is ScratchData {
      if (!InstructionNode.TypeGuard.isScratchDataCompatible(scratchData))
        return false;
      return true;
    },
  };

  export interface Data extends InstructionNode.Data {
    instructionFlowNodeType: InstructionNode.Type.UNKNOWN;
  }

  export interface ScratchData extends InstructionNode.ScratchData {}
}

export default UnknownInstructionNode;
