import * as BaseEdge from "../../graph/BaseEdge.ts";
import type { EdgeBuilder, EdgeTypeGuard } from "../../graph/Edge.ts";

export class Class<
  D extends Data = Data,
  S extends ScratchData = ScratchData,
> extends BaseEdge.Class<D, S> {}

export abstract class Builder
  extends BaseEdge.Builder
  implements EdgeBuilder<Data, ScratchData>
{
  #flowEdgeType: Type;

  constructor(type: Type) {
    super();
    this.#flowEdgeType = type;
  }

  buildData(data: BaseEdge.Data): Data {
    return {
      ...super.buildData(data),
      flowEdgeType: this.#flowEdgeType,
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
    if (!BaseEdge.TypeGuard.isDataCompatible(data)) return false;
    const d = data as Data;
    if (!Object.values(Type).includes(d.flowEdgeType as Type)) return false;
    return true;
  },

  isScratchDataCompatible(
    scratchData: BaseEdge.ScratchData,
  ): scratchData is ScratchData {
    if (!BaseEdge.TypeGuard.isScratchDataCompatible(scratchData))
      return false;
    return true;
  },
};

export interface Data extends BaseEdge.Data {
  flowEdgeType: Type;
}

export interface ScratchData extends BaseEdge.ScratchData {}

// ------------------------------------------------------------

export const Type = {
  CONTROL_FLOW: "control_flow",
  DATA_FLOW: "data_flow"
} as const;

export type Type = (typeof Type)[keyof typeof Type];
