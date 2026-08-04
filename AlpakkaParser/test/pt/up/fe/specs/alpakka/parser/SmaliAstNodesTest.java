package pt.up.fe.specs.alpakka.parser;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.up.fe.specs.util.SpecsIo;

import java.io.File;

public class SmaliAstNodesTest extends SmaliAstTester {

    public SmaliAstNodesTest() {
        super("pt/up/fe/specs/alpakka/nodes/");
    }


    @BeforeAll
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
    void testEncodedEnum1() {
        testSmaliFile("EncodedEnum1.smali");
    }

    @Test
    void testFieldNode1() {
        testSmaliFile("FieldNode1.smali");
    }

    @Test
    void testFieldReference1() {
        testSmaliFile("FieldReference1.smali");
    }

    @Test
    void testInstructionFormat10x() {
        testSmaliFile("InstructionFormat10x.smali");
    }

    @Test
    void testInstructionFormat11n() {
        testSmaliFile("InstructionFormat11n.smali");
    }

    @Test
    void testInstructionFormat12x() {
        testSmaliFile("InstructionFormat12x.smali");
    }

    @Test
    void testInstructionFormat11x() {
        testSmaliFile("InstructionFormat11x.smali");
    }

    @Test
    void testLabelRef1() {
        testSmaliFile("LabelRef1.smali");
    }

    @Test
    void testPrimitiveLiteral1() {
        testSmaliFile("PrimitiveLiteral1.smali");
    }

    @Test
    void testMethodNode1() {
        testSmaliFile("MethodNode1.smali");
    }

    @Test
    void testSubannotationDirective1() {
        testSmaliFile("SubannotationDirective1.smali");
    }

    @Test
    void testPackedSwitchDirective1() {
        testSmaliFile("PackedSwitchDirective1.smali");
    }

    @Test
    void testSparseSwitchDirective1() {
        testSmaliFile("SparseSwitchDirective1.smali");
    }

    @Test
    void testInstructionFormat21cField() {
        testSmaliFile("InstructionFormat21cField.smali");
    }

    @Test
    void testInstructionFormat21cMethodType() {
        testSmaliFile("InstructionFormat21cMethodType.smali");
    }

    @Test
    void testInstructionFormat21cString() {
        testSmaliFile("InstructionFormat21cString.smali");
    }

    @Test
    void testInstructionFormat21cType() {
        testSmaliFile("InstructionFormat21cType.smali");
    }

    @Test
    void testInstructionFormat21ih() {
        testSmaliFile("InstructionFormat21ih.smali");
    }

    @Test
    void testInstructionFormat21lh() {
        testSmaliFile("InstructionFormat21lh.smali");
    }

    @Test
    void testInstructionFormat21s() {
        testSmaliFile("InstructionFormat21s.smali");
    }

    @Test
    void testInstructionFormat21t() {
        testSmaliFile("InstructionFormat21t.smali");
    }

    @Test
    void testInstructionFormat22b() {
        testSmaliFile("InstructionFormat22b.smali");
    }

    @Test
    void testInstructionFormat22cField() {
        testSmaliFile("InstructionFormat22cField.smali");
    }

    @Test
    void testInstructionFormat22cType() {
        testSmaliFile("InstructionFormat22cType.smali");
    }

    @Test
    void testInstructionFormat22s() {
        testSmaliFile("InstructionFormat22s.smali");
    }

    @Test
    void testInstructionFormat22t() {
        testSmaliFile("InstructionFormat22t.smali");
    }

    @Test
    void testInstructionFormat22x() {
        testSmaliFile("InstructionFormat22x.smali");
    }

    @Test
    void testInstructionFormat23x() {
        testSmaliFile("InstructionFormat23x.smali");
    }

    @Test
    void testInstructionFormat31c() {
        testSmaliFile("InstructionFormat31c.smali");
    }

    @Test
    void testInstructionFormat31i() {
        testSmaliFile("InstructionFormat31i.smali");
    }

    @Test
    void testInstructionFormat32x() {
        testSmaliFile("InstructionFormat32x.smali");
    }

    @Test
    void testInstructionFormat35cMethod() {
        testSmaliFile("InstructionFormat35cMethod.smali");
    }

    @Test
    void testInstructionFormat35cType() {
        testSmaliFile("InstructionFormat35cType.smali");
    }

    @Test
    void testInstructionFormat3rcMethod() {
        testSmaliFile("InstructionFormat3rcMethod.smali");
    }

    @Test
    void testInstructionFormat3rcType() {
        testSmaliFile("InstructionFormat3rcType.smali");
    }

    @Test
    void testInstructionFormat45ccMethod() {
        testSmaliFile("InstructionFormat45ccMethod.smali");
    }

    @Test
    void testInstructionFormat4rccMethod() {
        testSmaliFile("InstructionFormat4rccMethod.smali");
    }

    @Test
    void testInstructionFormat51l() {
        testSmaliFile("InstructionFormat51l.smali");
    }

}
