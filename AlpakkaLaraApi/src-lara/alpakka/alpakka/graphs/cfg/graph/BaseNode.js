import Graph from "./Graph.js";
import BaseEdge from "./BaseEdge.js";
var BaseNode;
(function (BaseNode) {
    class Class {
        #graph;
        #node;
        // _d and _sd are a hack to force typescript to typecheck
        // D and S in .as() method.
        constructor(graph, node, _d = {}, _sd = {}) {
            this.#graph = graph;
            this.#node = node;
        }
        get data() {
            return this.#node.data();
        }
        get scratchData() {
            return this.#node.scratch(Graph.scratchNamespace);
        }
        get id() {
            return this.#node.id();
        }
        get incomers() {
            return this.#node
                .incomers()
                .edges()
                .map((edge) => new BaseEdge.Class(this.#graph, edge));
        }
        get outgoers() {
            return this.#node
                .outgoers()
                .edges()
                .map((edge) => new BaseEdge.Class(this.#graph, edge));
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
        as(NodeType) {
            return new NodeType(this.#graph, this.#node, this.data, this.scratchData);
        }
        init(builder) {
            const initedData = builder.buildData(this.data);
            const initedScratchData = builder.buildScratchData(this.scratchData);
            this.#node.data(initedData);
            this.#node.scratch(Graph.scratchNamespace, initedScratchData);
            return new BaseNode.Class(this.#graph, this.#node, initedData, initedScratchData);
        }
        remove() {
            this.#node.remove();
        }
        // cytoscape's dfs is not flexible enough for clava-flow purposes
        // at least as far as I can tell
        // Returns a generator that yields [node, path, index]
        bfs(propagate) {
            function* inner(root) {
                const toVisit = [[root, []]];
                const visited = new Set();
                let idx = 0;
                while (toVisit.length > 0) {
                    const [node, path] = toVisit.pop();
                    if (visited.has(node.id)) {
                        continue;
                    }
                    if (path.length > 0 && !propagate(path[path.length - 1])) {
                        continue;
                    }
                    yield [node, path, idx];
                    idx++;
                    visited.add(node.id);
                    for (const out of node.outgoers) {
                        toVisit.push([out.target, [...path, out]]);
                    }
                }
            }
            return inner(this);
        }
        get graph() {
            return this.#graph;
        }
        toCy() {
            return this.#node;
        }
    }
    BaseNode.Class = Class;
    class Builder {
        buildData(data) {
            return data;
        }
        buildScratchData(scratchData) {
            return scratchData;
        }
    }
    BaseNode.Builder = Builder;
    BaseNode.TypeGuard = {
        isDataCompatible(data) {
            return true;
        },
        isScratchDataCompatible(sData) {
            return true;
        },
    };
})(BaseNode || (BaseNode = {}));
export default BaseNode;
//# sourceMappingURL=BaseNode.js.map