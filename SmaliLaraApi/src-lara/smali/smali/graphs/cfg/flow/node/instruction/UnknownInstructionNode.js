import InstructionNode from "../../node/instruction/InstructionNode.js";
var UnknownInstructionNode;
(function (UnknownInstructionNode) {
    class Class extends InstructionNode.Class {
    }
    UnknownInstructionNode.Class = Class;
    class Builder extends InstructionNode.Builder {
        constructor($jp) {
            super(InstructionNode.Type.UNKNOWN, $jp);
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
    UnknownInstructionNode.Builder = Builder;
    UnknownInstructionNode.TypeGuard = {
        isDataCompatible(data) {
            if (!InstructionNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.instructionFlowNodeType !== InstructionNode.Type.UNKNOWN)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!InstructionNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(UnknownInstructionNode || (UnknownInstructionNode = {}));
export default UnknownInstructionNode;
//# sourceMappingURL=UnknownInstructionNode.js.map