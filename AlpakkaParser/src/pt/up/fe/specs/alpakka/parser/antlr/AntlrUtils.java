package pt.up.fe.specs.alpakka.parser.antlr;

import org.antlr.runtime.tree.Tree;
import pt.up.fe.specs.alpakka.ast.SmaliNode;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.collections.AccumulatorMapL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

public class AntlrUtils {

    private long nodeCount;
    private long fileCount;
    private final AccumulatorMapL<String> histogram;

    public AntlrUtils() {
        nodeCount = 0;
        fileCount = 0;
        histogram = new AccumulatorMapL<>();
    }

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

    public void registerNode(SmaliNode root) {
        var nodes = root.getDescendantsAndSelf(SmaliNode.class);

        nodeCount += nodes.size() + 1;
        fileCount += 1;

        var currentHistogram = getHistogram(nodes, node -> node.getClass().getName()).getAccMap();
        currentHistogram.forEach(histogram::add);

        // Nodes as attributes
        var attributesNodes = new ArrayList<SmaliNode>();
        var seenNodes = new HashSet<String>();
        nodes.forEach(node -> seenNodes.add(node.get(SmaliNode.ID)));

        if (seenNodes.size() != nodes.size()) {
            System.out.println("Total AST nodes (" + nodes.size() + ") different from unique AST nodes (" + seenNodes.size() + ")");
        }

        for (var node : nodes) {
            for (var key : node.getDataKeysWithValues()) {
                var value = node.get(key);

                if (!(value instanceof SmaliNode smaliNode)) {
                    continue;
                }

                if (seenNodes.contains(smaliNode.get(SmaliNode.ID))) {
                    continue;
                }

                seenNodes.add(smaliNode.get(SmaliNode.ID));
                attributesNodes.add(smaliNode);
            }
        }

        nodeCount += attributesNodes.size();

        var attributesHistogram = getHistogram(attributesNodes, node -> "*" + node.getClass().getName()).getAccMap();
        attributesHistogram.forEach(histogram::add);
    }

    public AccumulatorMapL<String> getHistogram(Collection<? extends SmaliNode> nodes, Function<SmaliNode, String> getKey) {
        var histogram = new AccumulatorMapL<String>();
        for (var node : nodes) {
            histogram.add(getKey.apply(node));
        }

        return histogram;
    }

    public void printNodeCount() {
        System.out.println("Accumulated: " + nodeCount + " nodes, " + fileCount + " files");
    }

    public void printHistogram() {


        System.out.println("Histogram:");
        var keys = new ArrayList<>(histogram.getAccMap().keySet());
        keys.sort((key1, key2) -> -1 * Double.compare(histogram.getRatio(key1), histogram.getRatio(key2)));

        for (var key : keys) {
            System.out.println(key + ": " + histogram.getCount(key) + " (" + SpecsStrings.toPercentage(histogram.getRatio(key)) + ")");
        }

        System.out.println("\n* - Nodes found exclusively as attributes of AST nodes");
    }
}
