package pt.up.fe.specs.alpakka.weaver;


import org.lara.interpreter.tester.WeaverTester;
import pt.up.fe.specs.alpakka.weaver.options.SmaliWeaverOption;

public class AlpakkaWeaverTester extends WeaverTester<SmaliWeaver, AlpakkaWeaverTester> {

    public AlpakkaWeaverTester(String basePackage) {
        super(SmaliWeaver.class, basePackage);

        // Set custom settings
        getCustomData().add(SmaliWeaverOption.TARGET_SDK, 20);
    }
}
