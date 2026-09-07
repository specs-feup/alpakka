import cytoscape from "cytoscape";
import * as BaseGraph from "./BaseGraph.ts";

export type GraphConstructor<
  D extends BaseGraph.Data,
  S extends BaseGraph.ScratchData,
  G extends BaseGraph.Class<D, S>,
> = new (node: cytoscape.Core, _d: D, _sd: S) => G;

export interface GraphBuilder<
  D extends BaseGraph.Data,
  S extends BaseGraph.ScratchData,
> {
  buildData(data: BaseGraph.Data): D;
  buildScratchData(scratchData: BaseGraph.ScratchData): S;
}

export interface GraphTypeGuard<
  D extends BaseGraph.Data,
  S extends BaseGraph.ScratchData,
> {
  isDataCompatible(data: BaseGraph.Data): data is D;
  isScratchDataCompatible(sData: BaseGraph.ScratchData): sData is S;
}

export interface GraphTransformation {
  apply(graph: BaseGraph.Class): void;
}

export const scratchNamespace = "_smali_flow";

export function create(): BaseGraph.Class {
  return new BaseGraph.Class(cytoscape({}));
}

export function fromCy(graph: cytoscape.Core): BaseGraph.Class {
  return new BaseGraph.Class(graph);
}
