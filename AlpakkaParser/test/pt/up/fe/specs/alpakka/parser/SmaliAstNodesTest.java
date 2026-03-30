package pt.up.fe.specs.alpakka.parser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pt.up.fe.specs.util.SpecsIo;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class SmaliAstNodesTest extends SmaliAstTester {
    private static final String BASE_PACKAGE = "pt/up/fe/specs/alpakka/nodes/";
    private static final String FIXTURES_RESOURCE = "pt/up/fe/specs/alpakka/nodes";
    public SmaliAstNodesTest() {
        super(BASE_PACKAGE);
    }
    @BeforeAll
    @AfterAll
    static void clear() {
        File outputFolder = SpecsIo.mkdir(getOutputFoldername());
        SpecsIo.deleteFolderContents(outputFolder);
        outputFolder.delete();
    }
    @Test
    void fixturesHaveMatchingExpectedOutputs() {
        Set<String> smaliFixtures = listFixtureNames(name -> name.endsWith(".smali"))
                .collect(Collectors.toSet());
        Assertions.assertFalse(smaliFixtures.isEmpty(), "Could not find any SMALI fixtures in '" + FIXTURES_RESOURCE + "'");
        Set<String> expectedOutputs = listFixtureNames(name -> name.endsWith(".smali.txt"))
                .map(name -> name.substring(0, name.length() - ".txt".length()))
                .collect(Collectors.toSet());
        Assertions.assertEquals(smaliFixtures, expectedOutputs,
                "The fixture inventory is out of sync: every '.smali' fixture should have exactly one matching '.txt' expected output");
    }
    @ParameterizedTest(name = "{0}")
    @MethodSource("smaliFixtures")
    void testSmaliFixture(String resourceName) {
        testSmaliFile(resourceName);
    }
    private static Stream<String> smaliFixtures() {
        return listFixtureNames(name -> name.endsWith(".smali"));
    }
    private static Stream<String> listFixtureNames(Predicate<String> filter) {
        Path fixturesFolder = getFixturesFolder();
        try {
            return Files.list(fixturesFolder)
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(filter)
                    .sorted();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not list SMALI fixtures under '" + fixturesFolder + "'", e);
        }
    }
    private static Path getFixturesFolder() {
        try {
            var resource = SmaliAstNodesTest.class.getClassLoader().getResource(FIXTURES_RESOURCE);
            Assertions.assertNotNull(resource, "Could not find fixture folder '" + FIXTURES_RESOURCE + "'");
            return Path.of(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Could not resolve fixture folder '" + FIXTURES_RESOURCE + "'", e);
        }
    }
}
