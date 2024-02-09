package pt.up.fe.specs.smali.weaver;

import pt.up.fe.specs.lara.WeaverLauncher;
import pt.up.fe.specs.util.SpecsSystem;

public class SmaliWeaverLauncher {

    public static void main(String[] args) {

        SpecsSystem.programStandardInit();

        var isSucess = new WeaverLauncher(new SmaliWeaver()).launch(args);

    }
}
