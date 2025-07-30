package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

/**
 * Auto-Generated class for join point AResourceNode
 * This class is overwritten by the Weaver Generator.
 * 
 * Resource nodes, like xml files are not being handled for now
 * @author Lara Weaver Generator
 */
public abstract class AResourceNode extends ASmaliWeaverJoinPoint {

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "resourceNode";
    }
    /**
     * 
     */
    protected enum ResourceNodeAttributes {
        AST("ast"),
        CHILDREN("children"),
        CODE("code"),
        DESCENDANTS("descendants"),
        GETANCESTOR("getAncestor"),
        GETCHILD("getChild"),
        GETDESCENDANTS("getDescendants"),
        GETDESCENDANTSANDSELF("getDescendantsAndSelf"),
        ID("id"),
        PARENT("parent"),
        ROOT("root");
        private String name;

        /**
         * 
         */
        private ResourceNodeAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<ResourceNodeAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(ResourceNodeAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
