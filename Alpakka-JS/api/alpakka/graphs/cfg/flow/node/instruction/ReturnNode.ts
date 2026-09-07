import * as InstructionNode from "./InstructionNode.ts";
import * as BaseNode from "../../../graph/BaseNode.ts";
import type { NodeBuilder, NodeTypeGuard } from "../../../graph/Node.ts";
import { ReturnStatement } from "../../../../../../Joinpoints.ts";

export class Class<
  D extends Data = Data,
  S extends ScratchData = ScratchData,
> extends InstructionNode.Class<D, S> {
  override get jp(): ReturnStatement {
    return this.scratchData.$jp;
  }
}

export class Builder
  extends InstructionNode.Builder
  implements NodeBuilder<Data, ScratchData>
{
  constructor($jp: ReturnStatement) {
    super(InstructionNode.Type.RETURN, $jp);
  }

  buildData(data: BaseNode.Data): Data {
    return {
      ...(super.buildData(data) as InstructionNode.Data & {
        instructionFlowNodeType: typeof InstructionNode.Type.RETURN;
      }),
    };
  }

  buildScratchData(scratchData: BaseNode.ScratchData): ScratchData {
    return {
      ...(super.buildScratchData(scratchData) as InstructionNode.Data & {
        $jp: ReturnStatement;
      }),
    };
  }
}

export const TypeGuard: NodeTypeGuard<Data, ScratchData> = {
  isDataCompatible(data: BaseNode.Data): data is Data {
    if (!InstructionNode.TypeGuard.isDataCompatible(data)) return false;
    const d = data as Data;
    if (d.instructionFlowNodeType !== InstructionNode.Type.RETURN)
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
  instructionFlowNodeType: typeof InstructionNode.Type.RETURN;
}

export interface ScratchData extends InstructionNode.ScratchData {
  $jp: ReturnStatement;
}
