import Graph from "./Graph.js";
import BaseNode from "./BaseNode.js";
import BaseEdge from "./BaseEdge.js";
import Io from "lara-js/api/lara/Io.js";
var BaseGraph;
(function (BaseGraph) {
    class Class {
        #graph;
        // _d and _sd are a hack to force typescript to typecheck
        // D and S in .as() method.
        constructor(graph, _d = {}, _sd = {}) {
            this.#graph = graph;
        }
        get data() {
            return this.#graph.data();
        }
        get scratchData() {
            return this.#graph.scratch(Graph.scratchNamespace);
        }
        addNode(id) {
            const newNode = this.#graph.add({
                group: "nodes",
                data: { id },
            });
            return new BaseNode.Class(this, newNode);
        }
        addEdge(source, target, id) {
            const newEdge = this.#graph.add({
                group: "edges",
                data: { id, source: source.id, target: target.id },
            });
            return new BaseEdge.Class(this, newEdge);
        }
        getNodeById(id) {
            const node = this.#graph.getElementById(id);
            if (node.isNode()) {
                return new BaseNode.Class(this, node);
            }
            return undefined;
        }
        getEdgeById(id) {
            const edge = this.#graph.getElementById(id);
            if (edge.isEdge()) {
                return new BaseEdge.Class(this, edge);
            }
            return undefined;
        }
        // TODO
        get nodes() {
            return this.#graph.nodes().map((node) => new BaseNode.Class(this, node));
        }
        // TODO
        get edges() {
            return this.#graph.edges().map((edge) => new BaseEdge.Class(this, edge));
        }
        is(guard) {
            const data = this.data;
            const scratchData = this.scratchData;
            const result = guard.isDataCompatible(data) &&
                guard.isScratchDataCompatible(scratchData);
            // Have typescript statically check that the types are correct
            // in the implementation of this function.
            result && (data) && (scratchData);
            return result;
        }
        as(GraphType) {
            return new GraphType(this.#graph, this.data, this.scratchData);
        }
        init(builder) {
            const initedData = builder.buildData(this.data);
            const initedScratchData = builder.buildScratchData(this.scratchData);
            this.#graph.data(initedData);
            this.#graph.scratch(Graph.scratchNamespace, initedScratchData);
            return new BaseGraph.Class(this.#graph, initedData, initedScratchData);
        }
        apply(transformation) {
            transformation.apply(this);
            return this;
        }
        toDot(dotFormatter, label) {
            return dotFormatter.format(this, label);
        }
        toDotFile(dotFormatter, filename, label) {
            return Io.writeFile(filename, this.toDot(dotFormatter, label));
        }
        toCy() {
            return this.#graph;
        }
    }
    BaseGraph.Class = Class;
    class Builder {
        buildData(data) {
            return data;
        }
        buildScratchData(scratchData) {
            return scratchData;
        }
    }
    BaseGraph.Builder = Builder;
    BaseGraph.TypeGuard = {
        isDataCompatible(data) {
            return true;
        },
        isScratchDataCompatible(sData) {
            return true;
        },
    };
})(BaseGraph || (BaseGraph = {}));
export default BaseGraph;
//# sourceMappingURL=BaseGraph.js.map