package pt.up.fe.specs.alpakka.parser.antlr;

import org.antlr.runtime.tree.Tree;

import java.util.ArrayList;
import java.util.List;

public class AntlrUtils {

    public static List<Tree> getDescendants(Tree node) {
        var descendants = new ArrayList<Tree>();
        getDescendants(node, descendants);
        return descendants;
    }

    private static void getDescendants(Tree node, List<Tree> descendants) {
        for (int i = 0; i < node.getChildCount(); i++) {
            var child = node.getChild(i);
            descendants.add(child);
            getDescendants(child, descendants);
        }
    }

}
