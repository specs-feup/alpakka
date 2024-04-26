import InstructionNode from "../../node/instruction/InstructionNode.js";
var ReturnNode;
(function (ReturnNode) {
    class Class extends InstructionNode.Class {
        get jp() {
            return this.scratchData.$jp;
        }
    }
    ReturnNode.Class = Class;
    class Builder extends InstructionNode.Builder {
        constructor($jp) {
            super(InstructionNode.Type.RETURN, $jp);
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
    ReturnNode.Builder = Builder;
    ReturnNode.TypeGuard = {
        isDataCompatible(data) {
            if (!InstructionNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.instructionFlowNodeType !== InstructionNode.Type.RETURN)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!InstructionNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(ReturnNode || (ReturnNode = {}));
export default ReturnNode;
//# sourceMappingURL=ReturnNode.js.map