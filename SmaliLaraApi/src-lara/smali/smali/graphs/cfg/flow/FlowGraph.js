import FlowGraphGenerator from "./FlowGraphGenerator.js";
import FlowNode from "./node/FlowNode.js";
import BaseGraph from "../graph/BaseGraph.js";
import Graph from "../graph/Graph.js";
import FunctionEntryNode from "./node/instruction/FunctionEntryNode.js";
import FunctionExitNode from "./node/instruction/FunctionExitNode.js";
import ControlFlowEdge from "./edge/ControlFlowEdge.js";
import IfComparisonNode from "./node/condition/IfComparisonNode.js";
import TryCatchNode from "./node/condition/TryCatchNode.js";
import CaseNode from "./node/condition/CaseNode.js";
var FlowGraph;
(function (FlowGraph) {
    class Class extends BaseGraph.Class {
        addFunction($jp, bodyHead, bodyTail) {
            const function_entry = this.addNode()
                .init(new FunctionEntryNode.Builder($jp))
                .as(FunctionEntryNode.Class);
            this.data.functions.set($jp.name, function_entry.id);
            const function_exit = this.addNode()
                .init(new FunctionExitNode.Builder($jp))
                .as(FunctionExitNode.Class);
            function_exit.insertBefore(function_entry);
            //   for (const param of params) {
            //     function_exit.insertBefore(param);
            // }
            if (bodyHead !== undefined) {
                function_exit.insertSubgraphBefore(bodyHead, bodyTail);
            }
            if (bodyTail.length === 0) {
                function_exit.removeFromFlow();
                function_exit.remove();
                return [function_entry];
            }
            return [function_entry, function_exit];
        }
        addCondition($jp, iftrue, iffalse) {
            const ifnode = this.addNode();
            const iftrueEdge = this.addEdge(ifnode, iftrue).init(new ControlFlowEdge.Builder());
            const iffalseEdge = this.addEdge(ifnode, iffalse).init(new ControlFlowEdge.Builder());
            return ifnode
                .init(new IfComparisonNode.Builder(iftrueEdge, iffalseEdge, $jp))
                .as(IfComparisonNode.Class);
        }
        addSwitchCase($jp, iftrue, iffalse) {
            const caseNode = this.addNode();
            const iftrueEdge = this.addEdge(caseNode, iftrue).init(new ControlFlowEdge.Builder());
            const iffalseEdge = this.addEdge(caseNode, iffalse).init(new ControlFlowEdge.Builder());
            return caseNode
                .init(new CaseNode.Builder(iftrueEdge, iffalseEdge, $jp))
                .as(CaseNode.Class);
        }
        addTryCatch($jp, iftrue, iffalse) {
            const tryNode = this.addNode();
            const iftrueEdge = this.addEdge(tryNode, iftrue).init(new ControlFlowEdge.Builder());
            const iffalseEdge = this.addEdge(tryNode, iffalse).init(new ControlFlowEdge.Builder());
            return tryNode
                .init(new TryCatchNode.Builder(iftrueEdge, iffalseEdge, $jp))
                .as(TryCatchNode.Class);
        }
        getFunction(name) {
            const id = this.data.functions.get(name);
            if (id === undefined)
                return undefined;
            const node = this.getNodeById(id);
            if (node === undefined || !node.is(FunctionEntryNode.TypeGuard)) {
                return undefined;
            }
            return node.as(FunctionEntryNode.Class);
        }
        get functions() {
            const nodes = [];
            for (const id of this.data.functions.values()) {
                const node = this.getNodeById(id);
                if (node === undefined || !node.is(FunctionEntryNode.TypeGuard)) {
                    continue;
                }
                nodes.push(node.as(FunctionEntryNode.Class));
            }
            return nodes;
        }
        // /**
        //  * Returns the graph node where the given statement belongs.
        //  *
        //  * @param $stmt - A statement join point, or a string with the id of the join point
        //  */
        getNode($stmt) {
            // If string, assume it is id
            const id = typeof $stmt === "string" ? $stmt : $stmt.id;
            const node = this.getNodeById(id);
            if (node === undefined || !node.is(FlowNode.TypeGuard)) {
                return undefined;
            }
            return node.as(FlowNode.Class);
        }
    }
    FlowGraph.Class = Class;
    class Builder extends BaseGraph.Builder {
        buildData(data) {
            return {
                ...super.buildData(data),
                functions: new Map(),
            };
        }
        buildScratchData(scratchData) {
            return {
                ...super.buildScratchData(scratchData),
            };
        }
    }
    FlowGraph.Builder = Builder;
    FlowGraph.TypeGuard = {
        isDataCompatible(data) {
            if (!BaseGraph.TypeGuard.isDataCompatible(data))
                return false;
            return true;
        },
        isScratchDataCompatible(sData) {
            if (!BaseGraph.TypeGuard.isScratchDataCompatible(sData))
                return false;
            return true;
        },
    };
    // ---------------------
    function generate($jp, graph) {
        const flowGraph = (graph ?? Graph.create())
            .init(new FlowGraph.Builder())
            .as(FlowGraph.Class);
        return new FlowGraphGenerator($jp, flowGraph).build();
    }
    FlowGraph.generate = generate;
})(FlowGraph || (FlowGraph = {}));
export default FlowGraph;
//# sourceMappingURL=FlowGraph.js.map