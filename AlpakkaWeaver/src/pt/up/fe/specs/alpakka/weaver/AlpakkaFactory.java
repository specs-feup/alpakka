package pt.up.fe.specs.alpakka.weaver;

import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;
import pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints.AJoinPoint;

/**
 * Static factory methods for creating Alpakka AST constructs as join points, callable from TypeScript.
 */
public class AlpakkaFactory {

    /**
     * Creates the flat list of statements that make up a packed-switch construct
     * (switch instruction, case labels, data label, directive) — the same sequence
     * the parser would produce as children of a method body.
     *
     * <p>Insert them into the AST using the existing {@code insertBefore}/{@code insertAfter} actions.
     */
    public static AJoinPoint[] packedSwitch(String register, int numCases) {
        var weaver  = SmaliWeaver.getSmaliWeaver();
        var rootJp  = (ASmaliWeaverJoinPoint) weaver.getRootJp();
        var factory = rootJp.getNode().getContext().getFactory();

        return factory.packedSwitch(register, numCases).stream()
                .map(SmaliJoinpoints::create)
                .toArray(AJoinPoint[]::new);
    }
}
