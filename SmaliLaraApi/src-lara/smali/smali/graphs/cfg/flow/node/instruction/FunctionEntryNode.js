import InstructionNode from "../../node/instruction/InstructionNode.js";
var FunctionEntryNode;
(function (FunctionEntryNode) {
    class Class extends InstructionNode.Class {
        get jp() {
            return this.scratchData.$jp;
        }
    }
    FunctionEntryNode.Class = Class;
    class Builder extends InstructionNode.Builder {
        constructor($jp) {
            super(InstructionNode.Type.FUNCTION_ENTRY, $jp);
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
    FunctionEntryNode.Builder = Builder;
    FunctionEntryNode.TypeGuard = {
        isDataCompatible(data) {
            if (!InstructionNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.instructionFlowNodeType !== InstructionNode.Type.FUNCTION_ENTRY)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!InstructionNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(FunctionEntryNode || (FunctionEntryNode = {}));
export default FunctionEntryNode;
//# sourceMappingURL=FunctionEntryNode.js.map