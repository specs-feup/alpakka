import InstructionNode from "../../node/instruction/InstructionNode.js";
var GotoLabelNode;
(function (GotoLabelNode) {
    class Class extends InstructionNode.Class {
        get jp() {
            return this.scratchData.$jp;
        }
    }
    GotoLabelNode.Class = Class;
    class Builder extends InstructionNode.Builder {
        constructor($jp) {
            super(InstructionNode.Type.GOTO_LABEL, $jp);
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
    GotoLabelNode.Builder = Builder;
    GotoLabelNode.TypeGuard = {
        isDataCompatible(data) {
            if (!InstructionNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.instructionFlowNodeType !== InstructionNode.Type.GOTO_LABEL)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!InstructionNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(GotoLabelNode || (GotoLabelNode = {}));
export default GotoLabelNode;
//# sourceMappingURL=GotoLabelNode.js.map