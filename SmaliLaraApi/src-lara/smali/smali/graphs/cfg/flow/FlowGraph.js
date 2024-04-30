import FlowGraphGenerator from "./FlowGraphGenerator.js";
import BaseGraph from "../graph/BaseGraph.js";
import Graph from "../graph/Graph.js";
import FunctionEntryNode from "./node/instruction/FunctionEntryNode.js";
import FunctionExitNode from "./node/instruction/FunctionExitNode.js";
import ControlFlowEdge from "./edge/ControlFlowEdge.js";
import ConditionNode from "./node/condition/ConditionNode.js";
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
            function_exit.insertSubgraphBefore(bodyHead, bodyTail);
            if (bodyTail.length === 0) {
                function_exit.removeFromFlow();
                function_exit.remove();
                return [function_entry];
            }
            return [function_entry, function_exit];
        }
        // addScope($jp: Scope, subGraphs: [FlowNode.Class, InstructionNode.Class[]][]): [ScopeStartNode.Class, ScopeEndNode.Class?] {
        //     const scope_start = this.addNode()
        //         .init(new ScopeStartNode.Builder($jp))
        //         .as(ScopeStartNode.Class);
        //     let current_tail: InstructionNode.Class[] = [scope_start];
        //     for (const [head, tail] of subGraphs) {
        //         for (const tailNode of current_tail) {
        //             tailNode.nextNode = head;
        //         }
        //         current_tail = tail;
        //     }
        //     if (current_tail.length === 0) {
        //         return [scope_start];
        //     }
        //     const scope_end = this.addNode()
        //         .init(new ScopeEndNode.Builder($jp))
        //         .as(ScopeEndNode.Class);
        //     for (const tailNode of current_tail) {
        //         tailNode.nextNode = scope_end;
        //     }
        //     return [scope_start, scope_end];
        // }
        addCondition($jp, iftrue, iffalse) {
            const ifnode = this.addNode();
            const iftrueEdge = this.addEdge(ifnode, iftrue).init(new ControlFlowEdge.Builder());
            const iffalseEdge = this.addEdge(ifnode, iffalse).init(new ControlFlowEdge.Builder());
            return ifnode
                .init(new ConditionNode.Builder(iftrueEdge, iffalseEdge, $jp))
                .as(ConditionNode.Class);
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