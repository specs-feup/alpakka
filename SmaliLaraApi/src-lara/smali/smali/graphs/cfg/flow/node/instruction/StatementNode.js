import InstructionNode from "../../node/instruction/InstructionNode.js";
var StatementNode;
(function (StatementNode) {
    class Class extends InstructionNode.Class {
        get jp() {
            return this.scratchData.$jp;
        }
    }
    StatementNode.Class = Class;
    class Builder extends InstructionNode.Builder {
        constructor($jp) {
            super(InstructionNode.Type.STATEMENT, $jp);
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
    StatementNode.Builder = Builder;
    StatementNode.TypeGuard = {
        isDataCompatible(data) {
            if (!InstructionNode.TypeGuard.isDataCompatible(data))
                return false;
            const d = data;
            if (d.instructionFlowNodeType !== InstructionNode.Type.STATEMENT)
                return false;
            return true;
        },
        isScratchDataCompatible(scratchData) {
            if (!InstructionNode.TypeGuard.isScratchDataCompatible(scratchData))
                return false;
            return true;
        },
    };
})(StatementNode || (StatementNode = {}));
export default StatementNode;
//# sourceMappingURL=StatementNode.js.map