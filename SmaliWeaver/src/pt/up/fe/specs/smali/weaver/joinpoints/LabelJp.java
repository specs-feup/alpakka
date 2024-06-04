package pt.up.fe.specs.smali.weaver.joinpoints;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.stmt.Label;
import pt.up.fe.specs.smali.weaver.abstracts.joinpoints.ALabel;

public class LabelJp extends ALabel {

    private final Label label;

    public LabelJp(Label label) {
        super(new StatementJp(label));
        this.label = label;
    }

    @Override
    public SmaliNode getNode() {
        return this.label;
    }

    @Override
    public String getNameImpl() {
        return this.label.getLabelName();
    }
}
