import DotFormatter from "./DotFormatter.js";
import BaseEdge from "../graph/BaseEdge.js";
import BaseNode from "../graph/BaseNode.js";

export default class DefaultDotFormatter extends DotFormatter {
  override formatNode(node: BaseNode.Class): DotFormatter.Node {
    return {
      id: node.id,
      attrs: {
        label: node.id,
        shape: "box",
      },
    };
  }

  override formatEdge(edge: BaseEdge.Class): DotFormatter.Edge {
    return {
      source: edge.source.id,
      target: edge.target.id,
      attrs: {
        label: edge.id,
      },
    };
  }
}
