/**
 * Copyright 2016 SPeCS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.smali.weaver;

import larai.LaraI;
import org.lara.interpreter.joptions.config.interpreter.LaraiKeys;
import org.lara.interpreter.joptions.config.interpreter.VerboseLevel;
import org.lara.interpreter.joptions.keys.FileList;
import org.lara.interpreter.joptions.keys.OptionalFile;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.smali.weaver.options.SmaliWeaverOption;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.providers.ResourceProvider;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmaliWeaverTester {

    private static final boolean DEBUG = false;

    private static final String WORK_FOLDER = "smali_weaver_output";

    private final String basePackage;

    private boolean checkWovenCodeSyntax;
    private boolean checkExpectedOutput;
    private String srcPackage;
    private String resultPackage;
    private String resultsFile;
    private boolean run;
    private final DataStore additionalSettings;
    // private boolean debug;

    public SmaliWeaverTester(String basePackage) {
        this.basePackage = basePackage;

        this.checkWovenCodeSyntax = true;
        srcPackage = null;
        resultPackage = null;
        resultsFile = null;
        run = true;
        additionalSettings = DataStore.newInstance("Additional Settings");
        checkExpectedOutput = true;
        // debug = false;
    }

    public SmaliWeaverTester checkExpectedOutput(boolean checkExpectedOutput) {
        this.checkExpectedOutput = checkExpectedOutput;

        return this;
    }

    /**
     *
     * @param checkWovenCodeSyntax
     * @return the previous value
     */
    public SmaliWeaverTester setCheckWovenCodeSyntax(boolean checkWovenCodeSyntax) {
        this.checkWovenCodeSyntax = checkWovenCodeSyntax;

        return this;
    }



    public SmaliWeaverTester setResultPackage(String resultPackage) {
        this.resultPackage = sanitizePackage(resultPackage);

        return this;
    }

    public SmaliWeaverTester setSrcPackage(String srcPackage) {
        this.srcPackage = sanitizePackage(srcPackage);

        return this;
    }

    public SmaliWeaverTester doNotRun() {
        run = false;
        return this;
    }

    public void setResultsFile(String resultsFile) {
        this.resultsFile = resultsFile;
    }

    public <T, ET extends T> SmaliWeaverTester set(DataKey<T> key, ET value) {
        this.additionalSettings.set(key, value);

        return this;
    }

    public SmaliWeaverTester set(DataKey<Boolean> key) {
        this.additionalSettings.set(key, true);

        return this;
    }

    private String sanitizePackage(String packageName) {
        String sanitizedPackage = packageName;
        if (!sanitizedPackage.endsWith("/")) {
            sanitizedPackage += "/";
        }

        return sanitizedPackage;
    }

    private ResourceProvider buildCodeResource(String codeResourceName) {
        StringBuilder builder = new StringBuilder();

        builder.append(basePackage);
        if (srcPackage != null) {
            builder.append(srcPackage);
        }

        builder.append(codeResourceName);

        return () -> builder.toString();
    }

    public void test(String laraResource, String... codeResource) {
        test(laraResource, Arrays.asList(codeResource));
    }

    public void test(String laraResource, List<String> codeResources) {
        SpecsLogs.msgInfo("\n---- Testing '" + laraResource + "' ----\n");

        if (!run) {
            SpecsLogs.info("Ignoring test, 'run' flag is not set");
            return;
        }

        List<ResourceProvider> codes = SpecsCollections.map(codeResources, this::buildCodeResource);

        File log = runSmaliWeaver(() -> basePackage + laraResource, codes);

        // Do not check expected output
        if (!this.checkExpectedOutput) {
            return;
        }

        String logContents = SpecsIo.read(log);

        StringBuilder expectedResourceBuilder = new StringBuilder();
        expectedResourceBuilder.append(basePackage);
        if (resultPackage != null) {
            expectedResourceBuilder.append(resultPackage);
        }

        String actualResultsFile = resultsFile != null ? resultsFile : laraResource + ".txt";

        // expectedResourceBuilder.append(laraResource).append(".txt");
        expectedResourceBuilder.append(actualResultsFile);

        String expectedResource = expectedResourceBuilder.toString();
        // String expectedResource = basePackage + laraResource + ".txt";
        if (!SpecsIo.hasResource(expectedResource)) {
            SpecsLogs.msgInfo("Could not find resource '" + expectedResource
                    + "', skipping verification. Actual output:\n" + logContents);

            throw new RuntimeException("Expected outputs not found");
            // return;
        }

        assertEquals(normalize(SpecsIo.getResource(expectedResource)), normalize(logContents));
    }

    /**
     * Normalizes endlines
     *
     * @param string
     * @return
     */
    private static String normalize(String string) {
        return SpecsStrings.normalizeFileContents(string, true);
        // return string.replaceAll("\r\n", "\n");
    }

    private File runSmaliWeaver(ResourceProvider lara, List<ResourceProvider> code) {
        // Prepare folder
        File workFolder = SpecsIo.mkdir(WORK_FOLDER);
        SpecsIo.deleteFolderContents(workFolder);

        // Prepare files
        code.forEach(resource -> resource.write(workFolder));

        File laraFile = lara.write(workFolder);

        DataStore data = DataStore.newInstance("SmaliWeaverTest");

        // Set LaraI configurations
        data.add(LaraiKeys.LARA_FILE, laraFile);
        data.add(LaraiKeys.OUTPUT_FOLDER, workFolder);

        // Add workspace folder if code is not empty
        if (!code.isEmpty()) {
            data.add(LaraiKeys.WORKSPACE_FOLDER, FileList.newInstance(workFolder));
        }

        data.add(LaraiKeys.VERBOSE, VerboseLevel.errors);

        data.add(LaraiKeys.TRACE_MODE, true);
        data.add(LaraiKeys.LOG_JS_OUTPUT, Boolean.TRUE);
        data.add(LaraiKeys.LOG_FILE, OptionalFile.newInstance(getWeaverLog().getAbsolutePath()));

        // Set custom weaver configurations
        // Set SmaliWeaver configurations
        data.add(SmaliWeaverOption.TARGET_SDK, 20);

        // Add additional settings
        data.addAll(additionalSettings);

        var weaver = new SmaliWeaver();

        try {
            boolean result = LaraI.exec(data, weaver);
            // Check weaver executed correctly
            assertTrue(result);
        } catch (Exception e) {
            throw new RuntimeException("Problems during weaving", e);
        }


        return getWeaverLog();
    }

    public static File getWorkFolder() {
        return new File(WORK_FOLDER);
    }

    public static File getWeaverLog() {
        return new File(WORK_FOLDER, "test.log");
    }


    public static void clean() {
        if (DEBUG) {
            return;
        }

        // Delete weaver folder
        File workFolder = SmaliWeaverTester.getWorkFolder();
        SpecsIo.deleteFolder(workFolder);
    }

}
