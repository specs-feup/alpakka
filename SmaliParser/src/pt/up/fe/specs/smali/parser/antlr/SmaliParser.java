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

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.context.SmaliContext;
import pt.up.fe.specs.smali.ast.expr.RegisterReference;
import pt.up.fe.specs.smali.ast.type.ClassType;
import pt.up.fe.specs.smali.ast.type.MethodPrototype;
import pt.up.fe.specs.smali.ast.type.Type;
import pt.up.fe.specs.util.SpecsIo;

public class SmaliParser {

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

		var children = new ArrayList<SmaliNode>();

		var fieldReferenceAttributes = new HashMap<String, Object>();

		children.add(factory.register(node.getChild(1).getText()));

		int i = 2;
		if (node.getChild(i).getType() != smaliParser.SIMPLE_NAME) {
			if (node.getChild(i).getType() == smaliParser.CLASS_DESCRIPTOR) {
				fieldReferenceAttributes.put("referenceTypeDescriptor", factory.classType(node.getChild(i).getText()));
			} else {
				i++;
				fieldReferenceAttributes.put("referenceTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
			}
			i++;
		}

		fieldReferenceAttributes.put("memberName", node.getChild(i).getText());
		i++;

		if (node.getChild(i).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
			i++;
			fieldReferenceAttributes.put("nonVoidTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
		} else {
			fieldReferenceAttributes.put("nonVoidTypeDescriptor", factory.nonVoidType(node.getChild(i).getText()));
		}

		children.add(factory.fieldReference(fieldReferenceAttributes));

		return factory.instructionFormat21cField(instruction, children);
	}

	private SmaliNode convertStatementFormat21cString(Tree node) {
		var factory = context.get(SmaliContext.FACTORY);

		var instruction = node.getChild(0).getText();
		var children = new ArrayList<SmaliNode>();

		children.add(factory.register(node.getChild(1).getText()));
		System.out.println("TODO: " + parser.getTokenNames()[node.getChild(2).getType()]);

		return factory.instructionFormat21cString(instruction, children);
	}

	private SmaliNode convertStatementFormat35cMethod(Tree node) {
		var factory = context.get(SmaliContext.FACTORY);

		var instruction = node.getChild(0).getText();
		var children = new ArrayList<SmaliNode>();

		var registerListChildren = new ArrayList<RegisterReference>();
		var methodReferenceAttributes = new HashMap<String, Object>();

		var rList = node.getChild(1);
		for (int i = 0; i < rList.getChildCount(); i++) {
			registerListChildren.add(factory.register(rList.getChild(i).getText()));
		}

		children.add(factory.registerList(registerListChildren));

		int i = 2;
		if (node.getChild(i).getType() != smaliParser.SIMPLE_NAME) {
			if (node.getChild(i).getType() == smaliParser.CLASS_DESCRIPTOR) {
				methodReferenceAttributes.put("referenceTypeDescriptor", factory.classType(node.getChild(i).getText()));
			} else {
				i++;
				methodReferenceAttributes.put("referenceTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
			}
			i++;
		}

		methodReferenceAttributes.put("memberName", node.getChild(i).getText());
		i++;

		methodReferenceAttributes.put("prototype", convertMethodPrototype(node.getChild(i)));

		children.add(factory.methodReference(methodReferenceAttributes));

		return factory.instructionFormat35cMethod(instruction, children);
	}

}
