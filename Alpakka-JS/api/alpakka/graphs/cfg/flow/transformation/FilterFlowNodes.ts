import * as FlowNode from "../node/FlowNode.ts";
import * as BaseGraph from "../../graph/BaseGraph.ts";
import type { GraphTransformation } from "../../graph/Graph.ts";

export default class FilterFlowNodes implements GraphTransformation {
  #filterFn: (node: FlowNode.Class) => boolean;

  constructor(filterFn: (node: FlowNode.Class) => boolean) {
    this.#filterFn = filterFn;
  }

  apply(graph: BaseGraph.Class): void {
    for (const node of graph.nodes) {
      if (node.is(FlowNode.TypeGuard)) {
        const flowNode = node.as(FlowNode.Class);
        if (!this.#filterFn(flowNode)) {
          flowNode.removeFromFlow();
          flowNode.remove();
        }
      }
    }
  }
}
