import FlowEdge from "./FlowEdge.js";
var ControlFlowEdge;
(function (ControlFlowEdge) {
    class Class extends FlowEdge.Class {
    }
    ControlFlowEdge.Class = Class;
    class Builder extends FlowEdge.Builder {
        constructor() {
            super(FlowEdge.Type.CONTROL_FLOW);
        }
        buildData(data) {
            return {
                ...super.buildData(data),
            };
        }
        buildScratchData(scratchData) {
            return {
                ...super.buildScratchData(scratchData),
            };
        }
    }
    ControlFlowEdge.Builder = Builder;
    ControlFlowEdge.TypeGuard = {
        isDataCompatible(data) {
            if (!FlowEdge.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.flowEdgeType !== FlowEdge.Type.CONTROL_FLOW)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!FlowEdge.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(ControlFlowEdge || (ControlFlowEdge = {}));
export default ControlFlowEdge;
//# sourceMappingURL=ControlFlowEdge.js.map