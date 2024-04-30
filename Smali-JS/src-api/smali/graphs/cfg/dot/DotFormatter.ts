import BaseEdge from "../graph/BaseEdge.js";
import BaseGraph from "../graph/BaseGraph.js";
import BaseNode from "../graph/BaseNode.js";

abstract class DotFormatter {
  static defaultGraphName: string = "smali_graph";

  abstract formatNode(node: BaseNode.Class): DotFormatter.Node;

  abstract formatEdge(edge: BaseEdge.Class): DotFormatter.Edge;

  static #sanitizeDotLabel(label: string) {
    return label.replace(/(?<!\\)"/g, '\\"');
  }

  #formatAttrs(attrs: [string, string][]): string {
    return attrs
      .map(
        ([key, value]) => `${key}="${DotFormatter.#sanitizeDotLabel(value)}"`,
      )
      .join(" ");
  }

  format(graph: BaseGraph.Class, label?: string): string {
    const graphName = label ?? DotFormatter.defaultGraphName;

    const nodes = graph.nodes
      .map((node) => {
        const { id, attrs } = this.formatNode(node);
        const formattedAttrs = this.#formatAttrs(Object.entries(attrs));
        return `"${id}" [${formattedAttrs}];\n`;
      })
      .join("");

    const edges = graph.edges
      .map((edge) => {
        const { source, target, attrs } = this.formatEdge(edge);
        const formattedAttrs = this.#formatAttrs(Object.entries(attrs));

        return `"${source}" -> "${target}" [${formattedAttrs}];\n`;
      })
      .join("");

    return `digraph ${graphName} {\n${nodes}${edges}}\n`;
  }
}

namespace DotFormatter {
  export interface Attrs {
    label: string;
    shape: string;
    [key: string]: string;
  }

  export interface Node {
    id: string;
    attrs: {
      label: string;
      shape: string;
      [key: string]: string;
    };
  }

  export interface Edge {
    source: string;
    target: string;
    attrs: {
      [key: string]: string;
    };
  }
}

export default DotFormatter;
