package pt.up.fe.specs.alpakka.weaver;

import org.lara.langspec2.dsl.WeaverSpec;

/**
 * Weaver specification for the Alpakka (Smali) weaver, translated from the XML
 * specification files (joinPointModel.xml, artifacts.xml and actionModel.xml)
 * into the Java DSL.
 * <p>
 * The WeaverGen2 prefix is 'Smali', so concrete join point classes are named
 * {@code Smali<JpName>} (e.g., {@code SmaliProgram}); the LARA-visible join
 * point names are unchanged with respect to the XML model (e.g. 'program').
 */
public class SmaliSpec extends WeaverSpec {

    @Override
    public void define() {
        weaverPrefix("Smali");
        packageName("pt.up.fe.specs.alpakka.weaver");
        rootJoinPoint("program");

        // =====================================================================
        // Global attributes and actions (weaver-specific, not in BaseJoinPointSpec)
        // Excludes base contract: dump, joinPointType, node, self, children,
        // descendants, scopeNodes, parent, code, line, column, toString,
        // equals, compareNodes, same, instanceOf, insert
        // =====================================================================

        global()
                .attribute("root", jpRef("program"), "Returns the 'program' joinpoint")
                .attribute("getAncestor")
                    .tooltip("Looks for an ancestor joinpoint name, walking back on the AST")
                    .param("type", STRING)
                    .returns(jpRef("joinpoint"))
                .attribute("getDescendants")
                    .tooltip("Retrieves the descendants of the given type")
                    .param("type", STRING)
                    .returns(array(jpRef("joinpoint")))
                .attribute("getDescendantsAndSelf")
                    .tooltip("Retrieves the descendants of the given type, including the node itself")
                    .param("type", STRING)
                    .returns(array(jpRef("joinpoint")))
                .attribute("getChild")
                    .tooltip("Returns the child of the node at the given index, ignoring null nodes")
                    .param("index", INT)
                    .returns(jpRef("joinpoint"))
                .attribute("id", STRING, "The id of the node")
                .attribute("ast", STRING, "String representation of the ast")
                .action("replaceWith")
                    .tooltip("Replaces this node with the given node")
                    .param("node", jpRef("joinpoint"))
                    .returns(jpRef("joinpoint"))
                .action("replaceWith")
                    .tooltip("Overload which accepts a string")
                    .param("node", STRING)
                    .returns(jpRef("joinpoint"))
                .action("replaceWith")
                    .tooltip("Overload which accepts a list of join points")
                    .param("node", array(jpRef("joinpoint")))
                    .returns(jpRef("joinpoint"))
                .action("replaceWithStrings")
                    .tooltip("Overload which accepts a list of strings")
                    .param("node", array(STRING))
                    .returns(jpRef("joinpoint"))
                .action("insertBefore")
                    .tooltip("Inserts the given join point before this join point")
                    .param("node", jpRef("joinpoint"))
                    .returns(jpRef("joinpoint"))
                .action("insertBefore")
                    .tooltip("Overload which accepts a string")
                    .param("node", STRING)
                    .returns(jpRef("joinpoint"))
                .action("insertAfter")
                    .tooltip("Inserts the given join point after this join point")
                    .param("node", jpRef("joinpoint"))
                    .returns(jpRef("joinpoint"))
                .action("insertAfter")
                    .tooltip("Overload which accepts a string")
                    .param("code", STRING)
                    .returns(jpRef("joinpoint"))
                .action("detach")
                    .tooltip("Removes the node associated to this joinpoint from the AST")
                    .returns(jpRef("joinpoint"));

        // =====================================================================
        // Join point definitions
        // =====================================================================

        joinPoint("placeholder")
                .tooltip("Placeholder node")
                .attribute("kind", STRING);

        joinPoint("resourceNode")
                .tooltip("Resource nodes, like xml files are not being handled for now");

        joinPoint("program")
                .tooltip("App node")
                .attribute("manifest", jpRef("manifest"))
                .attribute("classes", array(jpRef("classNode")))
                .action("buildApk")
                    .param("outputName", STRING)
                    .returns(VOID);

        joinPoint("manifest")
                .tooltip("The application's manifest")
                .attribute("packageName", STRING)
                .attribute("activities", array(STRING))
                .attribute("services", array(STRING));

        joinPoint("classNode")
                .tooltip("Class definition")
                .attribute("methods", array(jpRef("methodNode")))
                .attribute("fields", array(jpRef("fieldNode")))
                .attribute("classDescriptor", jpRef("classType"))
                .attribute("superClassDescriptor", jpRef("classType"));

        joinPoint("fieldNode")
                .tooltip("Field definition")
                .attribute("name", STRING)
                .attribute("referenceName", STRING)
                .attribute("isStatic", BOOLEAN);

        joinPoint("methodNode")
                .tooltip("Method definition")
                .attribute("name", STRING)
                .attribute("referenceName", STRING)
                .attribute("prototype", jpRef("methodPrototype"))
                .attribute("registersDirective", jpRef("registersDirective"))
                .attribute("isStatic", BOOLEAN);

        joinPoint("expression")
                .tooltip("Expression");

        joinPoint("registerReference")
                .tooltip("Register reference")
                .extending("expression");

        joinPoint("labelReference")
                .tooltip("Label reference")
                .attribute("decl", jpRef("label"));

        joinPoint("methodReference")
                .tooltip("Method reference")
                .attribute("parentClassDescriptor", jpRef("typeDescriptor"))
                .attribute("name", STRING)
                .attribute("prototype", jpRef("methodPrototype"));

        joinPoint("fieldReference")
                .tooltip("Field reference")
                .extending("expression");

        joinPoint("sparseSwitchElement")
                .tooltip("Sparse switch element")
                .attribute("label", jpRef("labelReference"));

        joinPoint("registerList")
                .tooltip("Register list");

        joinPoint("registerRange")
                .tooltip("Register range");

        joinPoint("literal")
                .tooltip("Literal")
                .extending("expression");

        joinPoint("primitiveLiteral")
                .tooltip("Primitive literal")
                .extending("literal")
                .attribute("setValue")
                    .param("value", STRING)
                    .returns(STRING);

        joinPoint("typeDescriptor")
                .tooltip("Type descriptor");

        joinPoint("classType")
                .tooltip("Class descriptor")
                .extending("typeDescriptor")
                .attribute("className", STRING)
                .attribute("packageName", STRING)
                .attribute("decl", jpRef("classNode"));

        joinPoint("arrayType")
                .tooltip("Array descriptor")
                .extending("typeDescriptor");

        joinPoint("primitiveType")
                .tooltip("Primitive descriptor")
                .extending("typeDescriptor");

        joinPoint("methodPrototype")
                .tooltip("Method prototype")
                .extending("typeDescriptor")
                .attribute("parameters", array(jpRef("typeDescriptor")))
                .attribute("returnType", jpRef("typeDescriptor"));

        joinPoint("statement")
                .tooltip("Statement")
                .attribute("nextStatement", jpRef("statement"))
                .attribute("prevStatement", jpRef("statement"));
                // NOTE: the old XML model had a statement attribute 'line' of type
                // 'lineDirective'. The base join point contract (BaseJoinPointSpec)
                // already declares a global attribute 'line' (Integer, the source
                // line), which would clash in Java, so the base contract wins and
                // 'line' now returns the (undefined) source line number. The
                // 'lineDirective' join point is still reachable through 'children'.

        joinPoint("lineDirective")
                .tooltip("Line directive")
                .extending("statement")
                // LineDirective.getValue() returns a primitive int, never null
                .attribute("value", INT);

        joinPoint("label")
                .tooltip("Label declaration")
                .extending("statement")
                .attribute("name", STRING);

        joinPoint("catch")
                .tooltip("Catch directive")
                .extending("statement")
                .attribute("exception", jpRef("typeDescriptor"))
                .attribute("tryStart", jpRef("labelReference"))
                .attribute("tryEnd", jpRef("labelReference"))
                .attribute("catch", jpRef("labelReference"));

        joinPoint("registersDirective")
                .tooltip("Registers directive")
                .extending("statement")
                .attribute("type", STRING)
                // RegistersDirective.getValue() returns Integer, can be null
                .attribute("value", INTEGER);

        joinPoint("instruction")
                .tooltip("Instruction")
                .extending("statement")
                .attribute("canThrow", BOOLEAN)
                .attribute("setsResult", BOOLEAN)
                .attribute("setsRegister", BOOLEAN)
                .attribute("opCodeName", STRING);

        joinPoint("ifComparison")
                .tooltip("Smali instruction format 22t")
                .extending("instruction")
                .attribute("label", jpRef("labelReference"));

        joinPoint("ifComparisonWithZero")
                .tooltip("Smali instruction format 21t")
                .extending("instruction")
                .attribute("label", jpRef("labelReference"));

        joinPoint("goto")
                .tooltip("Smali instruction formats 10t, 20t, 30t")
                .extending("instruction")
                .attribute("label", jpRef("labelReference"));

        joinPoint("switch")
                .tooltip("Smali instruction format 31t")
                .extending("instruction");

        joinPoint("sparseSwitch")
                .tooltip("Sparse switch directive")
                .extending("statement");

        joinPoint("packedSwitch")
                .tooltip("Packed switch directive")
                .extending("statement");

        joinPoint("returnStatement")
                .tooltip("Smali instruction formats 10x, 11x")
                .extending("instruction");

        joinPoint("throwStatement")
                .tooltip("Smali instruction format 11x")
                .extending("instruction");

        joinPoint("binaryOp")
                .tooltip("Smali instruction formats 12x, 22b, 22s, 23x")
                .extending("instruction")
                .action("setOperator")
                    .tooltip("Sets the operator of this instruction")
                    .param("operation", STRING)
                    .returns(VOID);
    }
}
