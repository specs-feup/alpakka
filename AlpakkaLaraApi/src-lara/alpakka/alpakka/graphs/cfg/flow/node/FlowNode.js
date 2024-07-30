import ControlFlowEdge from "../edge/ControlFlowEdge.js";
import BaseNode from "../../graph/BaseNode.js";
import { Joinpoint } from "../../../../../Joinpoints.js";
var FlowNode;
(function (FlowNode) {
    class Class extends BaseNode.Class {
        insertBefore(node) {
            this.insertSubgraphBefore(node, [node]);
        }
        insertSubgraphBefore(head, tail) {
            this.incomers.forEach((edge) => {
                edge.target = head;
            });
            for (const tailNode of tail) {
                tailNode.nextNode = this;
            }
        }
        removeFromFlow() {
            // TODO improve ergonomics with Collection class
            const incomers = this.incomers.filter((edge) => edge.is(ControlFlowEdge.TypeGuard));
            const outgoers = this.outgoers.filter((edge) => edge.is(ControlFlowEdge.TypeGuard));
            if (incomers.length === 0 || outgoers.length === 0) {
                for (const edge of incomers) {
                    edge.remove();
                }
                for (const edge of outgoers) {
                    edge.remove();
                }
            }
            else if (outgoers.length === 1) {
                for (const edge of incomers) {
                    edge.target = outgoers[0].target;
                }
                outgoers[0].remove();
            }
            else {
                throw new Error("Cannot remove node with at least one incomer and multiple outgoers.");
            }
        }
        get reachableNodes() {
            const result = [];
            for (const [node] of this.bfs((e) => e.is(ControlFlowEdge.TypeGuard))) {
                if (node.is(FlowNode.TypeGuard)) {
                    result.push(node.as(FlowNode.Class));
                }
            }
            return result;
        }
        get previousEdges() {
            return this.incomers
                .filter((edge) => edge.is(ControlFlowEdge.TypeGuard))
                .map((edge) => edge)
                .map((edge) => edge.as(ControlFlowEdge.Class));
        }
        get previousNodes() {
            return this.previousEdges
                .map((edge) => edge.source)
                .filter((node) => node.is(FlowNode.TypeGuard))
                .map((node) => node)
                .map((node) => node.as(FlowNode.Class));
        }
        get nextEdges() {
            return this.outgoers
                .filter((edge) => edge.is(ControlFlowEdge.TypeGuard))
                .map((edge) => edge)
                .map((edge) => edge.as(ControlFlowEdge.Class));
        }
        get nextNodes() {
            return this.nextEdges
                .map((edge) => edge.target)
                .filter((node) => node.is(FlowNode.TypeGuard))
                .map((node) => node)
                .map((node) => node.as(FlowNode.Class));
        }
        get jp() {
            return this.scratchData.$jp;
        }
    }
    FlowNode.Class = Class;
    class Builder extends BaseNode.Builder {
        #$jp;
        #flowNodeType;
        constructor(type, $jp) {
            super();
            this.#$jp = $jp;
            this.#flowNodeType = type;
        }
        buildData(data) {
            return {
                ...super.buildData(data),
                flowNodeType: this.#flowNodeType,
            };
        }
        buildScratchData(scratchData) {
            return {
                ...super.buildScratchData(scratchData),
                $jp: this.#$jp,
            };
        }
    }
    FlowNode.Builder = Builder;
    FlowNode.TypeGuard = {
        isDataCompatible(data) {
            if (!BaseNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (!Object.values(Type).includes(d.flowNodeType))
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!BaseNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            const s = scratchData;
            if (s.$jp !== undefined && !(s.$jp instanceof Joinpoint))
                return false;
            return true;
        },
    };
    // ------------------------------------------------------------
    let Type;
    (function (Type) {
        Type["INSTRUCTION"] = "instruction";
        Type["CONDITION"] = "condition";
    })(Type = FlowNode.Type || (FlowNode.Type = {}));
})(FlowNode || (FlowNode = {}));
export default FlowNode;
//# sourceMappingURL=FlowNode.js.map