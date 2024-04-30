import InstructionNode from "./InstructionNode.js";
var LabelNode;
(function (LabelNode) {
    class Class extends InstructionNode.Class {
        get jp() {
            return this.scratchData.$jp;
        }
    }
    LabelNode.Class = Class;
    class Builder extends InstructionNode.Builder {
        constructor($jp) {
            super(InstructionNode.Type.LABEL, $jp);
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
    LabelNode.Builder = Builder;
    LabelNode.TypeGuard = {
        isDataCompatible(data) {
            if (!InstructionNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.instructionFlowNodeType !== InstructionNode.Type.LABEL)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!InstructionNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(LabelNode || (LabelNode = {}));
export default LabelNode;
//# sourceMappingURL=LabelNode.js.map