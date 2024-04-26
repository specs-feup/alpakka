import ControlFlowEdge from "../../edge/ControlFlowEdge.js";
import FlowNode from "../../node/FlowNode.js";
var InstructionNode;
(function (InstructionNode) {
    class Class extends FlowNode.Class {
        get nextEdge() {
            if (this.data.nextEdgeId === undefined) {
                return undefined;
            }
            // Data and scratchdata should be BaseEdge
            const edge = this.graph.getEdgeById(this.data.nextEdgeId);
            if (edge === undefined) {
                // this.data.nextEdgeId = undefined;
                return undefined;
            }
            return edge.as(ControlFlowEdge.Class);
        }
        get nextNode() {
            const node = this.nextEdge?.target;
            if (node === undefined || !node.is(FlowNode.TypeGuard)) {
                return undefined;
            }
            return node.as(FlowNode.Class);
        }
        set nextNode(node) {
            const edge = this.nextEdge;
            if (edge !== undefined && node !== undefined) {
                edge.target = node;
            }
            else if (edge !== undefined && node === undefined) {
                edge.remove();
                this.data.nextEdgeId = undefined;
            }
            else if (edge === undefined && node !== undefined) {
                const newEdge = this.graph
                    .addEdge(this, node)
                    .init(new ControlFlowEdge.Builder());
                this.data.nextEdgeId = newEdge.id;
            }
        }
    }
    InstructionNode.Class = Class;
    class Builder extends FlowNode.Builder {
        #instructionFlowNodeType;
        constructor(type, $jp) {
            super(FlowNode.Type.INSTRUCTION, $jp);
            this.#instructionFlowNodeType = type;
        }
        buildData(data) {
            return {
                ...super.buildData(data),
                instructionFlowNodeType: this.#instructionFlowNodeType,
                nextEdgeId: undefined,
            };
        }
        buildScratchData(scratchData) {
            return {
                ...super.buildScratchData(scratchData),
            };
        }
    }
    InstructionNode.Builder = Builder;
    InstructionNode.TypeGuard = {
        isDataCompatible(data) {
            if (!FlowNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.flowNodeType !== FlowNode.Type.INSTRUCTION)
                return false;
            if (!Object.values(Type).includes(d.instructionFlowNodeType))
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!FlowNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
    // ------------------------------------------------------------
    let Type;
    (function (Type) {
        Type["FUNCTION_ENTRY"] = "function_entry";
        Type["FUNCTION_EXIT"] = "function_exit";
        Type["SCOPE_START"] = "scope_start";
        Type["SCOPE_END"] = "scope_end";
        Type["COMMENT"] = "comment";
        Type["PRAGMA"] = "pragma";
        Type["VAR_DECLARATION"] = "var_declaration";
        Type["EMPTY_STATEMENT"] = "empty_statement";
        Type["EXPRESSION"] = "expression";
        Type["STATEMENT"] = "statement";
        Type["SWITCH"] = "switch";
        Type["RETURN"] = "return";
        Type["BREAK"] = "break";
        Type["CONTINUE"] = "continue";
        Type["GOTO_LABEL"] = "label";
        Type["GOTO"] = "goto";
        Type["UNKNOWN"] = "unknown";
    })(Type = InstructionNode.Type || (InstructionNode.Type = {}));
})(InstructionNode || (InstructionNode = {}));
export default InstructionNode;
//# sourceMappingURL=InstructionNode.js.map