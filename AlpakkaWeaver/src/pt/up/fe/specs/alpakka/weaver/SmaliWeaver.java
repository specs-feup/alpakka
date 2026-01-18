package pt.up.fe.specs.alpakka.weaver;

import org.lara.interpreter.weaver.ast.AstMethods;
import org.lara.interpreter.weaver.interf.AGear;
import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.options.WeaverOption;
import org.lara.language.specification.dsl.LanguageSpecification;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.App;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.parser.antlr.AlpakkaParser;
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

    private SmaliNode root;

    public SmaliWeaver() {
        root = null;
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
    protected boolean begin(List<File> sources, File outputDir, DataStore args) {

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
     * Closes the weaver to the specified output directory location, if the weaver generates new file(s)
     *
     * @return if close was successful
     */
    @Override
    protected boolean close() {
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


    @Override
    public JoinPoint getRootJp() {
        return SmaliJoinpoints.create(root);
    }
}
