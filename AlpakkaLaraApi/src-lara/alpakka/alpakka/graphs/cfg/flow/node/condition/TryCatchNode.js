import ConditionNode from "./ConditionNode.js";
var TryCatchNode;
(function (TryCatchNode) {
    class Class extends ConditionNode.Class {
        get jp() {
            return this.scratchData.$jp;
        }
    }
    TryCatchNode.Class = Class;
    class Builder extends ConditionNode.Builder {
        constructor(truePath, falsePath, $jp) {
            super(ConditionNode.Type.TRY_CATCH, truePath, falsePath, $jp);
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
    TryCatchNode.Builder = Builder;
    TryCatchNode.TypeGuard = {
        isDataCompatible(data) {
            if (!ConditionNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.conditionFlowNodeType !== ConditionNode.Type.TRY_CATCH)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!ConditionNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(TryCatchNode || (TryCatchNode = {}));
export default TryCatchNode;
//# sourceMappingURL=TryCatchNode.js.map