package pt.up.fe.specs.smali.parser.antlr;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import org.jf.smali.smaliFlexLexer;
import org.jf.smali.smaliParser;

import pt.up.fe.specs.smali.ast.RegisterReference;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.context.SmaliContext;
import pt.up.fe.specs.smali.ast.type.ClassType;
import pt.up.fe.specs.smali.ast.type.MethodPrototype;
import pt.up.fe.specs.smali.ast.type.Type;
import pt.up.fe.specs.util.SpecsIo;

public class SmaliParser {

//	enum AccessSpec {
//		PUBLIC, PRIVATE, PROTECTED, STATIC, FINAL, SYNCHRONIZED, BRIDGE, VARARGS, NATIVE, ABSTRACT, STRICTFP, SYNTHETIC,
//		CONSTRUCTOR, DECLARED_SYNCHRONIZED, INTERFACE, ENUM, ANNOTATION, VOLATILE, TRANSIENT;
//
//		public static AccessSpec fromString(String access) {
//			if (AccessSpec.valueOf(normalize(access)) != null) {
//				return AccessSpec.valueOf(normalize(access));
//			}
//
//			return null;
//		}
//
//		private static String normalize(String text) {
//			return text.toUpperCase().replace('-', '_');
//		}
//
////		public static String toString(AccessSpec access) {
////			return access.toString().toLowerCase().replace('_', '-');
////		}
//	}

	private final smaliFlexLexer lex;
	private final smaliParser parser;
	private final SmaliContext context;

	private final Map<Integer, Function<Tree, SmaliNode>> converters;

	public SmaliParser(List<File> sources) {
		// sources can be a smali file, a folder or APK. Only supporting smali files for
		// now
		this.lex = new smaliFlexLexer(new StringReader(SpecsIo.read(sources.get(0))), 10);
		this.parser = new smaliParser(new CommonTokenStream(this.lex));
		this.context = new SmaliContext();
		this.converters = buildConverters();
	}

	private Map<Integer, Function<Tree, SmaliNode>> buildConverters() {
		var converters = new HashMap<Integer, Function<Tree, SmaliNode>>();

		converters.put(smaliParser.I_CLASS_DEF, this::convertClass);
		converters.put(smaliParser.I_METHOD, this::convertMethod);
		converters.put(smaliParser.I_STATEMENT_FORMAT21c_FIELD, this::convertStatementFormat21cField);
		converters.put(smaliParser.I_STATEMENT_FORMAT21c_STRING, this::convertStatementFormat21cString);
		converters.put(smaliParser.I_STATEMENT_FORMAT35c_METHOD, this::convertStatementFormat35cMethod);
		converters.put(smaliParser.I_STATEMENT_FORMAT10x, this::convertStatementFormat10x);

		return converters;
	}

	public Optional<SmaliNode> parse() {

		try {
			var root = parser.smali_file().getTree();

			if (parser.getNumberOfSyntaxErrors() > 0) {
				throw new RuntimeException("Syntax errors");
			}

			return Optional.of(convert(root));
		} catch (RecognitionException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private SmaliNode convert(Tree node) {

		var type = node.getType();

		var converter = converters.get(type);

		if (converter != null) {
			return converter.apply(node);
		}

		var kind = parser.getTokenNames()[type];

		var children = new ArrayList<SmaliNode>();
		for (int i = 0; i < node.getChildCount(); i++) {
			children.add(convert(node.getChild(i)));
		}

		var factory = context.get(SmaliContext.FACTORY);

		return factory.placeholder(kind, children);
	}

	private SmaliNode convertClass(Tree node) {
		var factory = context.get(SmaliContext.FACTORY);

		ClassType classDescriptor = null;
		var accessList = new ArrayList<String>();
		ClassType superClassDescriptor = null;
		var implementsDescriptors = new ArrayList<ClassType>();
		var source = new String();

		var children = new ArrayList<SmaliNode>();

		for (int i = 0; i < node.getChildCount(); i++) {
			switch (node.getChild(i).getType()) {
			case smaliParser.CLASS_DESCRIPTOR -> {
				classDescriptor = factory.classType(node.getChild(i).getText());
			}
			case smaliParser.I_ACCESS_LIST -> {
				for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
					accessList.add(node.getChild(i).getChild(j).getText());
				}
			}
			case smaliParser.I_SUPER -> {
				superClassDescriptor = factory.classType(node.getChild(i).getChild(0).getText());
			}
			case smaliParser.I_IMPLEMENTS -> {
				implementsDescriptors.add(factory.classType(node.getChild(i).getChild(0).getText()));
			}
			case smaliParser.I_SOURCE -> {
				source = node.getChild(i).getChild(0).getText();
			}
			case smaliParser.I_METHODS, smaliParser.I_FIELDS -> {
				for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
					children.add(convert(node.getChild(i).getChild(j)));
				}
			}
			case smaliParser.I_ANNOTATIONS -> {
				System.out.println("TODO: " + parser.getTokenNames()[node.getChild(i).getType()]);
			}
			}
		}

		return factory.classNode(classDescriptor, accessList, superClassDescriptor, implementsDescriptors, source,
				children);
	}

