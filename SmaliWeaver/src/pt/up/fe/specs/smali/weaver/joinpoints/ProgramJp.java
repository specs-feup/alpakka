package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.App;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.AProgram;

public class ProgramJp extends AProgram {

    private final App program;

    public ProgramJp(App program) {
        this.program = program;
    }

    @Override
    public SmaliNode getNode() {
        return this.program;
    }
}
