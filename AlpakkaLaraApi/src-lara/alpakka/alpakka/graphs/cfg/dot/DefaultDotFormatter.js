import DotFormatter from "./DotFormatter.js";
export default class DefaultDotFormatter extends DotFormatter {
    formatNode(node) {
        return {
            id: node.id,
            attrs: {
                label: node.id,
                shape: "box",
            },
        };
    }
    formatEdge(edge) {
        return {
            source: edge.source.id,
            target: edge.target.id,
            attrs: {
                label: edge.id,
            },
        };
    }
}
//# sourceMappingURL=DefaultDotFormatter.js.map