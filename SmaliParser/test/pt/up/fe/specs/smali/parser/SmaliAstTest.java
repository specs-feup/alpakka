package pt.up.fe.specs.smali.parser;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.smali.parser.antlr.SmaliParser;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;

class SmaliAstTest {

    private static final String OUTPUT_FOLDERNAME = "temp-smali-ast";

    File setUpResource(String resource) throws Exception {

        // Copy resources under test
        File outputFolder = SpecsIo.mkdir(SmaliAstTest.OUTPUT_FOLDERNAME);
        File copiedFile = SpecsIo.resourceCopy(resource, outputFolder, false, true);
        Assert.assertTrue("Could not copy resource '" + resource + "'", copiedFile.isFile());

        return copiedFile;
    }

    @AfterAll
    static void clear() throws Exception {
        File outputFolder = SpecsIo.mkdir(SmaliAstTest.OUTPUT_FOLDERNAME);
        SpecsIo.deleteFolderContents(outputFolder);
        outputFolder.delete();
    }

    @Test
    void testHelloWorld() throws Exception {
        var file = setUpResource("pt/up/fe/specs/smali/HelloWorld.smali");

        testSmaliFile(file);
    }

    @Test
    void testBracketedMemberNames() throws Exception {
        var file = setUpResource("pt/up/fe/specs/smali/BracketedMemberNames.smali");

        testSmaliFile(file);
    }

    void testSmaliFile(File resourceFile) throws Exception {

        var smaliRoot = new SmaliParser().parse(List.of(resourceFile)).orElseThrow();

        var directory = SpecsIo.mkdir(SmaliAstTest.OUTPUT_FOLDERNAME + "/outputFirst");
        SpecsIo.write(new File(directory, resourceFile.getName()), smaliRoot.getCode());

        // Parse output again, check if files are the same
        File firstOutput = new File(directory, resourceFile.getName());

        var smaliRoot2 = new SmaliParser().parse(List.of(firstOutput)).orElseThrow();

        var secondDirectory = SpecsIo.mkdir(SmaliAstTest.OUTPUT_FOLDERNAME + "/outputSecond");
        SpecsIo.write(new File(secondDirectory, resourceFile.getName()), smaliRoot2.getCode());

        Map<String, File> outputFiles1 = SpecsIo.getFiles(new File(SmaliAstTest.OUTPUT_FOLDERNAME + "/outputFirst"))
                .stream().collect(Collectors.toMap(file -> file.getName(), file -> file));

        Map<String, File> outputFiles2 = SpecsIo.getFiles(new File(SmaliAstTest.OUTPUT_FOLDERNAME + "/outputSecond"))
                .stream().collect(Collectors.toMap(file -> file.getName(), file -> file));

        for (String name : outputFiles1.keySet()) {
            // Get corresponding file in output 2
            File outputFile2 = outputFiles2.get(name);

            Assert.assertNotNull("Could not find second version of file '" + name + "'", outputFile2);

        }

        // Compare with .txt, if available
        String txtResource = "pt/up/fe/specs/smali/" + resourceFile.getName() + ".txt";

        if (SpecsIo.hasResource(txtResource)) {
            String txtContents = SpecsStrings.normalizeFileContents(SpecsIo.getResource(txtResource), true);

            File generatedFile = outputFiles2.get(resourceFile.getName());
            String generatedFileContents = SpecsStrings.normalizeFileContents(SpecsIo.read(generatedFile), true);

            Assert.assertEquals(txtContents, generatedFileContents);
        }

        testIdempotence(outputFiles1, outputFiles2);
    }

    private void testIdempotence(Map<String, File> outputFiles1, Map<String, File> outputFiles2) {
        for (String name : outputFiles1.keySet()) {
            // Get corresponding file in output 1
            var outputFile1 = outputFiles1.get(name);

            // Get corresponding file in output 2
            var outputFile2 = outputFiles2.get(name);

            var normalizedFile1 = SpecsStrings.normalizeFileContents(SpecsIo.read(outputFile1), true);
            var normalizedFile2 = SpecsStrings.normalizeFileContents(SpecsIo.read(outputFile2), true);

            Assert.assertEquals(normalizedFile1, normalizedFile2);
        }
    }

}
