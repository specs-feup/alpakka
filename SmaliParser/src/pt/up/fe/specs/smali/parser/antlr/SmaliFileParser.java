package pt.up.fe.specs.smali.parser.antlr;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.smali.smaliFlexLexer;
import com.android.tools.smali.smali.smaliParser;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import pt.up.fe.specs.smali.ast.*;
import pt.up.fe.specs.smali.ast.context.SmaliContext;
import pt.up.fe.specs.smali.ast.expr.FieldReference;
import pt.up.fe.specs.smali.ast.expr.MethodReference;
import pt.up.fe.specs.smali.ast.expr.literal.Literal;
import pt.up.fe.specs.smali.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.ClassType;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;
import pt.up.fe.specs.smali.ast.stmt.LineDirective;
import pt.up.fe.specs.util.SpecsIo;

import java.io.File;
import java.io.StringReader;
import java.util.*;
import java.util.function.Function;

public class SmaliFileParser {

    private final smaliParser parser;
    private final SmaliContext context;

    private final Set<String> notImplemented;

    private final Map<Integer, Function<Tree, SmaliNode>> converters;

    private LineDirective lineDirective = null;

    private final String dexClass;

    public SmaliFileParser(File source, SmaliContext context, Integer targetSdkVersion) {
        var lex = new smaliFlexLexer(new StringReader(SpecsIo.read(source)), targetSdkVersion);
        this.parser = new smaliParser(new CommonTokenStream(lex));
        if (source.getPath().contains(File.separator))
            dexClass = source.getPath().split("\\" + File.separator)[1];
        else
            dexClass = "";
        this.context = context;
        this.converters = buildConverters();

        this.notImplemented = new HashSet<>();
    }

