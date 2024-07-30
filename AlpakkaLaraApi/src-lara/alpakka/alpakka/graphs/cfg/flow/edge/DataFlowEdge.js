import FlowEdge from "./FlowEdge.js";
var DataFlowEdge;
(function (DataFlowEdge) {
    class Class extends FlowEdge.Class {
    }
    DataFlowEdge.Class = Class;
    class Builder extends FlowEdge.Builder {
        constructor() {
            super(FlowEdge.Type.DATA_FLOW);
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
    DataFlowEdge.Builder = Builder;
    DataFlowEdge.TypeGuard = {
        isDataCompatible(data) {
            if (!FlowEdge.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.flowEdgeType !== FlowEdge.Type.DATA_FLOW)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!FlowEdge.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(DataFlowEdge || (DataFlowEdge = {}));
export default DataFlowEdge;
//# sourceMappingURL=DataFlowEdge.js.map