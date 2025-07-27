package pt.up.fe.specs.alpakka.weaver;

import org.lara.interpreter.weaver.ast.AstMethods;
import org.lara.interpreter.weaver.interf.AGear;
import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.options.WeaverOption;
import org.lara.interpreter.weaver.utils.LaraResourceProvider;
import org.lara.language.specification.dsl.LanguageSpecification;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.App;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.parser.antlr.AlpakkaParser;
import pt.up.fe.specs.alpakka.parser.antlr.SmaliFileParser;
import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;
import pt.up.fe.specs.alpakka.weaver.abstracts.weaver.ASmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.options.SmaliWeaverOption;
import pt.up.fe.specs.alpakka.weaver.options.SmaliWeaverOptions;

import java.io.File;
import java.util.*;

/**
 * Weaver Implementation for SmaliWeaver<br>
 * Since the generated abstract classes are always overwritten, their implementation should be done by extending those
 * abstract classes with user-defined classes.<br>
 * The abstract class {@link ASmaliWeaverJoinPoint} can be used to add
 * user-defined methods and fields which the user intends to add for all join points and are not intended to be used in
 * LARA aspects.
 *
 * @author Lara Weaver Generator
 */
public class SmaliWeaver extends ASmaliWeaver {

    private static final String WOVEN_CODE_FOLDERNAME = "woven_code";

    public static String getWovenCodeFoldername() {
        return WOVEN_CODE_FOLDERNAME;
    }


    /**
     * @return
     */
    public static LanguageSpecification buildLanguageSpecification() {
        return LanguageSpecification.newInstance(() -> "smali/weaverspecs/joinPointModel.xml",
                () -> "smali/weaverspecs/artifacts.xml", () -> "smali/weaverspecs/actionModel.xml");
    }

    private static final String ALPAKKA_API_NAME = "@specs-feup/alpakka";
    // private static final List<ResourceProvider> SMALI_LARA_API = new ArrayList<>();
    // static {
    // SMALI_LARA_API.addAll(AlpakkaLaraApis.getApis());
    // }

    private final SmaliFileParser parser;
    private SmaliNode root;

    public SmaliWeaver() {
        parser = null;
        root = null;
    }

    // @Override
    // protected void addWeaverApis() {
    // addApis(SMALI_API_NAME, SMALI_LARA_API);
    // }

    @Override
    public String getWeaverApiName() {
        return ALPAKKA_API_NAME;
    }

    /**
     * Warns the lara interpreter if the weaver accepts a folder as the application or only one file at a time.
     *
     * @return true if the weaver is able to work with several files, false if only works with one file
     */
    @Override
    public boolean handlesApplicationFolder() {
        // Can the weaver handle an application folder?
        return true;
    }

    private List<File> filterSupportedFiles(File... sources) {
        var supportedFiles = new ArrayList<File>();

        for (var source : sources) {
            if (source.getName().endsWith(".smali") || source.getName().endsWith(".apk")) {
                supportedFiles.add(source);
            }
        }

        return supportedFiles;
    }

    /**
     * Set a file/folder in the weaver if it is valid file/folder type for the weaver.
     *
     * @param sources   the file with the source code
     * @param outputDir output directory for the generated file(s)
     * @param args      arguments to start the weaver
     * @return true if the file type is valid
     */
    @Override
    public boolean begin(List<File> sources, File outputDir, DataStore args) {

        var smaliFiles = new ArrayList<File>();

        sources.forEach(source -> {
            if (source.isDirectory()) {
                smaliFiles.addAll(filterSupportedFiles(Objects.requireNonNull(source.listFiles())));
            } else {
                smaliFiles.addAll(filterSupportedFiles(source));
            }
        });

        root = new AlpakkaParser().parse(smaliFiles, buildParserOptions(args))
                .orElse(new App(DataStore.newInstance(App.class), List.of()));

        System.out.println("SOURCES: " + sources);
        System.out.println("ARGS: " + args);
        // Initialize weaver with the input file/folder
        // throw new UnsupportedOperationException("Method begin for SmaliWeaver is not
        // yet implemented");
        return true;
    }

    private List<String> buildParserOptions(DataStore args) {
        var options = new ArrayList<String>();

        var targetSdkVersion = args.get(SmaliWeaverOption.TARGET_SDK);
        options.add("-targetSdkVersion" + targetSdkVersion);

        var packageFilter = args.get(SmaliWeaverOption.PACKAGE_FILTER);
        options.add("-packageFilter" + packageFilter);

        return options;
    }

    @Override
    public AstMethods getAstMethods() {
        // TODO Auto-generated method stub
        return super.getAstMethods();
    }

    /**
     * Return a JoinPoint instance of the language root, i.e., an instance of APlaceholder
     *
     * @return an instance of the join point root/program
     */
    @Override
    public JoinPoint select() {
        // return new <APlaceholder implementation>;
        // throw new UnsupportedOperationException("Method select for SmaliWeaver is not
        // yet implemented");
        return null;
    }

    /**
     * Closes the weaver to the specified output directory location, if the weaver generates new file(s)
     *
     * @return if close was successful
     */
    @Override
    public boolean close() {
        // Terminate weaver execution with final steps required and writing output files
        // throw new UnsupportedOperationException("Method close for SmaliWeaver is not
        // yet implemented");
        return true;
    }

    /**
     * Returns a list of Gears associated to this weaver engine
     *
     * @return a list of implementations of {@link AGear} or null if no gears are available
     */
    @Override
    public List<AGear> getGears() {
        return Collections.emptyList(); // i.e., no gears currently being used
    }

    /**
     * Returns Weaving Engine as a SmaliWeaver
     */
    public static SmaliWeaver getSmaliWeaver() {
        return (SmaliWeaver) getThreadLocalWeaver();
    }

    @Override
    public List<WeaverOption> getOptions() {
        return SmaliWeaverOption.STORE_DEFINITION.getKeys().stream()
                .map(SmaliWeaverOptions::getOption)
                .toList();
    }

    @Override
    protected LanguageSpecification buildLangSpecs() {
        return buildLanguageSpecification();
    }

    @Override
    public String getName() {
        return "SmaliWeaver";
    }

//    @Override
//    public List<ResourceProvider> getAspectsAPI() {
//        return SMALI_LARA_API;
//    }

    @Override
    protected List<LaraResourceProvider> getWeaverNpmResources() {
        return Arrays.asList(AlpakkaApiJsResource.values());
    }

    @Override
    public JoinPoint getRootJp() {
        return SmaliJoinpoints.create(root);
    }
}
