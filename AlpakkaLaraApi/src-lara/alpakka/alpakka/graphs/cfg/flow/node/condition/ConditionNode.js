import ControlFlowEdge from "../../edge/ControlFlowEdge.js";
import FlowNode from "../FlowNode.js";
var ConditionNode;
(function (ConditionNode) {
    class Class extends FlowNode.Class {
        get trueEdge() {
            // Data and scratchdata should be ControlFlowEdge
            const edge = this.graph.getEdgeById(this.data.trueEdgeId);
            return edge.as(ControlFlowEdge.Class);
        }
        get trueNode() {
            const node = this.trueEdge.target;
            return node.as(FlowNode.Class);
        }
        set trueNode(node) {
            this.trueEdge.target = node;
        }
        get falseEdge() {
            // Data and scratchdata should be ControlFlowEdge
            const edge = this.graph.getEdgeById(this.data.falseEdgeId);
            return edge.as(ControlFlowEdge.Class);
        }
        get falseNode() {
            const node = this.falseEdge.target;
            return node.as(FlowNode.Class);
        }
        set falseNode(node) {
            this.falseEdge.target = node;
        }
    }
    ConditionNode.Class = Class;
    class Builder extends FlowNode.Builder {
        #truePath;
        #falsePath;
        #conditionFlowNodeType;
        constructor(type, truePath, falsePath, $jp) {
            super(FlowNode.Type.CONDITION, $jp);
            this.#truePath = truePath;
            this.#falsePath = falsePath;
            this.#conditionFlowNodeType = type;
        }
        buildData(data) {
            return {
                ...super.buildData(data),
                trueEdgeId: this.#truePath.id,
                falseEdgeId: this.#falsePath.id,
                conditionFlowNodeType: this.#conditionFlowNodeType,
            };
        }
        buildScratchData(scratchData) {
            return {
                ...super.buildScratchData(scratchData),
            };
        }
    }
    ConditionNode.Builder = Builder;
    ConditionNode.TypeGuard = {
        isDataCompatible(data) {
            if (!FlowNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.flowNodeType !== FlowNode.Type.CONDITION)
                return false;
            if (typeof d.trueEdgeId !== "string")
                return false;
            if (typeof d.falseEdgeId !== "string")
                return false;
            if (!Object.values(Type).includes(d.conditionFlowNodeType))
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
        Type["IF_COMPARISON"] = "if_comparison";
        Type["SWITCH_CASE"] = "switch_case";
        Type["TRY_CATCH"] = "try_catch";
    })(Type = ConditionNode.Type || (ConditionNode.Type = {}));
})(ConditionNode || (ConditionNode = {}));
export default ConditionNode;
//# sourceMappingURL=ConditionNode.js.map