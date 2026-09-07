package pt.up.fe.specs.alpakka.weaver;

import org.lara.interpreter.joptions.config.interpreter.LaraiKeys;
import org.lara.interpreter.weaver.ast.AstMethods;
import org.lara.interpreter.weaver.ast.TreeNodeAstMethods;
import org.lara.interpreter.weaver.interf.AGear;
import org.lara.interpreter.weaver.options.WeaverOption;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.App;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.alpakka.ast.context.SmaliContext;
import pt.up.fe.specs.alpakka.ast.context.SmaliFactory;
import pt.up.fe.specs.alpakka.parser.antlr.AlpakkaParser;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AJoinpoint;
import pt.up.fe.specs.alpakka.weaver.abstracts.weaver.ASmaliWeaver;
import pt.up.fe.specs.alpakka.weaver.options.SmaliWeaverOption;
import pt.up.fe.specs.alpakka.weaver.options.SmaliWeaverOptions;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Weaver Implementation for SmaliWeaver<br>
 * Since the generated abstract classes are always overwritten, their implementation should be done by extending those
 * abstract classes with user-defined classes.<br>
 * The concrete class {@link pt.up.fe.specs.alpakka.weaver.joinpoints.SmaliJoinpoint} can be used to add
 * user-defined methods and fields which the user intends to add for all join points and are not intended to be used in
 * LARA aspects.
 */
public class SmaliWeaver extends ASmaliWeaver {

    private static final String WOVEN_CODE_FOLDERNAME = "woven_code";

    public static String getWovenCodeFoldername() {
        return WOVEN_CODE_FOLDERNAME;
    }

    private App root;

    public SmaliWeaver() {
        root = null;
    }

    public App getRootNode() {
        return root;
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
        setData(args);

        var smaliFiles = new ArrayList<File>();

        sources.forEach(source -> {
            if (source.isDirectory()) {
                smaliFiles.addAll(filterSupportedFiles(Objects.requireNonNull(source.listFiles())));
            } else {
                smaliFiles.addAll(filterSupportedFiles(source));
            }
        });

        root = new AlpakkaParser().parse(smaliFiles, buildParserOptions(args))
                .orElse(new SmaliFactory(new SmaliContext()).app(App.getDefaultSdkVersion(), List.of()));

        System.out.println("SOURCES: " + sources);
        System.out.println("ARGS: " + args);

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

    /**
     * Closes the weaver to the specified output directory location, if the weaver generates new file(s)
     *
     * @return if close was successful
     */
    @Override
    protected boolean close() {

        // Output files to a "woven_code" folder inside output folder
        var outputFolder = this.dataStore.get(LaraiKeys.OUTPUT_FOLDER);
        var wovenCodeFolder = SpecsIo.mkdir(outputFolder, WOVEN_CODE_FOLDERNAME);
        SpecsLogs.info("Writing output files to folder '" + wovenCodeFolder.getAbsolutePath() + "'");

        // Write all smali files to output folder
        for (var smaliClass : root.getClasses()) {
            var outputFile = new File(wovenCodeFolder, smaliClass.getClassDescriptor().getClassName() + ".smali");
            SpecsIo.write(outputFile, smaliClass.getCode());
        }

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

    @Override
    public List<WeaverOption> getOptions() {
        return SmaliWeaverOption.STORE_DEFINITION.getKeys().stream()
                .map(SmaliWeaverOptions::getOption)
                .toList();
    }

    @Override
    public String getName() {
        return "SmaliWeaver";
    }

    @Override
    public AJoinpoint<?> getRootJp() {
        return SmaliJoinpoints.create(root, this);
    }

    @Override
    public AstMethods getAstMethods() {
        return new TreeNodeAstMethods<>(this, SmaliNode.class, node -> SmaliJoinpoints.create(node, this),
                SmaliJoinpoints::getJoinPointName, node -> node.getChildren());
    }
}
