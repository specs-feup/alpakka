package pt.up.fe.specs.alpakka.parser;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.up.fe.specs.alpakka.ast.context.SmaliContext;
import pt.up.fe.specs.alpakka.parser.antlr.AlpakkaParser;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkAstTest extends SmaliAstTester {

    public ApkAstTest() {
        super("pt/up/fe/specs/alpakka/");
    }


    @BeforeAll
    @AfterAll
    static void clear() {
        File outputFolder = SpecsIo.mkdir(getOutputFoldername());
        SpecsIo.deleteFolderContents(outputFolder);
        outputFolder.delete();
    }

    @Test
    void testApk() {
        SpecsSystem.programStandardInit();
        var resourceFile = setUpResource("tiny-test.apk");
        var parserOptions = new ArrayList<String>();
        parserOptions.add("-targetSdkVersion" + "20");
        parserOptions.add("-packageFilter" + "com.example.myapplication");

        var context = new SmaliContext();
        context.set(SmaliContext.IS_DEBUG);
//        context.set(SmaliContext.CACHE_TYPES);
        var app = new AlpakkaParser(context).parse(List.of(resourceFile), parserOptions).orElseThrow();
        Assertions.assertEquals("com.example.tiny", app.getManifest().getPackageName());
    }
}
