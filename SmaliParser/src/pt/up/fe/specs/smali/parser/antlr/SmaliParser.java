package pt.up.fe.specs.smali.parser.antlr;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Optional;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.jf.smali.smaliFlexLexer;
import org.jf.smali.smaliParser;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.context.SmaliContext;
import pt.up.fe.specs.util.SpecsIo;

public class SmaliParser {

    public Optional<SmaliNode> parse(File file) {
        var lexer = new smaliFlexLexer(new StringReader(SpecsIo.read(file)),
                10);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new smaliParser(tokenStream);

        var antlrParser = new SmaliParser();
        return antlrParser.parse(lexer, parser);
    }

    /**
     * Invokes the given parser rule, returning an implementation of JmmNode. If there are syntax errors,
     *
     * @param parser
     * @param ruleName
     * @return
     */
    public Optional<SmaliNode> parse(smaliFlexLexer lex, smaliParser parser) {

        // Setup listeners so that an exception is thrown where there is an error
        // lex.removeErrorListeners();
        // lex.addErrorListener(new ThrowingErrorListener());

        // parser.removeErrorListeners();
        // parser.addErrorListener(new ThrowingErrorListener());

        try {
            var root = parser.smali_file().getTree();

            if (parser.getNumberOfSyntaxErrors() > 0) {
                throw new RuntimeException("Syntax errors");
            }

            var smaliContext = new SmaliContext();

            return Optional.of(convert(root, parser, smaliContext));
        } catch (RecognitionException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        /*
        // var root = convert(node, parser);
        var root = AntlrToJmmNodeConverter.convert(node, parser);
        
        List<String> ignoreList = getIgnoreList(parser);
        if (!ignoreList.isEmpty()) {
            clean(root, ignoreList);
        }
        
        return Optional.of(root);
        */
    }

    private SmaliNode convert(Tree node, smaliParser parser, SmaliContext context) {

        var kind = parser.getTokenNames()[node.getType()];

        var children = new ArrayList<SmaliNode>();
        for (int i = 0; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i), parser, context));
        }

        var factory = context.get(SmaliContext.FACTORY);

        return factory.placeholder(kind, children);
    }

    // private static void clean(JmmNode root, List<String> ignoreList) {
    // var cleanup = new JmmNodeCleanup(ignoreList);
    // cleanup.visit(root);
    // }
    //
    // public static List<String> getIgnoreList(Parser parser) {
    // try {
    //
    // var fields = Arrays.asList(parser.getClass().getDeclaredFields())
    // .stream()
    // .filter(f -> f.getName().equals("ignoreList"))
    // .findFirst()
    // .map(f -> {
    // try {
    // f.setAccessible(true);
    // return ((String[]) f.get(parser));
    // } catch (IllegalAccessException e) {
    // return new String[] {};
    // }
    // }).orElse(new String[] {});
    //
    // return Arrays.asList(fields);
    // // if(root.)
    // } catch (Exception e) {
    //
    // // No list of ignores specified.
    // return Collections.emptyList();
    // }
    // }
    //
    // private static JmmNode convert(ParseTree node, Parser parser) {
    // // Get kind
    // var kind = getKind(node, parser);
    //
    // var jmmNode = new JmmNodeImpl(kind);
    //
    // // Get hierarchy
    // addHierarchy(jmmNode, node, parser);
    //
    // // Get attributes
    // addAttributes(jmmNode, node, parser);
    //
    // // Get children
    // addChildren(jmmNode, node, parser);
    //
    // return jmmNode;
    // }
    //
    // @SuppressWarnings("unchecked")
    // private static void addHierarchy(JmmNodeImpl jmmNode, ParseTree node, Parser parser) {
    // // If terminal node, it has no hierarchy
    // if (node instanceof TerminalNode) {
    // return;
    // }
    //
    // // If terminal node, it has no hierarchy
    // if (!(node instanceof ParserRuleContext)) {
    // System.out.println("Don't know how to handle nodes of this class: " + node.getClass());
    // return;
    // }
    //
    // // Get hierarchy
    // var classes = getNodeClasses(node);
    //
    // var hierarchy = classes.stream()
    // .map(aClass -> getKind((Class<? extends ParserRuleContext>) aClass))
    // .collect(Collectors.toList());
    //
    // jmmNode.setHierarchy(hierarchy);
    // }
    //
    // private static void addChildren(JmmNodeImpl jmmNode, ParseTree node, Parser parser) {
    //
    // for (int i = 0; i < node.getChildCount(); i++) {
    // var child = node.getChild(i);
    //
    // // Ignore terminal nodes that do not have a symbolic name
    // if (child instanceof TerminalNode) {
    // continue;
    // }
    //
    // jmmNode.add(convert(child, parser));
    // }
    // }
    //
    // private static void addAttributes(JmmNodeImpl jmmNode, ParseTree node, Parser parser) {
    //
    // // System.out.println("S NAME: " + parser.getSourceName());
    // // Add line and column
    // var startPosition = parser.getTokenStream().get(node.getSourceInterval().a);
    // var endPosition = parser.getTokenStream().get(node.getSourceInterval().b);
    //
    // jmmNode.put(NodePosition.LINE_START.getKey(), Integer.toString(startPosition.getLine()));
    // jmmNode.put(NodePosition.COL_START.getKey(), Integer.toString(startPosition.getCharPositionInLine()));
    //
    // jmmNode.put(NodePosition.LINE_END.getKey(), Integer.toString(endPosition.getLine()));
    // jmmNode.put(NodePosition.COL_END.getKey(), Integer.toString(endPosition.getCharPositionInLine()));
    //
    // if (node instanceof TerminalNode) {
    // var token = ((TerminalNode) node).getSymbol();
    // jmmNode.put("value", token.getText());
    // return;
    // }
    //
    // SpecsCheck.checkArgument(node instanceof ParserRuleContext,
    // () -> "Expected node '" + node.getClass() + "' to be an instance of " + ParserRuleContext.class);
    //
    // // Get all classes up to ParserRuleContext
    // var nodeClasses = getNodeClasses(node);
    //
    // var fields = nodeClasses.stream()
    // // Get declaring fields of node classes
    // .flatMap(nodeClass -> Arrays.asList(nodeClass.getDeclaredFields()).stream())
    // // Only those that are public
    // .filter(field -> Modifier.isPublic(field.getModifiers()))
    // .collect(Collectors.toList());
    //
    // for (var field : fields) {
    //
    // var name = field.getName();
    //
    // try {
    // // for (var field : node.getClass().getFields()) {
    // if (!field.getType().isAssignableFrom(Token.class)) {
    // var value = processValue(field.get(node));
    // jmmNode.putObject(name, value);
    // continue;
    // }
    //
    // var token = (Token) field.get(node);
    //
    // // If no token for the given field, skip
    // if (token == null) {
    // continue;
    // }
    //
    // var literalValue = token.getText();
    //
    // SpecsCheck.checkNotNull(literalValue, () -> "Could not extract value from token");
    //
    // jmmNode.put(name, literalValue);
    // } catch (IllegalAccessException e) {
    // throw new RuntimeException("Could not access field '" + name + "' from node " + node);
    // }
    // }
    //
    // }
    //
    // private static Object processValue(Object value) {
    // // If Token, convert to String
    // if (value instanceof Token) {
    // return ((Token) value).getText();
    // }
    //
    // // If List, convert elements
    // if (value instanceof List) {
    // return ((List<?>) value).stream()
    // .map(element -> processValue(element))
    // .collect(Collectors.toList());
    // }
    //
    // // Return as-is
    // return value;
    // }
    //
    // private static List<Class<?>> getNodeClasses(ParseTree node) {
    // var nodeClasses = new ArrayList<Class<?>>();
    // Class<?> currentNodeClass = node.getClass();
    // while (!currentNodeClass.equals(ParserRuleContext.class)) {
    // nodeClasses.add(currentNodeClass);
    // currentNodeClass = currentNodeClass.getSuperclass();
    // }
    // return nodeClasses;
    // }
    //
    // // private static String extractValue(Token token, Parser parser) {
    // //
    // // // We know this is safe to use in this case
    // // if (token instanceof CommonToken) {
    // // return token.getText();
    // // }
    // //
    // // // This method was being used
    // // var literalValue = parser.getVocabulary().getLiteralName(token.getType());
    // // if (literalValue == null) {
    // // literalValue = token.getText();
    // // }
    // // }
    //
    // private static String getKind(ParseTree node, Parser parser) {
    // // Tokens are terminal nodes
    // if (node instanceof TerminalNode) {
    // var token = ((TerminalNode) node).getSymbol();
    // return parser.getVocabulary().getSymbolicName(token.getType());
    // }
    //
    // if (!(node instanceof ParserRuleContext)) {
    // throw new RuntimeException("Expected node to be of class '" + ParserRuleContext.class
    // + "', but got '" + node.getClass() + "'");
    // }
    //
    // return getKind(((ParserRuleContext) node).getClass());
    // }
    //
    // private static String getKind(Class<? extends ParserRuleContext> nodeClass) {
    //
    // String className = nodeClass.getSimpleName();
    //
    // // Rules end with context
    // if (!className.endsWith("Context")) {
    // throw new RuntimeException("Expected classname to end with 'Context' " + nodeClass.getSimpleName());
    // }
    //
    // return className.substring(0, className.length() - "Context".length());
    // }
}
