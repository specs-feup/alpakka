package pt.up.fe.specs.alpakka.parser;

import java.io.File;
import java.util.ArrayList;

import pt.up.fe.specs.alpakka.parser.antlr.AlpakkaParser;
import pt.up.fe.specs.util.SpecsIo;

public class Main {

    public static void main(String[] args) {
        var filesList = new ArrayList<File>();
        filesList.add(SpecsIo.resourceCopy("pt/up/fe/specs/alpakka/HelloWorld.smali"));

        var parserOptions = new ArrayList<String>();
        parserOptions.add("-targetSdkVersion" + "20");

        var smaliRoot = new AlpakkaParser().parse(filesList, parserOptions).orElseThrow();

        System.out.println(smaliRoot.toTree());

        filesList.forEach(File::delete);
    }

}
