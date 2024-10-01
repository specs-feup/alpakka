import cytoscape from "@specs-feup/lara/api/libs/cytoscape-3.26.0.js";
import BaseGraph from "./BaseGraph.js";
var Graph;
(function (Graph) {
    Graph.scratchNamespace = "_smali_flow";
    function create() {
        return new BaseGraph.Class(cytoscape({}));
    }
    Graph.create = create;
    function fromCy(graph) {
        return new BaseGraph.Class(graph);
    }
    Graph.fromCy = fromCy;
})(Graph || (Graph = {}));
export default Graph;
//# sourceMappingURL=Graph.js.map