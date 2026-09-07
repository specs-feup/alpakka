import * as BaseGraph from "./BaseGraph.ts";
import * as BaseNode from "./BaseNode.ts";
import cytoscape from "cytoscape";

export type NodeConstructor<
  D extends BaseNode.Data,
  S extends BaseNode.ScratchData,
  N extends BaseNode.Class<D, S>,
> = new (
  graph: BaseGraph.Class,
  node: cytoscape.NodeSingular,
  _d: D,
  _sd: S,
) => N;

export interface NodeBuilder<
  D extends BaseNode.Data,
  S extends BaseNode.ScratchData,
> {
  buildData(data: BaseNode.Data): D;
  buildScratchData(scratchData: BaseNode.ScratchData): S;
}

export interface NodeTypeGuard<
  D extends BaseNode.Data,
  S extends BaseNode.ScratchData,
> {
  isDataCompatible(data: BaseNode.Data): data is D;
  isScratchDataCompatible(sData: BaseNode.ScratchData): sData is S;
}
