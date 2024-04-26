import FlowGraphGenerator from "./FlowGraphGenerator.js";
import BaseGraph from "../graph/BaseGraph.js";
import Graph from "../graph/Graph.js";
import FunctionNode from "./node/instruction/FunctionNode.js";
var FlowGraph;
(function (FlowGraph) {
    class Class extends BaseGraph.Class {
        // addFunction(
        //     $jp: FunctionJp,
        //     bodyHead: FlowNode.Class,
        //     bodyTail: InstructionNode.Class[],
        //     params: VarDeclarationNode.Class[] = [],
        // ): [FunctionEntryNode.Class, FunctionExitNode.Class?] {
        //     const function_entry = this.addNode()
        //         .init(new FunctionEntryNode.Builder($jp))
        //         .as(FunctionEntryNode.Class);
        //     this.data.functions.set($jp.name, function_entry.id);
        //     const function_exit = this.addNode()
        //         .init(new FunctionExitNode.Builder($jp))
        //         .as(FunctionExitNode.Class);
        //     function_exit.insertBefore(function_entry);
        //     for (const param of params) {
        //         function_exit.insertBefore(param);
        //     }
        //     function_exit.insertSubgraphBefore(bodyHead, bodyTail);
        //     if (bodyTail.length === 0) {
        //         function_exit.removeFromFlow();
        //         function_exit.remove();
        //         return [function_entry];
        //     }
        //     return [function_entry, function_exit];
        // }
        addMethod($jp, body) {
            const method = this.addNode()
                .init(new FunctionNode.Builder($jp))
                .as(FunctionNode.Class);
            this.data.functions.set($jp.name, method.id);
            method.nextNode = body;
            return method;
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