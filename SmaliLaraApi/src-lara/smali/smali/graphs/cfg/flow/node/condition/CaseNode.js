import ConditionNode from "./ConditionNode.js";
var CaseNode;
(function (CaseNode) {
    class Class extends ConditionNode.Class {
        get jp() {
            return this.scratchData.$jp;
        }
    }
    CaseNode.Class = Class;
    class Builder extends ConditionNode.Builder {
        constructor(truePath, falsePath, $jp) {
            super(ConditionNode.Type.SWITCH_CASE, truePath, falsePath, $jp);
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
    CaseNode.Builder = Builder;
    CaseNode.TypeGuard = {
        isDataCompatible(data) {
            if (!ConditionNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.conditionFlowNodeType !== ConditionNode.Type.SWITCH_CASE)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!ConditionNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(CaseNode || (CaseNode = {}));
export default CaseNode;
//# sourceMappingURL=CaseNode.js.map