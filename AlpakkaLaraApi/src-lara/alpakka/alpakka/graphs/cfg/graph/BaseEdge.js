import BaseNode from "./BaseNode.js";
import Graph from "./Graph.js";
var BaseEdge;
(function (BaseEdge) {
    class Class {
        #graph;
        #edge;
        // _d and _sd are a hack to force typescript to typecheck
        // D and S in .as() method.
        constructor(graph, edge, _d = {}, _sd = {}) {
            this.#graph = graph;
            this.#edge = edge;
        }
        get data() {
            return this.#edge.data();
        }
        get scratchData() {
            return this.#edge.scratch(Graph.scratchNamespace);
        }
        get id() {
            return this.#edge.id();
        }
        get source() {
            return new BaseNode.Class(this.#graph, this.#edge.source());
        }
        set source(node) {
            this.#edge.move({ source: node.id });
        }
        get target() {
            return new BaseNode.Class(this.#graph, this.#edge.target());
        }
        set target(node) {
            this.#edge.move({ target: node.id });
        }
        is(guard) {
            const data = this.data;
            const scratchData = this.scratchData;
            const result = guard.isDataCompatible(data) &&
                guard.isScratchDataCompatible(scratchData);
            // Have typescript statically check that the types are correct
            // in the implementation of this function.
            result && data && scratchData;
            return result;
        }
        as(EdgeType) {
            return new EdgeType(this.#graph, this.#edge, this.data, this.scratchData);
        }
        init(builder) {
            const initedData = builder.buildData(this.data);
            const initedScratchData = builder.buildScratchData(this.scratchData);
            this.#edge.data(initedData);
            this.#edge.scratch(Graph.scratchNamespace, initedScratchData);
            return new BaseEdge.Class(this.#graph, this.#edge, initedData, initedScratchData);
        }
        remove() {
            this.#edge.remove();
        }
        get graph() {
            return this.#graph;
        }
        toCy() {
            return this.#edge;
        }
    }
    BaseEdge.Class = Class;
    class Builder {
        buildData(data) {
            return data;
        }
        buildScratchData(scratchData) {
            return scratchData;
        }
    }
    BaseEdge.Builder = Builder;
    BaseEdge.TypeGuard = {
        isDataCompatible(data) {
            return true;
        },
        isScratchDataCompatible(scratchData) {
            return true;
        },
    };
})(BaseEdge || (BaseEdge = {}));
export default BaseEdge;
//# sourceMappingURL=BaseEdge.js.map