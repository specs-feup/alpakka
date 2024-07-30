package pt.up.fe.specs.alpakka.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.alpakka.parser.antlr.AlpakkaParser;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;

class SmaliAstTest {

    static final String OUTPUT_FOLDERNAME = "temp-smali-ast";

    File setUpResource(String resource) {

        // Copy resources under test
        File outputFolder = SpecsIo.mkdir(SmaliAstTest.OUTPUT_FOLDERNAME);
        File copiedFile = SpecsIo.resourceCopy(resource, outputFolder, false, true);
        Assertions.assertTrue(copiedFile.isFile(), "Could not copy resource '" + resource + "'");
        return copiedFile;
    }

    @AfterAll
    static void clear() {
        File outputFolder = SpecsIo.mkdir(SmaliAstTest.OUTPUT_FOLDERNAME);
        SpecsIo.deleteFolderContents(outputFolder);
        outputFolder.delete();
    }

    @Test
    void testHelloWorld() {
        var file = setUpResource("pt/up/fe/specs/alpakka/HelloWorld.smali");

        testSmaliFile(file);
    }

    @Test
    void testBracketedMemberNames() {
        var file = setUpResource("pt/up/fe/specs/alpakka/BracketedMemberNames.smali");

        testSmaliFile(file);
    }

    void testSmaliFile(File resourceFile) {

        var parserOptions = new ArrayList<String>();
        parserOptions.add("-targetSdkVersion" + "20");

        var smaliRoot = new AlpakkaParser().parse(List.of(resourceFile), parserOptions).orElseThrow();

        var directory = SpecsIo.mkdir(SmaliAstTest.OUTPUT_FOLDERNAME + "/outputFirst");
        SpecsIo.write(new File(directory, resourceFile.getName()), smaliRoot.getChildren().get(0).getCode());

        // Parse output again, check if files are the same
        File firstOutput = new File(directory, resourceFile.getName());

        var smaliRoot2 = new AlpakkaParser().parse(List.of(firstOutput), parserOptions).orElseThrow();

        var secondDirectory = SpecsIo.mkdir(SmaliAstTest.OUTPUT_FOLDERNAME + "/outputSecond");
        SpecsIo.write(new File(secondDirectory, resourceFile.getName()), smaliRoot2.getChildren().get(0).getCode());

        Map<String, File> outputFiles1 = SpecsIo.getFiles(new File(SmaliAstTest.OUTPUT_FOLDERNAME + "/outputFirst"))
                .stream().collect(Collectors.toMap(File::getName, file -> file));

        Map<String, File> outputFiles2 = SpecsIo.getFiles(new File(SmaliAstTest.OUTPUT_FOLDERNAME + "/outputSecond"))
                .stream().collect(Collectors.toMap(File::getName, file -> file));

        for (String name : outputFiles1.keySet()) {
            // Get corresponding file in output 2
            File outputFile2 = outputFiles2.get(name);

            Assertions.assertNotNull(outputFile2, "Could not find second version of file '" + name + "'");

        }

        // Compare with .txt, if available
        String txtResource = "pt/up/fe/specs/alpakka/" + resourceFile.getName() + ".txt";

        if (SpecsIo.hasResource(txtResource)) {
            String txtContents = SpecsStrings.normalizeFileContents(SpecsIo.getResource(txtResource), true);

            File generatedFile = outputFiles2.get(resourceFile.getName());
            String generatedFileContents = SpecsStrings.normalizeFileContents(SpecsIo.read(generatedFile), true);

            Assertions.assertEquals(txtContents, generatedFileContents);
        }

        testIdempotence(outputFiles1, outputFiles2);
    }

    void testIdempotence(Map<String, File> outputFiles1, Map<String, File> outputFiles2) {
        for (String name : outputFiles1.keySet()) {
            // Get corresponding file in output 1
            var outputFile1 = outputFiles1.get(name);

            // Get corresponding file in output 2
            var outputFile2 = outputFiles2.get(name);

            var normalizedFile1 = SpecsStrings.normalizeFileContents(SpecsIo.read(outputFile1), true);
            var normalizedFile2 = SpecsStrings.normalizeFileContents(SpecsIo.read(outputFile2), true);

            Assertions.assertEquals(normalizedFile1, normalizedFile2);
        }
    }

}
