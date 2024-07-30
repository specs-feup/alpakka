import FlowNode from "../node/FlowNode.js";
export default class FilterFlowNodes {
    #filterFn;
    constructor(filterFn) {
        this.#filterFn = filterFn;
    }
    apply(graph) {
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
//# sourceMappingURL=FilterFlowNodes.js.map