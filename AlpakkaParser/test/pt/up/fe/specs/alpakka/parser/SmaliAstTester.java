package pt.up.fe.specs.alpakka.parser;

import org.junit.jupiter.api.Assertions;
import pt.up.fe.specs.alpakka.parser.antlr.AlpakkaParser;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SmaliAstTester {

    private static final String OUTPUT_FOLDERNAME = "temp-smali-ast";

    private final String basePackage;
    //private final String outputFoldername;

    public SmaliAstTester(String basePackage) {
        this.basePackage = basePackage;
        //this.outputFoldername = outputFoldername;
    }

    public static String getOutputFoldername() {
        return OUTPUT_FOLDERNAME;
    }

    private String getOutputFoldername(String resourceName) {
        return OUTPUT_FOLDERNAME + "/" + resourceName + "/";
    }

    public File setUpResource(String resourceName) {
        return setUpResource(basePackage, resourceName);
    }

    File setUpResource(String basePackage, String resourceName) {

        var resource = basePackage + resourceName;

        // Copy resources under test
        File outputFolder = SpecsIo.mkdir(getOutputFoldername(resourceName));
        File copiedFile = SpecsIo.resourceCopy(resource, outputFolder, false, true);
        Assertions.assertTrue(copiedFile.isFile(), "Could not copy resource '" + resource + "'");
        return copiedFile;
    }

    void testSmaliFile(String resourceName) {
        var file = setUpResource(basePackage, resourceName);

        testSmaliFile(file);
    }

    void testSmaliFile(File resourceFile) {

        var parserOptions = new ArrayList<String>();
        parserOptions.add("-targetSdkVersion" + "20");

        var smaliRoot = new AlpakkaParser().parse(List.of(resourceFile), parserOptions).orElseThrow();

        var directory = SpecsIo.mkdir(getOutputFoldername(resourceFile.getName()) + "/outputFirst");
        SpecsIo.write(new File(directory, resourceFile.getName()), smaliRoot.getChildren().get(0).getCode());
        System.out.println("CODE:\n" + smaliRoot.getChildren().get(0).getCode());
        // Parse output again, check if files are the same
        File firstOutput = new File(directory, resourceFile.getName());

        var smaliRoot2 = new AlpakkaParser().parse(List.of(firstOutput), parserOptions).orElseThrow();
        System.out.println("RESOURCE: " + resourceFile.getName());
        System.out.println("SECOND OUTPUT: " + smaliRoot2.getChildren().get(0).getCode());
        var secondDirectory = SpecsIo.mkdir(getOutputFoldername(resourceFile.getName()) + "/outputSecond");
        SpecsIo.write(new File(secondDirectory, resourceFile.getName()), smaliRoot2.getChildren().get(0).getCode());

        Map<String, File> outputFiles1 = SpecsIo.getFiles(new File(getOutputFoldername(resourceFile.getName()) + "/outputFirst"))
                .stream().collect(Collectors.toMap(File::getName, file -> file));

        Map<String, File> outputFiles2 = SpecsIo.getFiles(new File(getOutputFoldername(resourceFile.getName()) + "/outputSecond"))
                .stream().collect(Collectors.toMap(File::getName, file -> file));

        for (String name : outputFiles1.keySet()) {
            // Get corresponding file in output 2
            File outputFile2 = outputFiles2.get(name);

            Assertions.assertNotNull(outputFile2, "Could not find second version of file '" + name + "'");

        }

        // Compare with .txt, if available
        String txtResource = basePackage + resourceFile.getName() + ".txt";

        if (SpecsIo.hasResource(txtResource)) {
            String txtContents = SpecsStrings.normalizeFileContents(SpecsIo.getResource(txtResource), true);

            File generatedFile = outputFiles2.get(resourceFile.getName());
            String generatedFileContents = SpecsStrings.normalizeFileContents(SpecsIo.read(generatedFile), true);

            Assertions.assertEquals(txtContents, generatedFileContents);
        } else {
            File generatedFile = outputFiles2.get(resourceFile.getName());
            System.out.println("Could not find expected '" + txtResource + "' with contents:\n" + SpecsIo.read(generatedFile));
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
