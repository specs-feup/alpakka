import BaseEdge from "../../graph/BaseEdge.js";
var FlowEdge;
(function (FlowEdge) {
    class Class extends BaseEdge.Class {
    }
    FlowEdge.Class = Class;
    class Builder extends BaseEdge.Builder {
        #flowEdgeType;
        constructor(type) {
            super();
            this.#flowEdgeType = type;
        }
        buildData(data) {
            return {
                ...super.buildData(data),
                flowEdgeType: this.#flowEdgeType,
            };
        }
        buildScratchData(scratchData) {
            return {
                ...super.buildScratchData(scratchData),
            };
        }
    }
    FlowEdge.Builder = Builder;
    FlowEdge.TypeGuard = {
        isDataCompatible(data) {
            if (!BaseEdge.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (!Object.values(Type).includes(d.flowEdgeType))
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!BaseEdge.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
    // ------------------------------------------------------------
    let Type;
    (function (Type) {
        Type["CONTROL_FLOW"] = "control_flow";
        Type["DATA_FLOW"] = "data_flow";
    })(Type = FlowEdge.Type || (FlowEdge.Type = {}));
})(FlowEdge || (FlowEdge = {}));
export default FlowEdge;
//# sourceMappingURL=FlowEdge.js.map