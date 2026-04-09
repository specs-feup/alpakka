package pt.up.fe.specs.alpakka.parser;

import org.junit.jupiter.api.Test;
import pt.up.fe.specs.alpakka.ast.context.SmaliContext;
import pt.up.fe.specs.alpakka.ast.expr.LabelRef;
import pt.up.fe.specs.alpakka.ast.stmt.Label;
import pt.up.fe.specs.alpakka.ast.stmt.PackedSwitch;
import pt.up.fe.specs.alpakka.ast.stmt.PackedSwitchCase;
import pt.up.fe.specs.alpakka.ast.stmt.PackedSwitchDirective;
import pt.up.fe.specs.alpakka.ast.stmt.Statement;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.NopStatement;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.SwitchStatement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SmaliFactoryTest {

    private SmaliContext newContext() {
        return new SmaliContext();
    }

    // -------------------------------------------------------------------------
    // Zero cases
    // -------------------------------------------------------------------------

    @Test
    void testPackedSwitch_zeroCases_structure() {
        var factory = newContext().getFactory();
        var ps = factory.packedSwitch("v0", List.of());

        assertInstanceOf(PackedSwitch.class, ps);

        var children = ps.getChildren();
        // switch stmt + data label + directive
        assertEquals(3, children.size());
        assertInstanceOf(SwitchStatement.class, children.get(0));
        assertInstanceOf(Label.class, children.get(1));
        assertInstanceOf(PackedSwitchDirective.class, children.get(2));
    }

    @Test
    void testPackedSwitch_zeroCases_directiveHasNoCaseLabelRefs() {
        var factory = newContext().getFactory();
        var ps = factory.packedSwitch("v0", List.of());

        assertEquals(0, ps.getDirective().getChildren().size());
    }

    @Test
    void testPackedSwitch_zeroCases_code() {
        var factory = newContext().getFactory();
        var ps = factory.packedSwitch("v0", List.of());
        var code = ps.getCode();

        assertTrue(code.startsWith("packed-switch v0, :pswitch_"),
                "Code should start with 'packed-switch v0, :pswitch_'");
        assertTrue(code.contains(".packed-switch 0x0"),
                "Code should contain '.packed-switch 0x0'");
        assertTrue(code.contains(".end packed-switch"),
                "Code should contain '.end packed-switch'");
    }

    // -------------------------------------------------------------------------
    // Single case
    // -------------------------------------------------------------------------

    @Test
    void testPackedSwitch_singleCase_structure() {
        var factory = newContext().getFactory();
        Statement nop = factory.nopInstructionFormat();
        var case0 = factory.packedSwitchCase(List.of(factory.label("case_0"), nop));
        var ps = factory.packedSwitch("v0", List.of(case0));

        var children = ps.getChildren();
        // switch stmt + one PackedSwitchCase + data label + directive
        assertEquals(4, children.size());
        assertInstanceOf(SwitchStatement.class, children.get(0));
        assertInstanceOf(PackedSwitchCase.class, children.get(1));
        assertInstanceOf(Label.class, children.get(2));
        assertInstanceOf(PackedSwitchDirective.class, children.get(3));
    }

    @Test
    void testPackedSwitch_singleCase_caseStructure() {
        var factory = newContext().getFactory();
        Statement nop = factory.nopInstructionFormat();
        var case0 = factory.packedSwitchCase(List.of(factory.label("case_0"), nop));
        var ps = factory.packedSwitch("v0", List.of(case0));

        var result = ps.getCases().get(0);
        assertInstanceOf(Label.class, result.getLabel());
        assertEquals(1, result.getBodyStatements().size());
        assertInstanceOf(NopStatement.class, result.getBodyStatements().get(0));
    }

    @Test
    void testPackedSwitch_singleCase_directiveHasOneLabelRef() {
        var factory = newContext().getFactory();
        Statement nop = factory.nopInstructionFormat();
        var case0 = factory.packedSwitchCase(List.of(factory.label("case_0"), nop));
        var ps = factory.packedSwitch("v0", List.of(case0));

        var directive = ps.getDirective();
        assertEquals(1, directive.getChildren().size());
        assertInstanceOf(LabelRef.class, directive.getChildren().get(0));
    }

    // -------------------------------------------------------------------------
    // Two cases
    // -------------------------------------------------------------------------

    @Test
    void testPackedSwitch_twoCases_structure() {
        var factory = newContext().getFactory();
        Statement nop1 = factory.nopInstructionFormat();
        Statement nop2 = factory.nopInstructionFormat();
        var case0 = factory.packedSwitchCase(List.of(factory.label("case_0"), nop1));
        var case1 = factory.packedSwitchCase(List.of(factory.label("case_1"), nop2));
        var ps = factory.packedSwitch("v0", List.of(case0, case1));

        var children = ps.getChildren();
        // switch stmt + two PackedSwitchCase nodes + data label + directive
        assertEquals(5, children.size());
        assertInstanceOf(SwitchStatement.class, children.get(0));
        assertInstanceOf(PackedSwitchCase.class, children.get(1));
        assertInstanceOf(PackedSwitchCase.class, children.get(2));
        assertInstanceOf(Label.class, children.get(3));
        assertInstanceOf(PackedSwitchDirective.class, children.get(4));
    }

    @Test
    void testPackedSwitch_twoCases_directiveHasTwoLabelRefs() {
        var factory = newContext().getFactory();
        Statement nop1 = factory.nopInstructionFormat();
        Statement nop2 = factory.nopInstructionFormat();
        var case0 = factory.packedSwitchCase(List.of(factory.label("case_0"), nop1));
        var case1 = factory.packedSwitchCase(List.of(factory.label("case_1"), nop2));
        var ps = factory.packedSwitch("v0", List.of(case0, case1));

        assertEquals(2, ps.getDirective().getChildren().size());
    }

    // -------------------------------------------------------------------------
    // Typed accessors on PackedSwitch
    // -------------------------------------------------------------------------

    @Test
    void testPackedSwitch_getCases_returnsCases() {
        var factory = newContext().getFactory();
        Statement nop1 = factory.nopInstructionFormat();
        Statement nop2 = factory.nopInstructionFormat();
        var case0 = factory.packedSwitchCase(List.of(factory.label("case_0"), nop1));
        var case1 = factory.packedSwitchCase(List.of(factory.label("case_1"), nop2));
        var ps = factory.packedSwitch("v0", List.of(case0, case1));

        var cases = ps.getCases();
        assertEquals(2, cases.size());
        cases.forEach(c -> assertInstanceOf(PackedSwitchCase.class, c));
    }

    @Test
    void testPackedSwitch_getDataLabel_returnsDataLabel() {
        var factory = newContext().getFactory();
        Statement nop = factory.nopInstructionFormat();
        var case0 = factory.packedSwitchCase(List.of(factory.label("case_0"), nop));
        var ps = factory.packedSwitch("v0", List.of(case0));

        var dataLabel = ps.getDataLabel();
        assertNotNull(dataLabel);
        assertTrue(dataLabel.getLabelName().endsWith("_data"),
                "Data label name should end with '_data'");
    }

    @Test
    void testPackedSwitch_getSwitchStatement_returnsSwitchInstruction() {
        var factory = newContext().getFactory();
        var ps = factory.packedSwitch("v0", List.of());

        assertInstanceOf(SwitchStatement.class, ps.getSwitchStatement());
    }

    // -------------------------------------------------------------------------
    // Label consistency
    // -------------------------------------------------------------------------

    @Test
    void testPackedSwitch_switchInstructionReferencesDataLabel() {
        var factory = newContext().getFactory();
        Statement nop = factory.nopInstructionFormat();
        var case0 = factory.packedSwitchCase(List.of(factory.label("case_0"), nop));
        var ps = factory.packedSwitch("v0", List.of(case0));

        var switchCode = ps.getSwitchStatement().getCode();
        var dataLabel = ps.getDataLabel();

        assertTrue(switchCode.contains(":" + dataLabel.getLabelName()),
                "Switch instruction should reference the data label");
    }

    @Test
    void testPackedSwitch_directiveLabelRefsMatchCaseLabels() {
        var factory = newContext().getFactory();
        Statement nop1 = factory.nopInstructionFormat();
        Statement nop2 = factory.nopInstructionFormat();
        var case0 = factory.packedSwitchCase(List.of(factory.label("case_0"), nop1));
        var case1 = factory.packedSwitchCase(List.of(factory.label("case_1"), nop2));
        var ps = factory.packedSwitch("v0", List.of(case0, case1));

        var cases = ps.getCases();
        var directive = ps.getDirective();

        var ref0 = (LabelRef) directive.getChildren().get(0);
        var ref1 = (LabelRef) directive.getChildren().get(1);

        assertEquals(cases.get(0).getLabel().getLabelName(), ref0.get(LabelRef.LABEL),
                "First directive label ref should match case 0 label");
        assertEquals(cases.get(1).getLabel().getLabelName(), ref1.get(LabelRef.LABEL),
                "Second directive label ref should match case 1 label");
    }

    // -------------------------------------------------------------------------
    // Register name is preserved
    // -------------------------------------------------------------------------

    @Test
    void testPackedSwitch_registerIsPreservedInCode() {
        var factory = newContext().getFactory();

        var psV5 = factory.packedSwitch("v5", List.<PackedSwitchCase>of());
        assertTrue(psV5.getCode().startsWith("packed-switch v5, "),
                "Register 'v5' should appear in generated code");

        var psP0 = factory.packedSwitch("p0", List.<PackedSwitchCase>of());
        assertTrue(psP0.getCode().startsWith("packed-switch p0, "),
                "Register 'p0' should appear in generated code");
    }

    // -------------------------------------------------------------------------
    // Independent packed switches get distinct prefixes
    // -------------------------------------------------------------------------

    @Test
    void testPackedSwitch_independentSwitchesHaveDistinctLabels() {
        var factory = newContext().getFactory();

        var ps1 = factory.packedSwitch("v0", List.<PackedSwitchCase>of());
        var ps2 = factory.packedSwitch("v0", List.<PackedSwitchCase>of());

        assertNotEquals(ps1.getDataLabel().getLabelName(), ps2.getDataLabel().getLabelName(),
                "Distinct packed switches should produce distinct label names");
    }
}
