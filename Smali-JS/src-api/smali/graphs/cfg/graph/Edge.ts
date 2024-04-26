import BaseEdge from "./BaseEdge.js";
import BaseGraph from "./BaseGraph.js";
import cytoscape from "lara-js/api/libs/cytoscape-3.26.0.js";

export type EdgeConstructor<
  D extends BaseEdge.Data,
  S extends BaseEdge.ScratchData,
  E extends BaseEdge.Class<D, S>,
> = new (
  graph: BaseGraph.Class,
  node: cytoscape.EdgeSingular,
  _d: D,
  _sd: S,
) => E;

export interface EdgeBuilder<
  D extends BaseEdge.Data,
  S extends BaseEdge.ScratchData,
> {
  buildData(data: BaseEdge.Data): D;
  buildScratchData(scratchData: BaseEdge.ScratchData): S;
}

export interface EdgeTypeGuard<
  D extends BaseEdge.Data,
  S extends BaseEdge.ScratchData,
> {
  isDataCompatible(data: BaseEdge.Data): data is D;
  isScratchDataCompatible(sData: BaseEdge.ScratchData): sData is S;
}