	private MethodPrototype convertMethodPrototype(Tree node) {
		var factory = context.get(SmaliContext.FACTORY);

		var prototypeAttributes = new HashMap<String, Object>();
		var parameters = new ArrayList<Type>();
		for (int j = 0; j < node.getChildCount(); j++) {
			if (node.getChild(j).getType() == smaliParser.I_METHOD_RETURN_TYPE) {
				// Type descriptor
				prototypeAttributes.put("returnType", factory.type(node.getChild(j).getChild(0).getText()));
			} else if (node.getChild(j).getType() == smaliParser.PARAM_LIST_OR_ID_PRIMITIVE_TYPE) {
				System.out.println("TODO: " + parser.getTokenNames()[node.getChild(j).getType()]);
			} else {
				// Non void type descriptor
				if (node.getChild(j).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
					j++;
					parameters.add(factory.arrayType(node.getChild(j).getText()));
				} else {
					parameters.add(factory.nonVoidType(node.getChild(j).getText()));
				}
			}
		}

		prototypeAttributes.put("parameters", parameters);

		return factory.methodPrototype(prototypeAttributes);
	}

	private SmaliNode convertMethod(Tree node) {
		var factory = context.get(SmaliContext.FACTORY);

		var methodAttributes = new HashMap<String, Object>();
		var accessOrRestrictionList = new ArrayList<String>();

		var children = new ArrayList<SmaliNode>();

		for (int i = 0; i < node.getChildCount(); i++) {
			switch (node.getChild(i).getType()) {
			case smaliParser.SIMPLE_NAME -> {
				methodAttributes.put("name", node.getChild(i).getText());
			}
			case smaliParser.I_METHOD_PROTOTYPE -> {
				methodAttributes.put("prototype", convertMethodPrototype(node.getChild(i)));
			}
			case smaliParser.I_ACCESS_OR_RESTRICTION_LIST -> {
				for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
					accessOrRestrictionList.add(node.getChild(i).getChild(j).getText());
				}
			}
			case smaliParser.I_REGISTERS, smaliParser.I_LOCALS -> {
				var directiveAttributes = new HashMap<String, Object>();
				directiveAttributes.put("type", parser.getTokenNames()[node.getChild(i).getType()]);
				directiveAttributes.put("value", Integer.parseInt(node.getChild(i).getChild(0).getText()));
				methodAttributes.put("registersOrLocals", factory.registersDirective(directiveAttributes));
			}
			case smaliParser.I_ORDERED_METHOD_ITEMS -> {
				for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
					children.add(convert(node.getChild(i).getChild(j)));
				}
			}
			case smaliParser.I_CATCHES, smaliParser.I_PARAMETERS, smaliParser.I_ANNOTATIONS -> {
				System.out.println("TODO: " + parser.getTokenNames()[node.getChild(i).getType()]);
			}
			}
		}

		methodAttributes.put("accessOrRestrictionList", accessOrRestrictionList);

		return factory.methodNode(methodAttributes, children);
	}

	private SmaliNode convertStatementFormat10x(Tree node) {
		var factory = context.get(SmaliContext.FACTORY);

		return factory.instructionFormat10x(node.getChild(0).getText());
	}

	private SmaliNode convertStatementFormat21cField(Tree node) {
		var factory = context.get(SmaliContext.FACTORY);

		var instruction = node.getChild(0).getText();
		var attributes = new HashMap<String, Object>();

		attributes.put("register", factory.register(node.getChild(1).getText()));

		int i = 2;
		if (node.getChild(i).getType() != smaliParser.SIMPLE_NAME) {
			if (node.getChild(i).getType() == smaliParser.CLASS_DESCRIPTOR) {
				attributes.put("referenceTypeDescriptor", factory.classType(node.getChild(i).getText()));
			} else {
				i++;
				attributes.put("referenceTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
			}
			i++;
		}

		attributes.put("memberName", node.getChild(i).getText());
		i++;

		if (node.getChild(i).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
			i++;
			attributes.put("nonVoidTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
		} else {
			attributes.put("nonVoidTypeDescriptor", factory.nonVoidType(node.getChild(i).getText()));
		}

		return factory.instructionFormat21cField(instruction, attributes);
	}

	private SmaliNode convertStatementFormat21cString(Tree node) {
		var factory = context.get(SmaliContext.FACTORY);

		var instruction = node.getChild(0).getText();
		var attributes = new HashMap<String, Object>();

		attributes.put("register", factory.register(node.getChild(1).getText()));
		attributes.put("string", node.getChild(2).getText());

		return factory.instructionFormat21cString(instruction, attributes);
	}

	private SmaliNode convertStatementFormat35cMethod(Tree node) {
		var factory = context.get(SmaliContext.FACTORY);

		var instruction = node.getChild(0).getText();
		var attributes = new HashMap<String, Object>();
		var registerList = new ArrayList<RegisterReference>();

		var rList = node.getChild(1);
		for (int i = 0; i < rList.getChildCount(); i++) {
			registerList.add(factory.register(rList.getChild(i).getText()));
		}

		attributes.put("registerList", registerList);

		int i = 2;
		if (node.getChild(i).getType() != smaliParser.SIMPLE_NAME) {
			if (node.getChild(i).getType() == smaliParser.CLASS_DESCRIPTOR) {
				attributes.put("referenceTypeDescriptor", factory.classType(node.getChild(i).getText()));
			} else {
				i++;
				attributes.put("referenceTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
			}
			i++;
		}

		attributes.put("memberName", node.getChild(i).getText());
		i++;

		attributes.put("prototype", convertMethodPrototype(node.getChild(i)));

		return factory.instructionFormat35cMethod(instruction, attributes);
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
	// private static void addHierarchy(JmmNodeImpl jmmNode, ParseTree node, Parser
	// parser) {
	// // If terminal node, it has no hierarchy
	// if (node instanceof TerminalNode) {
	// return;
	// }
	//
	// // If terminal node, it has no hierarchy
	// if (!(node instanceof ParserRuleContext)) {
	// System.out.println("Don't know how to handle nodes of this class: " +
	// node.getClass());
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
	// private static void addChildren(JmmNodeImpl jmmNode, ParseTree node, Parser
	// parser) {
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
	// private static void addAttributes(JmmNodeImpl jmmNode, ParseTree node, Parser
	// parser) {
	//
	// // System.out.println("S NAME: " + parser.getSourceName());
	// // Add line and column
	// var startPosition = parser.getTokenStream().get(node.getSourceInterval().a);
	// var endPosition = parser.getTokenStream().get(node.getSourceInterval().b);
	//
	// jmmNode.put(NodePosition.LINE_START.getKey(),
	// Integer.toString(startPosition.getLine()));
	// jmmNode.put(NodePosition.COL_START.getKey(),
	// Integer.toString(startPosition.getCharPositionInLine()));
	//
	// jmmNode.put(NodePosition.LINE_END.getKey(),
	// Integer.toString(endPosition.getLine()));
	// jmmNode.put(NodePosition.COL_END.getKey(),
	// Integer.toString(endPosition.getCharPositionInLine()));
	//
	// if (node instanceof TerminalNode) {
	// var token = ((TerminalNode) node).getSymbol();
	// jmmNode.put("value", token.getText());
	// return;
	// }
	//
	// SpecsCheck.checkArgument(node instanceof ParserRuleContext,
	// () -> "Expected node '" + node.getClass() + "' to be an instance of " +
	// ParserRuleContext.class);
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
	// SpecsCheck.checkNotNull(literalValue, () -> "Could not extract value from
	// token");
	//
	// jmmNode.put(name, literalValue);
	// } catch (IllegalAccessException e) {
	// throw new RuntimeException("Could not access field '" + name + "' from node "
	// + node);
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
	// throw new RuntimeException("Expected node to be of class '" +
	// ParserRuleContext.class
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
	// throw new RuntimeException("Expected classname to end with 'Context' " +
	// nodeClass.getSimpleName());
	// }
	//
	// return className.substring(0, className.length() - "Context".length());
	// }
}
