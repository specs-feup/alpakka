var _a;
class DotFormatter {
    static defaultGraphName = "clava_graph";
    static #sanitizeDotLabel(label) {
        return label.replaceAll('"', '\\"');
    }
    #formatAttrs(attrs) {
        return attrs
            .map(([key, value]) => `${key}="${_a.#sanitizeDotLabel(value)}"`)
            .join(" ");
    }
    format(graph, label) {
        const graphName = label ?? _a.defaultGraphName;
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
_a = DotFormatter;
export default DotFormatter;
//# sourceMappingURL=DotFormatter.js.map