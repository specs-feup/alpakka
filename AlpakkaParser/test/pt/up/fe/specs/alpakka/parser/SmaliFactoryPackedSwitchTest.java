package pt.up.fe.specs.alpakka.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.context.SmaliContext;
import pt.up.fe.specs.alpakka.ast.stmt.Label;
import pt.up.fe.specs.alpakka.ast.stmt.Statement;

import java.util.List;
import java.util.stream.Collectors;

class SmaliFactoryPackedSwitchTest {

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private static SmaliContext ctx() {
        return new SmaliContext();
    }

    private static String joinCode(List<Statement> stmts) {
        return stmts.stream().map(SmaliNode::getCode).collect(Collectors.joining("\n"));
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    /**
     * A packed-switch with two empty cases must produce:
     * - the switch instruction
     * - one label per case
     * - the data-table label
     * - the .packed-switch directive with 0x0 start and all case label-refs
     */
    @Test
    void testPackedSwitchStructure() {
        var factory = ctx().getFactory();

        var stmts = factory.packedSwitch("p0", 2);
        var code  = joinCode(stmts);

        // instruction line
        Assertions.assertTrue(code.startsWith("packed-switch p0, :"),
                "Expected instruction at start, got:\n" + code);

        // data table
        Assertions.assertTrue(code.contains(".packed-switch 0x0"),
                "Expected packed-switch directive");
        Assertions.assertTrue(code.endsWith(".end packed-switch"),
                "Expected .end packed-switch at end");

        // each label ref inside the directive is indented with a tab
        var dataTableLines = code.lines()
                .dropWhile(l -> !l.startsWith(".packed-switch"))
                .toList();
        long labelRefLines = dataTableLines.stream()
                .filter(l -> l.startsWith("\t:"))
                .count();
        Assertions.assertEquals(2, labelRefLines,
                "Expected 2 label-ref lines inside the directive");
    }

    /**
     * Case bodies must appear between the corresponding case label and the next label.
     */
    @Test
    void testPackedSwitchCaseBodies() {
        var factory = ctx().getFactory();

        var case0 = factory.packedSwitchCase(List.of(
                factory.label("my_case_0"),
                factory.literalStmt("const/4 v0, 0x0")));
        var case1 = factory.packedSwitchCase(List.of(
                factory.label("my_case_1"),
                factory.literalStmt("const/4 v0, 0x1"),
                factory.literalStmt("goto :end")));

        var stmts = factory.packedSwitch("p0", List.of(case0, case1));
        var lines = joinCode(stmts).lines().toList();

        // Find case 0 label index
        int case0Idx = indexOfFirst(lines, l -> l.equals(":my_case_0"));
        Assertions.assertTrue(case0Idx >= 0, "Expected :my_case_0 label");
        Assertions.assertEquals("const/4 v0, 0x0", lines.get(case0Idx + 1));

        // Find case 1 label index
        int case1Idx = indexOfFirst(lines, l -> l.equals(":my_case_1"));
        Assertions.assertTrue(case1Idx >= 0, "Expected :my_case_1 label");
        Assertions.assertEquals("const/4 v0, 0x1", lines.get(case1Idx + 1));
        Assertions.assertEquals("goto :end",        lines.get(case1Idx + 2));
    }

    /**
     * Two packed-switches created by the same factory must use distinct label names.
     */
    @Test
    void testPackedSwitchLabelUniqueness() {
        var factory = ctx().getFactory();

        var stmts1 = factory.packedSwitch("p0", 2);
        var stmts2 = factory.packedSwitch("p0", 2);

        var instr1 = stmts1.get(0).getCode();
        var instr2 = stmts2.get(0).getCode();
        Assertions.assertNotEquals(instr1, instr2,
                "Two packed-switches must not share the same data-table label");
    }

    /**
     * A single-case packed-switch must have exactly one label-ref inside the directive.
     */
    @Test
    void testPackedSwitchSingleCase() {
        var factory = ctx().getFactory();

        var stmts = factory.packedSwitch("v0", 1);
        var code  = joinCode(stmts);

        long labelRefsInDirective = code.lines()
                .dropWhile(l -> !l.startsWith(".packed-switch"))
                .filter(l -> l.startsWith("\t:"))
                .count();
        Assertions.assertEquals(1, labelRefsInDirective);
    }

    /**
     * The flat statement list must contain exactly the right number of nodes:
     * 1 switch instruction + numCases case labels + 1 data label + 1 directive = numCases + 3.
     */
    @Test
    void testPackedSwitchFlatCount() {
        var factory = ctx().getFactory();

        for (int n = 1; n <= 4; n++) {
            var stmts = factory.packedSwitch("v0", n);
            Assertions.assertEquals(n + 3, stmts.size(),
                    "Expected " + (n + 3) + " statements for " + n + " cases");
        }
    }

    // -----------------------------------------------------------------------
    // Utilities
    // -----------------------------------------------------------------------

    private static int indexOfFirst(List<String> lines, java.util.function.Predicate<String> pred) {
        return indexOfFirst(lines, 0, pred);
    }

    private static int indexOfFirst(List<String> lines, int from,
                                     java.util.function.Predicate<String> pred) {
        for (int i = from; i < lines.size(); i++) {
            if (pred.test(lines.get(i))) return i;
        }
        return -1;
    }
}
