package pt.up.fe.specs.alpakka.parser.antlr;

import com.android.tools.smali.dexlib2.Opcode;
import com.android.tools.smali.smali.smaliFlexLexer;
import com.android.tools.smali.smali.smaliParser;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.Tree;
import pt.up.fe.specs.alpakka.ast.*;
import pt.up.fe.specs.alpakka.ast.context.SmaliContext;
import pt.up.fe.specs.alpakka.ast.expr.*;
import pt.up.fe.specs.alpakka.ast.expr.literal.EncodedArray;
import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;
import pt.up.fe.specs.alpakka.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.ClassType;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;
import pt.up.fe.specs.alpakka.ast.stmt.LineDirective;
import pt.up.fe.specs.alpakka.ast.stmt.Statement;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.*;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

import java.io.File;
import java.io.StringReader;
import java.util.*;
import java.util.function.Function;

public class SmaliFileParser {

    private static final Map<Opcode, Class<? extends Instruction>> OPCODE_TO_INSTRUCTION;

    static {
        OPCODE_TO_INSTRUCTION = new HashMap<>();

        OPCODE_TO_INSTRUCTION.put(Opcode.RETURN_OBJECT, ReturnStatement.class);
        OPCODE_TO_INSTRUCTION.put(Opcode.RETURN_WIDE, ReturnStatement.class);
        OPCODE_TO_INSTRUCTION.put(Opcode.RETURN, ReturnStatement.class);
        OPCODE_TO_INSTRUCTION.put(Opcode.THROW, ThrowStatement.class);
        OPCODE_TO_INSTRUCTION.put(Opcode.NOP, NopStatement.class);
        OPCODE_TO_INSTRUCTION.put(Opcode.RETURN_VOID, ReturnStatement.class);
        OPCODE_TO_INSTRUCTION.put(Opcode.FILL_ARRAY_DATA, FillArrayStatement.class);
    }


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
        converters.put(smaliParser.I_STATEMENT_FORMAT10x, this::convertInstruction);
        converters.put(smaliParser.I_STATEMENT_FORMAT10t, node -> convertInstruction(node, this::convertLabelReferenceStatement, GotoStatement.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT11x, node -> convertInstruction(node, InstructionFormat11x.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT11n, node -> convertInstruction(node, InstructionFormat11n.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT12x, node -> convertInstruction(node, InstructionFormat12x.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT20t, node -> convertInstruction(node, this::convertLabelReferenceStatement, GotoStatement.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT21ih, node -> convertInstruction(node, InstructionFormat21ih.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT21lh, node -> convertInstruction(node, InstructionFormat21lh.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT21c_FIELD, node -> convertInstruction(node, this::convertFieldReferenceStatement, InstructionFormat21cField.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT21c_STRING, node -> convertInstruction(node, InstructionFormat21cString.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT21c_TYPE, node -> convertInstruction(node, this::convertTypeReferenceStatement, InstructionFormat21cType.class));
        // converters.put(smaliParser.I_STATEMENT_FORMAT21c_METHOD_HANDLE, this::convertStatementFormat21cMethodHandle);
        converters.put(smaliParser.I_STATEMENT_FORMAT21c_METHOD_TYPE, node -> convertInstruction(node, InstructionFormat21cMethodType.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT21s, node -> convertInstruction(node, InstructionFormat21s.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT21t, node -> convertInstruction(node, this::convertLabelReferenceStatement, InstructionFormat21t.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT22c_FIELD, node -> convertInstruction(node, this::convertFieldReferenceStatement, InstructionFormat22cField.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT22c_TYPE, node -> convertInstruction(node, this::convertTypeReferenceStatement, InstructionFormat22cType.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT22b, node -> convertInstruction(node, InstructionFormat22b.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT22s, node -> convertInstruction(node, InstructionFormat22s.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT22t, node -> convertInstruction(node, this::convertLabelReferenceStatement, InstructionFormat22t.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT22x, node -> convertInstruction(node, InstructionFormat22x.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT23x, node -> convertInstruction(node, InstructionFormat23x.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT30t, node -> convertInstruction(node, this::convertLabelReferenceStatement, GotoStatement.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT31c, node -> convertInstruction(node, InstructionFormat31c.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT31i, node -> convertInstruction(node, InstructionFormat31i.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT31t, node -> convertInstruction(node, this::convertLabelReferenceStatement, SwitchStatement.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT32x, node -> convertInstruction(node, InstructionFormat32x.class));
        // converters.put(smaliParser.I_STATEMENT_FORMAT35c_CALL_SITE, this::convertStatementFormat35cCallSite);
        converters.put(smaliParser.I_STATEMENT_FORMAT35c_METHOD, node -> convertInstruction(node, this::convertMethodChildren, InstructionFormat35cMethod.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT35c_TYPE, node -> convertInstruction(node, this::convertTypeReferenceStatement, InstructionFormat35cType.class));
        // converters.put(smaliParser.I_STATEMENT_FORMAT3rc_CALL_SITE, this::convertStatementFormat3rcCallSite);
        converters.put(smaliParser.I_STATEMENT_FORMAT3rc_METHOD, node -> convertInstruction(node, this::convertMethodChildren, InstructionFormat3rcMethod.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT3rc_TYPE, node -> convertInstruction(node, this::convertTypeReferenceStatement, InstructionFormat3rcType.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT45cc_METHOD, node -> convertInstruction(node, this::convertFormat4Children, InstructionFormat45ccMethod.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT4rcc_METHOD, node -> convertInstruction(node, this::convertFormat4Children, InstructionFormat4rccMethod.class));
        converters.put(smaliParser.I_STATEMENT_FORMAT51l, node -> convertInstruction(node, InstructionFormat51l.class));
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
        converters.put(smaliParser.VOID_TYPE, this::convertVoidType);
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
            var smaliNode = converter.apply(node);
            processSmaliNode(smaliNode);
            return smaliNode;
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

    /**
     * Post-process node.
     *
     * @param smaliNode
     */
    private void processSmaliNode(SmaliNode smaliNode) {
        if (smaliNode instanceof Statement && lineDirective != null) {
            smaliNode.set(Statement.LINE_DIRECTIVE, Optional.of(lineDirective));

        }
    }


    private List<SmaliNode> convertInstructionChildren(Tree node) {
        var children = new ArrayList<SmaliNode>();

        for (int i = 1; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        return children;
    }

    private Opcode getOpcode(Tree node) {
        var opcodeName = node.getChild(0).getText();
        return SmaliNode.getOpcode(opcodeName);
    }

    private SmaliNode convertClassDescriptor(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);
        return factory.classType(node.getText());
    }

    private SmaliNode convertClass(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var accessList = new ArrayList<AccessSpec>();
        var implementsDescriptors = new ArrayList<ClassType>();

        var children = new ArrayList<SmaliNode>();

        ClassType classDescriptor = null;
        ClassType superDescriptor = null;
        String source = null;

        for (int i = 0; i < node.getChildCount(); i++) {
            switch (node.getChild(i).getType()) {
                case smaliParser.CLASS_DESCRIPTOR -> {
                    classDescriptor = (ClassType) convert(node.getChild(i));
                }
                case smaliParser.I_ACCESS_LIST -> {
                    for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                        accessList.add(AccessSpec.getFromLabel(node.getChild(i).getChild(j).getText()));
                    }
                }
                case smaliParser.I_SUPER -> {
                    superDescriptor = (ClassType) convert(node.getChild(i).getChild(0));
                }
                case smaliParser.I_IMPLEMENTS -> {
                    implementsDescriptors.add((ClassType) convert(node.getChild(i).getChild(0)));
                }
                case smaliParser.I_SOURCE -> {
                    source = convert(node.getChild(i).getChild(0)).getCode();
                }
                case smaliParser.I_METHODS, smaliParser.I_FIELDS, smaliParser.I_ANNOTATIONS -> {
                    for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                        children.add(convert(node.getChild(i).getChild(j)));
                    }
                }
            }
        }


        return factory.classNode(classDescriptor, superDescriptor, accessList, implementsDescriptors, dexClass, source, children);
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

        var label = node.getChild(0).getText();

        return factory.label(label);
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

        //var attributes = getStatementAttributes(null);
        var children = new ArrayList<LabelRef>();

        var i = 0;
        TypeDescriptor exceptionType = null;
        if (node.getChild(i).getType() != smaliParser.SIMPLE_NAME) {
            // Non void type descriptor
            if (node.getChild(i).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
                i++;
                exceptionType = factory.arrayType(node.getChild(i).getText());
            } else {
                exceptionType = factory.nonVoidType(node.getChild(i).getText());
            }
            i++;
        }

        for (; i < node.getChildCount(); i++) {
            children.add(factory.labelRef(node.getChild(i).getText()));
        }

        return factory.catchDirective(exceptionType, children);
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


        var visibility = AnnotationVisibility.getFromString(node.getChild(0).getText());

        var subannotation = node.getChild(1);
        var classDescriptor = convert(subannotation.getChild(0));

        var annotationElements = new ArrayList<AnnotationElement>();
        for (int i = 1; i < subannotation.getChildCount(); i++) {
            annotationElements.add((AnnotationElement) convert(subannotation.getChild(i)));
        }

        return factory.annotationDirective(visibility, classDescriptor, annotationElements);
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

        var name = node.getChild(0).getText();
        var value = convert(node.getChild(1));

        return factory.annotationElement(name, value);
    }

    private SmaliNode convertField(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var accessOrRestrictionList = new ArrayList<Modifier>();
        String memberName = null;
        TypeDescriptor fieldType = null;

        var children = new ArrayList<SmaliNode>();

        for (int i = 0; i < node.getChildCount(); i++) {
            switch (node.getChild(i).getType()) {
                case smaliParser.I_ACCESS_OR_RESTRICTION_LIST -> {
                    for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                        accessOrRestrictionList.add(getAccessSpecOrHiddenApiRestriction(node.getChild(i).getChild(j)));
                    }
                }
                case smaliParser.SIMPLE_NAME -> {
                    memberName = node.getChild(i).getText();
                }
                case smaliParser.I_FIELD_TYPE -> {
                    // Non void type descriptor
                    for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
                        if (node.getChild(i).getChild(j).getType() == smaliParser.ARRAY_TYPE_PREFIX) {
                            j++;
                            fieldType = factory.arrayType(node.getChild(i).getChild(j).getText());
                        } else {
                            fieldType = factory.nonVoidType(node.getChild(i).getChild(j).getText());
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

        return factory.fieldNode(memberName, fieldType, accessOrRestrictionList, children);
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

    private EncodedArray convertArray(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var children = new ArrayList<SmaliNode>();

        for (int i = 0; i < node.getChildCount(); i++) {
            children.add(convert(node.getChild(i)));
        }

        var array = factory.encodedArray(children);

        if (!children.isEmpty()) {
            var child = children.get(0);
            var type = getType(child);
            array.setType(factory.arrayType((type)));
        }
        return array;
    }

    private TypeDescriptor getType(SmaliNode node) {
        if (node instanceof Expression expr) {
            return expr.getType();
        }

        if (node instanceof TypeDescriptor type) {
            return type;
        }

        throw new NotImplementedException(node);
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

        var value = node.getText();

        var literalExpr = factory.primitiveLiteral(value);

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


        var value = "'" + escapeString(node.getText().substring(1, node.getText().length() - 1)) + "'";

        var literalExpr = factory.primitiveLiteral(value);

        literalExpr.setType(factory.type("C"));

        return literalExpr;
    }

    private SmaliNode convertStringLiteral(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var value = "\"" + escapeString(node.getText().substring(1, node.getText().length() - 1)) + "\"";

        var literalExpr = factory.primitiveLiteral(value);

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

    private SmaliNode convertVoidType(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        return factory.type("V");
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

        children.add(factory.labelRef(node.getChild(i).getText()));

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


//    private List<SmaliNode> convertLabelReferenceStatement(Tree node)

    private <T extends Instruction> SmaliNode convertInstruction(Tree node) {
        return convertInstruction(node, null);
    }

    private <T extends Instruction> SmaliNode convertInstruction(Tree node, Class<T> defaultClass) {
        return convertInstruction(node, this::convertInstructionChildren, defaultClass);
    }

    private <T extends Instruction> SmaliNode convertInstruction(Tree node, Function<Tree, List<SmaliNode>> childrenConverter, Class<T> defaultClass) {
        var factory = context.get(SmaliContext.FACTORY);

        var opcode = getOpcode(node);
        var children = childrenConverter.apply(node);

        var instructionClass = OPCODE_TO_INSTRUCTION.get(opcode);

        if (instructionClass != null) {
            return factory.genericInstruction(instructionClass, opcode, children);
        }

        Objects.requireNonNull(defaultClass, () -> "Could not find a mapping for opcode " + opcode + " and no default class was provided");

        return factory.genericInstruction(defaultClass, opcode, children);
    }

    private List<SmaliNode> convertMethodChildren(Tree node) {
        var children = new ArrayList<SmaliNode>();

        children.add(convert(node.getChild(1)));

        children.add(convertMethodReference(node, 2));

        return children;
    }

    private List<SmaliNode> convertFormat4Children(Tree node) {
        var children = new ArrayList<SmaliNode>();

        children.add(convert(node.getChild(1)));

        children.add(convertMethodReference(node, 2));

        children.add(convert(node.getChild(node.getChildCount() - 1)));

        return children;
    }

    private SmaliNode convertArrayDataDirective(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);


        var elementWidthNode = (Literal) convert(node.getChild(0).getChild(0));
        var elementWidth = Integer.parseInt(elementWidthNode.getCode());

        var arrayElements = node.getChild(1);

        var elements = new ArrayList<Literal>();
        for (int i = 0; i < arrayElements.getChildCount(); i++) {
            elements.add((Literal) convert(arrayElements.getChild(i)));
        }

        return factory.arrayDataDirective(elementWidth, elements);
    }

    private SmaliNode convertPackedSwitch(Tree node) {
        var factory = context.get(SmaliContext.FACTORY);

        var attributes = getStatementAttributes(null);
        var children = new ArrayList<SmaliNode>();

        attributes.put("key", convert(node.getChild(0).getChild(0)));

        var packedSwitchElements = node.getChild(1);

        for (int i = 0; i < packedSwitchElements.getChildCount(); i++) {
            children.add(factory.labelRef(packedSwitchElements.getChild(i).getText()));
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

            elementChildren.add(factory.labelRef(sparseSwitchElements.getChild(i + 1).getText()));

            children.add(factory.sparseSwitchElement(elementChildren));
        }

        return factory.sparseSwitchDirective(attributes, children);
    }

}
