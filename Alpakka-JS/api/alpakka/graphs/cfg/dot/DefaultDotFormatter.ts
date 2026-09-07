import DotFormatter from "./DotFormatter.ts";
import type { Node, Edge } from "./DotFormatter.ts";
import * as BaseEdge from "../graph/BaseEdge.ts";
import * as BaseNode from "../graph/BaseNode.ts";

export default class DefaultDotFormatter extends DotFormatter {
  override formatNode(node: BaseNode.Class): Node {
    return {
      id: node.id,
      attrs: {
        label: node.id,
        shape: "box",
      },
    };
  }

  override formatEdge(edge: BaseEdge.Class): Edge {
    return {
      source: edge.source.id,
      target: edge.target.id,
      attrs: {
        label: edge.id,
      },
    };
  }
}
