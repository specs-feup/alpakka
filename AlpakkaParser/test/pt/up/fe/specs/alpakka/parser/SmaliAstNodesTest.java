package pt.up.fe.specs.alpakka.parser;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import pt.up.fe.specs.util.SpecsIo;

import java.io.File;

public class SmaliAstNodesTest extends SmaliAstTester {

    public SmaliAstNodesTest() {
        super("pt/up/fe/specs/alpakka/nodes/");
    }

    @AfterAll
    static void clear() {
        File outputFolder = SpecsIo.mkdir(getOutputFoldername());
        SpecsIo.deleteFolderContents(outputFolder);
        outputFolder.delete();
    }

    @Test
    void testAnnotationDirective1() {
        testSmaliFile("AnnotationDirective1.smali");
    }

    @Test
    void testAnnotationDirective2() {
        testSmaliFile("AnnotationDirective2.smali");
    }

    @Test
    void testAnnotationDirective3() {
        testSmaliFile("AnnotationDirective3.smali");
    }

    @Test
    void testAnnotationElement1() {
        testSmaliFile("AnnotationElement1.smali");
    }

    @Test
    void testAnnotationElement2() {
        testSmaliFile("AnnotationElement2.smali");
    }

    @Test
    void testArrayDataDirective1() {
        testSmaliFile("ArrayDataDirective1.smali");
    }

    @Test
    void testCatchDirective1() {
        testSmaliFile("CatchDirective1.smali");
    }

    @Test
    void testClassNode1() {
        testSmaliFile("ClassNode1.smali");
    }

    @Test
    void testClassNode2() {
        testSmaliFile("ClassNode2.smali");
    }

    @Test
    void testClassNode3() {
        testSmaliFile("ClassNode3.smali");
    }

    @Test
    void testClassNode4() {
        testSmaliFile("ClassNode4.smali");
    }

    @Test
    void testClassNode5() {
        testSmaliFile("ClassNode5.smali");
    }

    @Test
    void testClassNode6() {
        testSmaliFile("ClassNode6.smali");
    }

    @Test
    void testClassNode7() {
        testSmaliFile("ClassNode7.smali");
    }

    @Test
    void testClassNode8() {
        testSmaliFile("ClassNode8.smali");
    }

    @Test
    void testClassNode9() {
        testSmaliFile("ClassNode9.smali");
    }

    @Test
    void testClassNode10() {
        testSmaliFile("ClassNode10.smali");
    }

    @Test
    void testClassNode11() {
        testSmaliFile("ClassNode11.smali");
    }

    @Test
    void testLabelRef1() {
        testSmaliFile("LabelRef1.smali");
    }

    @Test
    void testPrimitiveLiteral1() {
        testSmaliFile("PrimitiveLiteral1.smali");
    }
}
