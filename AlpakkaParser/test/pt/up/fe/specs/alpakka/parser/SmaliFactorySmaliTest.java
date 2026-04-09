package pt.up.fe.specs.alpakka.parser;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.up.fe.specs.util.SpecsIo;

import java.io.File;

public class SmaliFactorySmaliTest extends SmaliAstTester {

    public SmaliFactorySmaliTest() {
        super("pt/up/fe/specs/alpakka/factory/");
    }

    @BeforeAll
    @AfterAll
    static void clear() {
        File outputFolder = SpecsIo.mkdir(getOutputFoldername());
        SpecsIo.deleteFolderContents(outputFolder);
        outputFolder.delete();
    }

    @Test
    void testPackedSwitchFactory_zeroCases() {
        testSmaliFile("PackedSwitchFactory_zeroCases.smali");
    }

    @Test
    void testPackedSwitchFactory_oneCase() {
        testSmaliFile("PackedSwitchFactory_oneCase.smali");
    }

    @Test
    void testPackedSwitchFactory_twoCases() {
        testSmaliFile("PackedSwitchFactory_twoCases.smali");
    }

    @Test
    void testPackedSwitchFactory_threeCases() {
        testSmaliFile("PackedSwitchFactory_threeCases.smali");
    }

    @Test
    void testPackedSwitchFactory_registerP0() {
        testSmaliFile("PackedSwitchFactory_registerP0.smali");
    }

    @Test
    void testPackedSwitchFactory_emptyCases() {
        testSmaliFile("PackedSwitchFactory_emptyCases.smali");
    }
}