    private Map<Integer, Function<Tree, SmaliNode>> buildConverters() {
        var converters = new HashMap<Integer, Function<Tree, SmaliNode>>();

        converters.put(smaliParser.CLASS_DESCRIPTOR, this::convertClassDescriptor);
        converters.put(smaliParser.I_CLASS_DEF, this::convertClass);
        converters.put(smaliParser.I_FIELD, this::convertField);
        converters.put(smaliParser.I_METHOD, this::convertMethod);
        converters.put(smaliParser.I_METHOD_PROTOTYPE, this::convertMethodPrototype);
        converters.put(smaliParser.I_CATCH, this::convertCatches);
        converters.put(smaliParser.I_CATCHALL, this::convertCatches);
        converters.put(smaliParser.I_PARAMETER, this::convertParameter);
        converters.put(smaliParser.I_ANNOTATION, this::convertAnnotation);
        converters.put(smaliParser.I_ANNOTATION_ELEMENT, this::convertAnnotationElement);
        converters.put(smaliParser.I_LINE, this::convertLineDirective);
        converters.put(smaliParser.I_PROLOGUE, this::convertPrologueDirective);
        converters.put(smaliParser.I_EPILOGUE, this::convertEpilogueDirective);
        converters.put(smaliParser.I_LOCAL, this::convertLocalDirective);
        converters.put(smaliParser.I_END_LOCAL, this::convertEndLocalDirective);
        converters.put(smaliParser.I_RESTART_LOCAL, this::convertRestartLocalDirective);
        converters.put(smaliParser.I_LABEL, this::convertLabel);
        converters.put(smaliParser.I_STATEMENT_FORMAT10x, this::convertStatementFormat10x);
        converters.put(smaliParser.I_STATEMENT_FORMAT10t, this::convertGotoStatementFormat);
        converters.put(smaliParser.I_STATEMENT_FORMAT11x, this::convertStatementFormat11x);
        converters.put(smaliParser.I_STATEMENT_FORMAT11n, this::convertStatementFormat11n);
        converters.put(smaliParser.I_STATEMENT_FORMAT12x, this::convertStatementFormat12x);
        converters.put(smaliParser.I_STATEMENT_FORMAT20t, this::convertGotoStatementFormat);
        converters.put(smaliParser.I_STATEMENT_FORMAT21ih, this::convertStatementFormat21ih);
        converters.put(smaliParser.I_STATEMENT_FORMAT21lh, this::convertStatementFormat21lh);
        converters.put(smaliParser.I_STATEMENT_FORMAT21c_FIELD, this::convertStatementFormat21cField);
        converters.put(smaliParser.I_STATEMENT_FORMAT21c_STRING, this::convertStatementFormat21cString);
        converters.put(smaliParser.I_STATEMENT_FORMAT21c_TYPE, this::convertStatementFormat21cType);
        // converters.put(smaliParser.I_STATEMENT_FORMAT21c_METHOD_HANDLE, this::convertStatementFormat21cMethodHandle);
        converters.put(smaliParser.I_STATEMENT_FORMAT21c_METHOD_TYPE, this::convertStatementFormat21cMethodType);
        converters.put(smaliParser.I_STATEMENT_FORMAT21s, this::convertStatementFormat21s);
        converters.put(smaliParser.I_STATEMENT_FORMAT21t, this::convertStatementFormat21t);
        converters.put(smaliParser.I_STATEMENT_FORMAT22c_FIELD, this::convertStatementFormat22cField);
        converters.put(smaliParser.I_STATEMENT_FORMAT22c_TYPE, this::convertStatementFormat22cType);
        converters.put(smaliParser.I_STATEMENT_FORMAT22b, this::convertStatementFormat22b);
        converters.put(smaliParser.I_STATEMENT_FORMAT22s, this::convertStatementFormat22s);
        converters.put(smaliParser.I_STATEMENT_FORMAT22t, this::convertStatementFormat22t);
        converters.put(smaliParser.I_STATEMENT_FORMAT22x, this::convertStatementFormat22x);
        converters.put(smaliParser.I_STATEMENT_FORMAT23x, this::convertStatementFormat23x);
        converters.put(smaliParser.I_STATEMENT_FORMAT30t, this::convertGotoStatementFormat);
        converters.put(smaliParser.I_STATEMENT_FORMAT31c, this::convertStatementFormat31c);
        converters.put(smaliParser.I_STATEMENT_FORMAT31i, this::convertStatementFormat31i);
        converters.put(smaliParser.I_STATEMENT_FORMAT31t, this::convertStatementFormat31t);
        converters.put(smaliParser.I_STATEMENT_FORMAT32x, this::convertStatementFormat32x);
        // converters.put(smaliParser.I_STATEMENT_FORMAT35c_CALL_SITE, this::convertStatementFormat35cCallSite);
        converters.put(smaliParser.I_STATEMENT_FORMAT35c_METHOD, this::convertStatementFormat35cMethod);
        converters.put(smaliParser.I_STATEMENT_FORMAT35c_TYPE, this::convertStatementFormat35cType);
        // converters.put(smaliParser.I_STATEMENT_FORMAT3rc_CALL_SITE, this::convertStatementFormat3rcCallSite);
        converters.put(smaliParser.I_STATEMENT_FORMAT3rc_METHOD, this::convertStatementFormat3rcMethod);
        converters.put(smaliParser.I_STATEMENT_FORMAT3rc_TYPE, this::convertStatementFormat3rcType);
        converters.put(smaliParser.I_STATEMENT_FORMAT45cc_METHOD, this::convertStatementFormat45ccMethod);
        converters.put(smaliParser.I_STATEMENT_FORMAT4rcc_METHOD, this::convertStatementFormat4rccMethod);
        converters.put(smaliParser.I_STATEMENT_FORMAT51l, this::convertStatementFormat51l);
        converters.put(smaliParser.I_ENCODED_FIELD, this::convertEncodedField);
        converters.put(smaliParser.I_ENCODED_METHOD, this::convertEncodedMethod);
        converters.put(smaliParser.I_ENCODED_ARRAY, this::convertArray);
        converters.put(smaliParser.I_ENCODED_ENUM, this::convertEnum);
        converters.put(smaliParser.I_SUBANNOTATION, this::convertSubannotationDirective);
        converters.put(smaliParser.STRING_LITERAL, this::convertStringLiteral);
        converters.put(smaliParser.INTEGER_LITERAL, this::convertPrimitiveLiteral);
        converters.put(smaliParser.SHORT_LITERAL, this::convertPrimitiveLiteral);
        converters.put(smaliParser.CHAR_LITERAL, this::convertCharLiteral);
        converters.put(smaliParser.FLOAT_LITERAL, this::convertPrimitiveLiteral);
        converters.put(smaliParser.DOUBLE_LITERAL, this::convertPrimitiveLiteral);
        converters.put(smaliParser.LONG_LITERAL, this::convertPrimitiveLiteral);
        converters.put(smaliParser.BYTE_LITERAL, this::convertPrimitiveLiteral);
        converters.put(smaliParser.BOOL_LITERAL, this::convertPrimitiveLiteral);
        converters.put(smaliParser.NULL_LITERAL, this::convertNullLiteral);
        converters.put(smaliParser.REGISTER, this::convertRegisterReference);
        converters.put(smaliParser.I_REGISTER_LIST, this::convertRegisterList);
        converters.put(smaliParser.I_REGISTER_RANGE, this::convertRegisterRange);
        converters.put(smaliParser.I_STATEMENT_ARRAY_DATA, this::convertArrayDataDirective);
        converters.put(smaliParser.I_STATEMENT_PACKED_SWITCH, this::convertPackedSwitch);
        converters.put(smaliParser.I_STATEMENT_SPARSE_SWITCH, this::convertSparseSwitch);

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

        System.out.println("Not implemented: " + parser.getTokenNames()[type]);

        var kind = parser.getTokenNames()[type];

        var children = new ArrayList<SmaliNode>();
        for (int i = 0; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        var factory = context.get(SmaliContext.FACTORY);

        return factory.placeholder(kind, children);
    }

    private SmaliNode convertClassDescriptor(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        return factory.classType(node.getText());
    }

    private SmaliNode convertClass(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var accessList = new ArrayList<AccessSpec>();
        var implementsDescriptors = new ArrayList<ClassType>();

        var attributes = new HashMap<String, Object>();
        var children = new ArrayList<SmaliNode>();

        for (int i = 0; i < node.getChildCount(); i++) {
            switch (node.getChild(i).getType()) {
            case smaliParser.CLASS_DESCRIPTOR -> {
                attributes.put("classDescriptor", convert(node.getChild(i)));
            }
            case smaliParser.I_ACCESS_LIST -> {
                for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                    accessList.add(AccessSpec.getFromLabel(node.getChild(i).getChild(j).getText()));
                }
            }
            case smaliParser.I_SUPER -> {
                attributes.put("superClassDescriptor", convert(node.getChild(i).getChild(0)));
            }
            case smaliParser.I_IMPLEMENTS -> {
                implementsDescriptors.add((ClassType) convert(node.getChild(i).getChild(0)));
            }
            case smaliParser.I_SOURCE -> {
                attributes.put("source", convert(node.getChild(i).getChild(0)));
            }
            case smaliParser.I_METHODS, smaliParser.I_FIELDS, smaliParser.I_ANNOTATIONS -> {
                for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                    children.add(convert(node.getChild(i).getChild(j)));
                }
            }
            }
        }

