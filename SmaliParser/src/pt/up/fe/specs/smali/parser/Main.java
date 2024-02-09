package pt.up.fe.specs.smali.parser;

import java.io.StringReader;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.Tree;
import org.jf.smali.smaliFlexLexer;
import org.jf.smali.smaliParser;

import pt.up.fe.specs.smali.parser.antlr.SmaliParser;
import pt.up.fe.specs.util.SpecsIo;

public class Main {

    public static void main(String[] args) {

        var lexer = new smaliFlexLexer(new StringReader(SpecsIo.getResource("pt/up/fe/specs/smali/HelloWorld.smali")),
                10);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new smaliParser(tokenStream);

        var antlrParser = new SmaliParser();
        var smaliRoot = antlrParser.parse(lexer, parser).orElseThrow();

        System.out.println(smaliRoot.toTree());

        /*
        try {
            var root = parser.smali_file().getTree();
        
            if (parser.getNumberOfSyntaxErrors() > 0) {
                throw new RuntimeException("Syntax errors");
            }
        
            dump(root, "", parser);
        
        } catch (RecognitionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
    }

    private static void dump(Tree node, String prefix, smaliParser parser) {
        System.out.println(prefix + parser.getTokenNames()[node.getType()]);

        for (int i = 0; i < node.getChildCount(); i++) {
            dump(node.getChild(i), prefix + " ", parser);
        }

        // TODO Auto-generated method stub

    }

}
