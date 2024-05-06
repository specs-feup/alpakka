package pt.up.fe.specs.smali.weaver;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lara.interpreter.weaver.ast.AstMethods;
import org.lara.interpreter.weaver.interf.AGear;
import org.lara.interpreter.weaver.interf.JoinPoint;
import org.lara.interpreter.weaver.options.WeaverOption;
import org.lara.interpreter.weaver.utils.LaraResourceProvider;
import org.lara.language.specification.LanguageSpecification;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.App;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.parser.antlr.SmaliFileParser;
import pt.up.fe.specs.smali.parser.antlr.SmaliParser;
import pt.up.fe.specs.smali.weaver.abstracts.weaver.ASmaliWeaver;
import pt.up.fe.specs.smali.weaver.options.SmaliWeaverOption;
import pt.up.fe.specs.smali.weaver.options.SmaliWeaverOptions;

/**
 * Weaver Implementation for SmaliWeaver<br>
 * Since the generated abstract classes are always overwritten, their implementation should be done by extending those
 * abstract classes with user-defined classes.<br>
 * The abstract class {@link pt.up.fe.specs.smali.weaver.abstracts.ASmaliWeaverJoinPoint} can be used to add
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
     * Thread-scope WeaverEngine
     */
    // private static final SpecsThreadLocal<WeaverEngine> THREAD_LOCAL_WEAVER = new
    // SpecsThreadLocal<>(
    // WeaverEngine.class);

    /**
     * @deprecated
     * @return
     */
    @Deprecated
    public static LanguageSpecification buildLanguageSpecificationOld() {
        return LanguageSpecification.newInstance(() -> "smali/weaverspecs/joinPointModel.xml",
                () -> "smali/weaverspecs/artifacts.xml", () -> "smali/weaverspecs/actionModel.xml", true);
    }

    private static final String SMALI_API_NAME = "smali-js";
    // private static final List<ResourceProvider> SMALI_LARA_API = new ArrayList<>();
    // static {
    // SMALI_LARA_API.addAll(SmaliLaraApis.getApis());
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
        return SMALI_API_NAME;
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

    /**
     * Set a file/folder in the weaver if it is valid file/folder type for the weaver.
     * 
     * @param sources
     *            the file with the source code
     * @param outputDir
     *            output directory for the generated file(s)
     * @param args
     *            arguments to start the weaver
     * @return true if the file type is valid
     */
    @Override
    public boolean begin(List<File> sources, File outputDir, DataStore args) {

        // sources can be a smali file, a folder or APK. Only supporting smali files for
        // now

        var targetSdkVersion = args.get(SmaliWeaverOption.TARGET_SDK);

        root = new SmaliParser().parse(sources, targetSdkVersion)
                .orElse(new App(DataStore.newInstance(App.class), List.of()));

        System.out.println("SOURCES: " + sources);
        System.out.println("ARGS: " + args);
        // Initialize weaver with the input file/folder
        // throw new UnsupportedOperationException("Method begin for SmaliWeaver is not
        // yet implemented");
        return true;
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
    public LanguageSpecification getLanguageSpecification() {
        return buildLanguageSpecificationOld();
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
        return Arrays.asList(SmaliApiJsResource.values());
    }

    @Override
    public JoinPoint getRootJp() {
        return SmaliJoinpoints.create(root);
    }
}