        attributes.put("accessList", accessList);
        attributes.put("implementsDescriptors", implementsDescriptors);
        attributes.put("dexClass", dexClass);

        return factory.classNode(attributes, children);
    }

    private void todo(String todo) {
        if (!notImplemented.contains(todo)) {
            notImplemented.add(todo);
            System.out.println("TODO: " + todo);
        }
    }

    private SmaliNode convertLineDirective(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = getStatementAttributes(null);

        attributes.put("line", convert(node.getChild(0)));

        return factory.lineDirective(attributes);
    }

    private SmaliNode convertPrologueDirective(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = getStatementAttributes(null);

        return factory.prologueDirective(attributes);
    }

    private SmaliNode convertEpilogueDirective(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = getStatementAttributes(null);

        return factory.epilogueDirective(attributes);
    }

    private SmaliNode convertLocalDirective(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = getStatementAttributes(null);

        attributes.put("register", convert(node.getChild(0)));

        if (node.getChildCount() > 1) {
            attributes.put("literal", convert(node.getChild(1)));

            var i = 2;

            if (node.getChild(i).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
                i++;
                attributes.put("typeDescriptor", factory.arrayType(node.getChild(i).getText()));
            } else {
                attributes.put("typeDescriptor", factory.type(node.getChild(i).getText()));
            }

            i++;
            if (node.getChildCount() > i) {
                attributes.put("signature", convert(node.getChild(i)));
            }
        }

        return factory.localDirective(attributes);
    }

    private SmaliNode convertEndLocalDirective(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = getStatementAttributes(null);
        var children = new ArrayList<SmaliNode>();

        children.add(convert(node.getChild(0)));

        return factory.endLocalDirective(attributes, children);
    }

    private SmaliNode convertRestartLocalDirective(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = getStatementAttributes(null);
        var children = new ArrayList<SmaliNode>();

        children.add(convert(node.getChild(0)));

        return factory.restartLocalDirective(attributes, children);
    }

    private SmaliNode convertLabel(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var attributes = getStatementAttributes(null);

        attributes.put("label", node.getChild(0).getText());

        return factory.label(attributes);
    }

    private MethodPrototype convertMethodPrototype(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var prototypeAttributes = new HashMap<String, Object>();
        var parameters = new ArrayList<TypeDescriptor>();
        for (int j = 0; j < node.getChildCount(); j++) {
            if (node.getChild(j).getType() == smaliParser.I_METHOD_RETURN_TYPE) {
                // Type descriptor
                if (node.getChild(j).getChild(0).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
                    prototypeAttributes.put("returnType", factory.arrayType(node.getChild(j).getChild(1).getText()));
                } else {
                    prototypeAttributes.put("returnType", factory.type(node.getChild(j).getChild(0).getText()));
                }
            } else if (node.getChild(j).getType() == smaliParser.PARAM_LIST_OR_ID_PRIMITIVE_TYPE) {
                todo(parser.getTokenNames()[node.getChild(j).getType()]);
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

    private Modifier getAccessSpecOrHiddenApiRestriction(Tree node) {
        if (node.getType() == smaliParser.ACCESS_SPEC) {
            return AccessSpec.getFromLabel(node.getText());
        } else if (node.getType() == smaliParser.HIDDENAPI_RESTRICTION) {
            HiddenApiRestriction.getFromLabel(node.getText());
        }

        return null;
    }

    private SmaliNode convertMethod(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var methodAttributes = new HashMap<String, Object>();
        var accessOrRestrictionList = new ArrayList<Modifier>();

        var children = new ArrayList<SmaliNode>();

        for (int i = 0; i < node.getChildCount(); i++) {
            switch (node.getChild(i).getType()) {
            case smaliParser.SIMPLE_NAME -> {
                methodAttributes.put("name", node.getChild(i).getText());
            }
            case smaliParser.I_METHOD_PROTOTYPE -> {
                methodAttributes.put("prototype", convert(node.getChild(i)));
            }
            case smaliParser.I_ACCESS_OR_RESTRICTION_LIST -> {
                for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                    accessOrRestrictionList.add(getAccessSpecOrHiddenApiRestriction(node.getChild(i).getChild(j)));
                }
            }
            case smaliParser.I_REGISTERS, smaliParser.I_LOCALS -> {
                var directiveAttributes = new HashMap<String, Object>();
                directiveAttributes.put("type", parser.getTokenNames()[node.getChild(i).getType()]);
                directiveAttributes.put("value", convert(node.getChild(i).getChild(0)));

                methodAttributes.put("registersOrLocals", factory.registersDirective(directiveAttributes));
            }
            case smaliParser.I_ORDERED_METHOD_ITEMS -> {
                for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                    if (node.getChild(i).getChild(j).getType() == smaliParser.I_LINE) {
                        lineDirective = (LineDirective) convert(node.getChild(i).getChild(j));
                    } else {
                        children.add(convert(node.getChild(i).getChild(j)));
                        lineDirective = null;
                    }
                }
            }
            case smaliParser.I_CATCHES, smaliParser.I_PARAMETERS, smaliParser.I_ANNOTATIONS -> {
                for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                    children.add(convert(node.getChild(i).getChild(j)));
                }
            }
            }
        }

        methodAttributes.put("accessOrRestrictionList", accessOrRestrictionList);

        return factory.methodNode(methodAttributes, children);
    }

    private SmaliNode convertCatches(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(null);
        var children = new ArrayList<SmaliNode>();

        var i = 0;

        if (node.getChild(i).getType() != smaliParser.SIMPLE_NAME) {
            // Non void type descriptor
            if (node.getChild(i).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
                i++;
                attributes.put("nonVoidTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
            } else {
                attributes.put("nonVoidTypeDescriptor", factory.nonVoidType(node.getChild(i).getText()));
            }
            i++;
        }

        for (; i < node.getChildCount(); i++) {
            var labelRefAttributes = new HashMap<String, Object>();
            labelRefAttributes.put("label", node.getChild(i).getText());
            children.add(factory.labelRef(labelRefAttributes));
        }

        return factory.catchDirective(attributes, children);
    }

    private SmaliNode convertParameter(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(null);
        var children = new ArrayList<SmaliNode>();

        var i = 0;

        attributes.put("register", convert(node.getChild(i)));
        i++;

        if (node.getChild(i).getType() == smaliParser.STRING_LITERAL) {
            attributes.put("string", convert(node.getChild(i)));
            i++;
        }

        for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
            children.add(convert(node.getChild(i).getChild(j)));
        }

        return factory.parameterDirective(attributes, children);
    }

    private SmaliNode convertAnnotation(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(null);
        var children = new ArrayList<SmaliNode>();

        attributes.put("visibility", AnnotationVisibility.getFromString(node.getChild(0).getText()));

        var subannotation = node.getChild(1);
        attributes.put("classDescriptor", convert(subannotation.getChild(0)));

        for (int i = 1; i < subannotation.getChildCount(); i++) {
            children.add(convert(subannotation.getChild(i)));
        }

        return factory.annotationDirective(attributes, children);
    }

    private SmaliNode convertSubannotationDirective(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = new HashMap<String, Object>();
        var children = new ArrayList<SmaliNode>();

        attributes.put("classDescriptor", convert(node.getChild(0)));

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.subannotationDirective(attributes, children);
    }

    private SmaliNode convertAnnotationElement(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = new HashMap<String, Object>();

        attributes.put("name", node.getChild(0).getText());
        attributes.put("value", convert(node.getChild(1)));

        return factory.annotationElement(attributes);
    }

    private SmaliNode convertField(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var fieldAttributes = new HashMap<String, Object>();
        var accessOrRestrictionList = new ArrayList<Modifier>();

        var children = new ArrayList<SmaliNode>();

        for (int i = 0; i < node.getChildCount(); i++) {
            switch (node.getChild(i).getType()) {
            case smaliParser.I_ACCESS_OR_RESTRICTION_LIST -> {
                for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                    accessOrRestrictionList.add(getAccessSpecOrHiddenApiRestriction(node.getChild(i).getChild(j)));
                }
            }
            case smaliParser.SIMPLE_NAME -> {
                fieldAttributes.put("memberName", node.getChild(i).getText());
            }
            case smaliParser.I_FIELD_TYPE -> {
                // Non void type descriptor
                for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                    if (node.getChild(i).getChild(j).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
                        j++;
                        fieldAttributes.put("fieldType", factory.arrayType(node.getChild(i).getChild(j).getText()));
                    } else {
                        fieldAttributes.put("fieldType", factory.nonVoidType(node.getChild(i).getChild(j).getText()));
                    }
                }
            }
            case smaliParser.I_FIELD_INITIAL_VALUE -> {
                // Literal
                children.add(convert(node.getChild(i).getChild(0)));
            }
            case smaliParser.I_ANNOTATIONS -> {
                for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                    children.add(convert(node.getChild(i).getChild(j)));
                }
            }
            }
        }

        fieldAttributes.put("accessOrRestrictionList", accessOrRestrictionList);

        return factory.fieldNode(fieldAttributes, children);
    }

    private SmaliNode convertEncodedField(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var children = new ArrayList<SmaliNode>();

        children.add(convertFieldReference(node, 0));

        return factory.encodedField(children);
    }

    private SmaliNode convertEncodedMethod(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var children = new ArrayList<SmaliNode>();

        children.add(convertMethodReference(node, 0));

        return factory.encodedMethod(children);
    }

    private SmaliNode convertArray(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var children = new ArrayList<SmaliNode>();

        for (int i = 0; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        var array = factory.encodedArray(children);

        if (!children.isEmpty())
            array.setType(factory.arrayType(((Literal) children.get(0)).getType()));

        return array;
    }

    private SmaliNode convertEnum(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var children = new ArrayList<SmaliNode>();

        children.add(convertFieldReference(node, 0));

        var parsedEnum = factory.encodedEnum(children);

        if (!children.isEmpty())
            parsedEnum.setType(((FieldReference) children.get(0)).getFieldReferenceType());

        return parsedEnum;
    }

    private SmaliNode convertPrimitiveLiteral(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var literalAttributes = new HashMap<String, Object>();
        literalAttributes.put("value", node.getText());

        var literalExpr = factory.primitiveLiteral(literalAttributes);

        switch (node.getType()) {
        case smaliParser.LONG_LITERAL -> literalExpr.setType(factory.type("J"));
        case smaliParser.INTEGER_LITERAL -> literalExpr.setType(factory.type("I"));
        case smaliParser.BYTE_LITERAL -> literalExpr.setType(factory.type("B"));
        case smaliParser.BOOL_LITERAL -> literalExpr.setType(factory.type("Z"));
        case smaliParser.SHORT_LITERAL -> literalExpr.setType(factory.type("S"));
        case smaliParser.FLOAT_LITERAL -> literalExpr.setType(factory.type("F"));
        case smaliParser.DOUBLE_LITERAL -> literalExpr.setType(factory.type("D"));
        }

        return literalExpr;
    }

    private SmaliNode convertCharLiteral(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var literalAttributes = new HashMap<String, Object>();
        literalAttributes.put("value", "'" + escapeString(node.getText().substring(1, node.getText().length() - 1)) + "'");

        var literalExpr = factory.primitiveLiteral(literalAttributes);

        literalExpr.setType(factory.type("C"));

        return literalExpr;
    }

    private SmaliNode convertStringLiteral(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var literalAttributes = new HashMap<String, Object>();
        literalAttributes.put("value",
                "\"" + escapeString(node.getText().substring(1, node.getText().length() - 1)) + "\"");

        var literalExpr = factory.primitiveLiteral(literalAttributes);

        literalExpr.setType(factory.classType("Ljava/lang/String;"));

        return literalExpr;
    }

    private String escapeString(String string) {
        return string.replace("\\", "\\\\")
                .replace("\'", "\\\'")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\"", "\\\"");
    }

    private SmaliNode convertNullLiteral(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        return factory.nullLiteral();
    }

    private SmaliNode convertRegisterReference(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        return factory.register(node.getText());
    }

    private SmaliNode convertRegisterList(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var children = new ArrayList<SmaliNode>();

        for (int i = 0; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.registerList(children);
    }

    private SmaliNode convertRegisterRange(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var children = new ArrayList<SmaliNode>();

        for (int i = 0; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.registerRange(children);
    }

    private List<SmaliNode> convertFieldReferenceStatement(Tree node) {
        var children = new ArrayList<SmaliNode>();

        var i = 1;

        while (i < node.getChildCount() && node.getChild(i).getType() == smaliParser.REGISTER) {
            children.add(convert(node.getChild(i)));
            i++;
        }

        children.add(convertFieldReference(node, i));

        return children;
    }

    private SmaliNode convertFieldReference(Tree node, Integer position) {
        var factory = context.get(SmaliContext.FACTORY);

        var fieldReferenceAttributes = new HashMap<String, Object>();

        var i = position;

        if (node.getChild(i).getType() != smaliParser.SIMPLE_NAME) {
            // Reference type descriptor
            if (node.getChild(i).getType() == smaliParser.CLASS_DESCRIPTOR) {
                fieldReferenceAttributes.put("referenceTypeDescriptor", convert(node.getChild(i)));
            } else {
                i++;
                fieldReferenceAttributes.put("referenceTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
            }
            i++;
        }

        fieldReferenceAttributes.put("memberName", node.getChild(i).getText());
        i++;

        // Non void type descriptor
        if (node.getChild(i).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
            i++;
            fieldReferenceAttributes.put("nonVoidTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
        } else {
            fieldReferenceAttributes.put("nonVoidTypeDescriptor", factory.nonVoidType(node.getChild(i).getText()));
        }

        return factory.fieldReference(fieldReferenceAttributes);
    }

    private MethodReference convertMethodReference(Tree node, Integer position) {
        var factory = context.get(SmaliContext.FACTORY);
        var methodReferenceAttributes = new HashMap<String, Object>();

        int i = position;
        if (node.getChild(i).getType() != smaliParser.SIMPLE_NAME) {
            // Reference type descriptor
            if (node.getChild(i).getType() == smaliParser.CLASS_DESCRIPTOR) {
                methodReferenceAttributes.put("referenceTypeDescriptor", convert(node.getChild(i)));
            } else {
                i++;
                methodReferenceAttributes.put("referenceTypeDescriptor", factory.arrayType(node.getChild(i).getText()));
            }
            i++;
        }

        methodReferenceAttributes.put("memberName", node.getChild(i).getText());
        i++;

        methodReferenceAttributes.put("prototype", convert(node.getChild(i)));

        return factory.methodReference(methodReferenceAttributes);
    }

    private List<SmaliNode> convertTypeReferenceStatement(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var children = new ArrayList<SmaliNode>();

        var i = 1;

        while (i < node.getChildCount() && (node.getChild(i).getType() == smaliParser.REGISTER
                || node.getChild(i).getType() == smaliParser.I_REGISTER_LIST
                || node.getChild(i).getType() == smaliParser.I_REGISTER_RANGE)) {
            children.add(convert(node.getChild(i)));
            i++;
        }

        // Non void type descriptor
        if (node.getChild(i).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
            i++;
            children.add(factory.arrayType(node.getChild(i).getText()));
        } else {
            children.add(factory.nonVoidType(node.getChild(i).getText()));
        }

        return children;
    }

    private List<SmaliNode> convertLabelReferenceStatement(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        var children = new ArrayList<SmaliNode>();

        var i = 1;

        while (i < node.getChildCount() && node.getChild(i).getType() != smaliParser.SIMPLE_NAME) {
            children.add(convert(node.getChild(i)));
            i++;
        }

        var labelRefAttributes = new HashMap<String, Object>();
        labelRefAttributes.put("label", node.getChild(i).getText());

        children.add(factory.labelRef(labelRefAttributes));

        return children;
    }

    private HashMap<String, Object> getStatementAttributes(String instruction) {
        var attributes = new HashMap<String, Object>();
        attributes.put("instruction", instruction);
        attributes.put("lineDirective", lineDirective);
        return attributes;
    }

    private SmaliNode convertStatementFormat10x(Tree node) {
        var opcode = node.getChild(0).getText();

        var factory = context.get(SmaliContext.FACTORY);
        var attributes = getStatementAttributes(opcode);

        if (opcode.equals(Opcode.NOP.name)) {
            return factory.nopInstructionFormat(attributes);
        }
        
        return factory.returnInstructionFormat(attributes, new ArrayList<>());
    }

    private SmaliNode convertGotoStatementFormat(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = convertLabelReferenceStatement(node);

        return factory.gotoInstructionFormat(attributes, children);
    }

    private SmaliNode convertStatementFormat11x(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var opcode = node.getChild(0).getText();

        var attributes = getStatementAttributes(opcode);
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        if (opcode.equals(Opcode.RETURN_OBJECT.name) ||
                opcode.equals(Opcode.RETURN_WIDE.name) ||
                opcode.equals(Opcode.RETURN.name)) {
            return factory.returnInstructionFormat(attributes, children);
        } else if (opcode.equals(Opcode.THROW.name)) {
            return factory.throwInstructionFormat(attributes, children);
        }

        return factory.instructionFormat11x(attributes, children);
    }

    private SmaliNode convertStatementFormat11n(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat11n(attributes, children);
    }

    private SmaliNode convertStatementFormat12x(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat12x(attributes, children);
    }

    private SmaliNode convertStatementFormat21ih(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat21ih(attributes, children);
    }

    private SmaliNode convertStatementFormat21lh(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat21lh(attributes, children);
    }

    private SmaliNode convertStatementFormat21cField(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());

        var children = convertFieldReferenceStatement(node);

        return factory.instructionFormat21cField(attributes, children);
    }

    private SmaliNode convertStatementFormat21cString(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat21cString(attributes, children);
    }

    private SmaliNode convertStatementFormat21cType(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = convertTypeReferenceStatement(node);

        return factory.instructionFormat21cType(attributes, children);
    }

    private SmaliNode convertStatementFormat21cMethodType(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat21cMethodType(attributes, children);
    }

    private SmaliNode convertStatementFormat21s(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat21s(attributes, children);
    }

    private SmaliNode convertStatementFormat21t(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = convertLabelReferenceStatement(node);

        return factory.instructionFormat21t(attributes, children);
    }

    private SmaliNode convertStatementFormat22cField(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());

        var children = convertFieldReferenceStatement(node);

        return factory.instructionFormat22cField(attributes, children);
    }

    private SmaliNode convertStatementFormat22cType(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = convertTypeReferenceStatement(node);

        return factory.instructionFormat22cType(attributes, children);
    }

    private SmaliNode convertStatementFormat22b(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat22b(attributes, children);
    }

    private SmaliNode convertStatementFormat22s(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat22s(attributes, children);
    }

    private SmaliNode convertStatementFormat22t(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = convertLabelReferenceStatement(node);

        return factory.instructionFormat22t(attributes, children);
    }

    private SmaliNode convertStatementFormat22x(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat22x(attributes, children);
    }

    private SmaliNode convertStatementFormat23x(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat23x(attributes, children);
    }

    private SmaliNode convertStatementFormat31c(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat31c(attributes, children);
    }

    private SmaliNode convertStatementFormat31i(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat31i(attributes, children);
    }

    private SmaliNode convertStatementFormat31t(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var opcode = node.getChild(0).getText();

        var attributes = getStatementAttributes(opcode);
        var children = convertLabelReferenceStatement(node);

        if (opcode.equals(Opcode.FILL_ARRAY_DATA.name)) {
            return factory.fillArrayInstructionFormat(attributes, children);
        }
        
        return factory.switchInstructionFormat(attributes, children);
    }

    private SmaliNode convertStatementFormat32x(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat32x(attributes, children);
    }

    private SmaliNode convertStatementFormat35cType(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = convertTypeReferenceStatement(node);

        return factory.instructionFormat35cType(attributes, children);
    }

    private SmaliNode convertStatementFormat35cMethod(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        children.add(convert(node.getChild(1)));

        children.add(convertMethodReference(node, 2));

        return factory.instructionFormat35cMethod(attributes, children);
    }

    private SmaliNode convertStatementFormat3rcMethod(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        children.add(convert(node.getChild(1)));

        children.add(convertMethodReference(node, 2));

        return factory.instructionFormat3rcMethod(attributes, children);
    }

    private SmaliNode convertStatementFormat3rcType(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = convertTypeReferenceStatement(node);

        return factory.instructionFormat3rcType(attributes, children);
    }

    private SmaliNode convertStatementFormat45ccMethod(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        children.add(convert(node.getChild(1)));

        children.add(convertMethodReference(node, 2));

        children.add(convert(node.getChild(node.getChildCount() - 1)));

        return factory.instructionFormat45ccMethod(attributes, children);
    }

    private SmaliNode convertStatementFormat4rccMethod(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        children.add(convert(node.getChild(1)));

        children.add(convertMethodReference(node, 2));

        children.add(convert(node.getChild(node.getChildCount() - 1)));

        return factory.instructionFormat4rccMethod(attributes, children);
    }

    private SmaliNode convertStatementFormat51l(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(node.getChild(0).getText());
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return factory.instructionFormat51l(attributes, children);
    }

    private SmaliNode convertArrayDataDirective(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(null);
        var children = new ArrayList<SmaliNode>();

        attributes.put("elementWidth", convert(node.getChild(0).getChild(0)));

        var arrayElements = node.getChild(1);

        for (int i = 0; i < arrayElements.getChildCount(); i++) {
            children.add(convert(arrayElements.getChild(i)));
        }

        return factory.arrayDataDirective(attributes, children);
    }

    private SmaliNode convertPackedSwitch(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(null);
        var children = new ArrayList<SmaliNode>();

        attributes.put("key", convert(node.getChild(0).getChild(0)));

        var packedSwitchElements = node.getChild(1);

        for (int i = 0; i < packedSwitchElements.getChildCount(); i++) {
            var labelRefAttributes = new HashMap<String, Object>();
            labelRefAttributes.put("label", packedSwitchElements.getChild(i).getText());

            children.add(factory.labelRef(labelRefAttributes));
        }

        return factory.packedSwitchDirective(attributes, children);
    }

    private SmaliNode convertSparseSwitch(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(null);
        var children = new ArrayList<SmaliNode>();
        var sparseSwitchElements = node.getChild(0);

        for (int i = 0; i < sparseSwitchElements.getChildCount(); i += 2) {
            var elementChildren = new ArrayList<SmaliNode>();

            elementChildren.add(convert(sparseSwitchElements.getChild(i)));

            var labelRefAttributes = new HashMap<String, Object>();
            labelRefAttributes.put("label", sparseSwitchElements.getChild(i + 1).getText());

            elementChildren.add(factory.labelRef(labelRefAttributes));

            children.add(factory.sparseSwitchElement(elementChildren));
        }

        return factory.sparseSwitchDirective(attributes, children);
    }

}
