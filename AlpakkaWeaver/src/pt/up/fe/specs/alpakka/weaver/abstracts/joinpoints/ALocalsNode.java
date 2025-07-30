package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;

/**
 * Auto-Generated class for join point ALocalsNode
 * This class is overwritten by the Weaver Generator.
 * 
 * Locals node
 * @author Lara Weaver Generator
 */
public abstract class ALocalsNode extends ASmaliWeaverJoinPoint {

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "localsNode";
    }
    /**
     * 
     */
    protected enum LocalsNodeAttributes {
        ID("id"),
        AST("ast"),
        CODE("code");
        private String name;

        /**
         * 
         */
        private LocalsNodeAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<LocalsNodeAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(LocalsNodeAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
