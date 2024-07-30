import ConditionNode from "./ConditionNode.js";
var IfComparisonNode;
(function (IfComparisonNode) {
    class Class extends ConditionNode.Class {
        get jp() {
            return this.scratchData.$jp;
        }
    }
    IfComparisonNode.Class = Class;
    class Builder extends ConditionNode.Builder {
        constructor(truePath, falsePath, $jp) {
            super(ConditionNode.Type.IF_COMPARISON, truePath, falsePath, $jp);
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
    IfComparisonNode.Builder = Builder;
    IfComparisonNode.TypeGuard = {
        isDataCompatible(data) {
            if (!ConditionNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.conditionFlowNodeType !== ConditionNode.Type.IF_COMPARISON)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!ConditionNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(IfComparisonNode || (IfComparisonNode = {}));
export default IfComparisonNode;
//# sourceMappingURL=IfComparisonNode.js.map