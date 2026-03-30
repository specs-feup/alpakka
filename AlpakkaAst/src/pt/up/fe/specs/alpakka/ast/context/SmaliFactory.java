package pt.up.fe.specs.alpakka.ast.context;

import com.android.tools.smali.dexlib2.Format;
import com.android.tools.smali.dexlib2.Opcode;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;
import pt.up.fe.specs.alpakka.ast.*;
import pt.up.fe.specs.alpakka.ast.expr.*;
import pt.up.fe.specs.alpakka.ast.expr.literal.*;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.ArrayType;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.ClassType;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.PrimitiveType;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.TypeDescriptor;
import pt.up.fe.specs.alpakka.ast.stmt.*;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.*;
import pt.up.fe.specs.util.SpecsSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SmaliFactory {

    private final SmaliContext context;
    private int idCounter;

    public SmaliFactory(SmaliContext context) {
        this.context = context;
        idCounter = 0;
    }

    public DataStore newDataStore(Class<? extends SmaliNode> nodeClass) {

        DataStore data = DataStore.newInstance(StoreDefinitions.fromInterface(nodeClass), true);

        // Set context
        data.set(SmaliNode.CONTEXT, context);

        // Set id
        var id = nextId();
        data.set(SmaliNode.ID, id);

        return data;
    }

    private String nextId() {
        var number = idCounter;
        idCounter++;
        return "id_" + number;
    }

    public Resource resource(HashMap<String, Object> attributes) {
        var data = newDataStore(Resource.class);
        data.set(Resource.ATTRIBUTES, attributes);

        return new Resource(data, null);
    }

    public Manifest manifest(HashMap<String, Object> attributes) {
        var data = newDataStore(Manifest.class);
        data.set(Manifest.ATTRIBUTES, attributes);

        return new Manifest(data, null);
    }

    public App app(HashMap<String, Object> attributes, List<? extends SmaliNode> children) {
        var data = newDataStore(App.class);
        data.set(App.ATTRIBUTES, attributes);

        return new App(data, children);
    }

    public Placeholder placeholder(String kind, List<? extends SmaliNode> children) {
        var data = newDataStore(Placeholder.class);
        data.set(Placeholder.KIND, kind);

        return new Placeholder(data, children);
    }

    public ClassNode classNode(ClassType classDescriptor, ClassType superDescriptor, List<AccessSpec> accessList, List<ClassType> implementsDescriptors, String dexClass, String source, List<? extends SmaliNode> children) {
        var data = newDataStore(ClassNode.class)
                .put(ClassNode.CLASS_DESCRIPTOR, classDescriptor)
                .put(ClassNode.SUPER_DESCRIPTOR, Optional.ofNullable(superDescriptor))
                .put(ClassNode.ACCESS_LIST, accessList)
                .put(ClassNode.IMPLEMENTS_DESCRIPTORS, implementsDescriptors)
                .put(ClassNode.DEX_CLASS, Optional.ofNullable(dexClass))
                .put(ClassNode.SOURCE, Optional.ofNullable(source));

        return new ClassNode(data, children);
    }

    public RegistersDirective registersDirective(HashMap<String, Object> attributes) {
        var data = newDataStore(RegistersDirective.class);
        data.set(RegistersDirective.ATTRIBUTES, attributes);

        return new RegistersDirective(data, null);
    }

    public LineDirective lineDirective(HashMap<String, Object> attributes) {
        var data = newDataStore(LineDirective.class);
        data.set(LineDirective.ATTRIBUTES, attributes);

        return new LineDirective(data, null);
    }

    public PrologueDirective prologueDirective(HashMap<String, Object> attributes) {
        var data = newDataStore(PrologueDirective.class);
        data.set(PrologueDirective.ATTRIBUTES, attributes);

        return new PrologueDirective(data, null);
    }

    public EpilogueDirective epilogueDirective(HashMap<String, Object> attributes) {
        var data = newDataStore(EpilogueDirective.class);
        data.set(EpilogueDirective.ATTRIBUTES, attributes);

        return new EpilogueDirective(data, null);
    }

    public LocalDirective localDirective(HashMap<String, Object> attributes) {
        var data = newDataStore(LocalDirective.class);
        data.set(LocalDirective.ATTRIBUTES, attributes);

        return new LocalDirective(data, null);
    }

    public EndLocalDirective endLocalDirective(HashMap<String, Object> attributes, List<? extends SmaliNode> children) {
        var data = newDataStore(EndLocalDirective.class);
        data.set(EndLocalDirective.ATTRIBUTES, attributes);

        return new EndLocalDirective(data, children);
    }

    public RestartLocalDirective restartLocalDirective(HashMap<String, Object> attributes, List<? extends SmaliNode> children) {
        var data = newDataStore(RestartLocalDirective.class);
        data.set(RestartLocalDirective.ATTRIBUTES, attributes);

        return new RestartLocalDirective(data, children);
    }

    public CatchDirective catchDirective(TypeDescriptor exceptionType, List<? extends LabelRef> children) {
        var data = newDataStore(CatchDirective.class);
        data.set(CatchDirective.EXCEPTION_TYPE, Optional.ofNullable(exceptionType));

        return new CatchDirective(data, children);
    }

    public ParameterDirective parameterDirective(HashMap<String, Object> attributes,
                                                 List<? extends SmaliNode> children) {
        var data = newDataStore(ParameterDirective.class);
        data.set(ParameterDirective.ATTRIBUTES, attributes);

        return new ParameterDirective(data, children);
    }

    public AnnotationDirective annotationDirective(AnnotationVisibility visibility, SmaliNode classDescriptor,
                                                   List<? extends AnnotationElement> annotationElements) {

        var data = newDataStore(AnnotationDirective.class);
        data.put(AnnotationDirective.VISIBILITY, visibility);
        data.put(AnnotationDirective.CLASS_DESCRIPTOR, classDescriptor);

        return new AnnotationDirective(data, annotationElements);
    }

    public AnnotationElement annotationElement(String name, SmaliNode value) {
        var data = newDataStore(AnnotationElement.class);
        data.set(AnnotationElement.NAME, name);

        return new AnnotationElement(data, List.of(value));
    }

    public Label label(String label) {
        var data = newDataStore(Label.class)
                .put(Label.LABEL, label);

        return new Label(data, null);
    }

    public MethodNode methodNode(HashMap<String, Object> attributes, List<? extends SmaliNode> children) {
        var data = newDataStore(MethodNode.class);
        data.set(MethodNode.ATTRIBUTES, attributes);

        return new MethodNode(data, children);
    }

    public FieldNode fieldNode(String memberName, TypeDescriptor fieldType, List<Modifier> modifiers, List<? extends SmaliNode> children) {
        var data = newDataStore(FieldNode.class)
                .put(FieldNode.MEMBER_NAME, memberName)
                .put(FieldNode.FIELD_TYPE, fieldType)
                .put(FieldNode.MODIFIERS, modifiers);

        return new FieldNode(data, children);
    }

    public MethodPrototype methodPrototype(HashMap<String, Object> attributes) {
        var data = newDataStore(MethodPrototype.class);
        data.set(MethodPrototype.ATTRIBUTES, attributes);

        return new MethodPrototype(data, null);
    }

    public ClassType classType(String type) {
        var data = newDataStore(ClassType.class);
        var classDescriptor = type.substring(1, type.length() - 1);
        var lastSlash = classDescriptor.lastIndexOf('/');
        if (lastSlash == -1) {
            data.set(ClassType.PACKAGE_NAME, "");
        } else {
            data.set(ClassType.PACKAGE_NAME, classDescriptor.substring(0, lastSlash));
        }

        data.set(ClassType.CLASS_NAME, classDescriptor.substring(lastSlash + 1));

        return new ClassType(data, null);
    }

    public ArrayType arrayType(String type) {
        var data = newDataStore(ArrayType.class);

        var children = new ArrayList<SmaliNode>();
        children.add(nonVoidType(type));

        return new ArrayType(data, children);
    }

    public ArrayType arrayType(TypeDescriptor type) {
        var data = newDataStore(ArrayType.class);

        var children = new ArrayList<SmaliNode>();
        if (type != null)
            children.add(type);

        return new ArrayType(data, children);
    }

    public TypeDescriptor nonVoidType(String type) {
        if (type.equals("V")) {
            throw new RuntimeException("Void type is not allowed here");
        }

        return type(type);
    }

    public TypeDescriptor type(String type) {
        if (type.length() == 1) {
            switch (type) {
                case "Z", "B", "S", "C", "I", "J", "F", "D", "V" -> {
                    var data = newDataStore(PrimitiveType.class);
                    data.set(PrimitiveType.TYPE_DESCRIPTOR, type);
                    return new PrimitiveType(data, null);
                }

                default -> throw new RuntimeException("Single char type not implemented: " + type);
            }
        }

        if (type.startsWith("L")) {
            return classType(type);
        }

        throw new RuntimeException("Type not implemented: " + type);
    }

    public NopStatement nopInstructionFormat(HashMap<String, Object> attributes) {
        var data = newDataStore(NopStatement.class);
        data.set(NopStatement.ATTRIBUTES, attributes);

        return new NopStatement(data, null);
    }

    public <T extends Instruction> T genericInstruction(Class<T> instructionClass, Opcode opcode, LineDirective lineDirective,
                                                        List<? extends SmaliNode> children) {

        var data = newDataStore(instructionClass)
                .put(Instruction.LINE_DIRECTIVE, Optional.ofNullable(lineDirective))
                .put(Instruction.OPCODE, opcode);

        return SpecsSystem.newInstance(instructionClass, data, children);
    }

    public ReturnStatement returnInstructionFormat(HashMap<String, Object> attributes,
                                                   List<? extends SmaliNode> children) {
        var data = newDataStore(ReturnStatement.class);
        data.set(ReturnStatement.ATTRIBUTES, attributes);

        return new ReturnStatement(data, children);
    }

    public InstructionFormat21cField instructionFormat21cField(HashMap<String, Object> attributes,
                                                               List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21cField.class);
        data.set(InstructionFormat21cField.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format21c);

        return new InstructionFormat21cField(data, children);
    }

    public InstructionFormat21cType instructionFormat21cType(HashMap<String, Object> attributes,
                                                             List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21cType.class);
        data.set(InstructionFormat21cType.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format21c);

        return new InstructionFormat21cType(data, children);
    }

    public InstructionFormat21t instructionFormat21t(HashMap<String, Object> attributes,
                                                     List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21t.class);
        data.set(InstructionFormat21t.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format21t);

        return new InstructionFormat21t(data, children);
    }

    public InstructionFormat22cField instructionFormat22cField(HashMap<String, Object> attributes,
                                                               List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22cField.class);
        data.set(InstructionFormat22cField.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format22c);

        return new InstructionFormat22cField(data, children);
    }

    public InstructionFormat22cType instructionFormat22cType(HashMap<String, Object> attributes,
                                                             List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22cType.class);
        data.set(InstructionFormat22cType.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format22c);

        return new InstructionFormat22cType(data, children);
    }

    public InstructionFormat22t instructionFormat22t(HashMap<String, Object> attributes,
                                                     List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22t.class);
        data.set(InstructionFormat22t.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format22t);

        return new InstructionFormat22t(data, children);
    }

    public InstructionFormat23x instructionFormat23x(HashMap<String, Object> attributes,
                                                     List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat23x.class);
        data.set(InstructionFormat23x.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format23x);

        return new InstructionFormat23x(data, children);
    }

    public FillArrayStatement fillArrayInstructionFormat(HashMap<String, Object> attributes,
                                                         List<? extends SmaliNode> children) {
        var data = newDataStore(FillArrayStatement.class);
        data.set(FillArrayStatement.ATTRIBUTES, attributes);

        return new FillArrayStatement(data, children);
    }

    public SwitchStatement switchInstructionFormat(HashMap<String, Object> attributes,
                                                   List<? extends SmaliNode> children) {
        var data = newDataStore(SwitchStatement.class);
        data.set(SwitchStatement.ATTRIBUTES, attributes);

        return new SwitchStatement(data, children);
    }

    public InstructionFormat35cMethod instructionFormat35cMethod(HashMap<String, Object> attributes,
                                                                 List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat35cMethod.class);
        data.set(InstructionFormat35cMethod.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format35c);

        return new InstructionFormat35cMethod(data, children);
    }

    public InstructionFormat35cType instructionFormat35cType(HashMap<String, Object> attributes,
                                                             List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat35cType.class);
        data.set(InstructionFormat35cType.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format35c);

        return new InstructionFormat35cType(data, children);
    }

    public InstructionFormat3rcMethod instructionFormat3rcMethod(HashMap<String, Object> attributes,
                                                                 List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat3rcMethod.class);
        data.set(InstructionFormat3rcMethod.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format3rc);

        return new InstructionFormat3rcMethod(data, children);
    }

    public InstructionFormat3rcType instructionFormat3rcType(HashMap<String, Object> attributes,
                                                             List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat3rcType.class);
        data.set(InstructionFormat3rcType.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format3rc);

        return new InstructionFormat3rcType(data, children);
    }

    public InstructionFormat45ccMethod instructionFormat45ccMethod(HashMap<String, Object> attributes,
                                                                   List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat45ccMethod.class);
        data.set(InstructionFormat45ccMethod.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format45cc);

        return new InstructionFormat45ccMethod(data, children);
    }

    public InstructionFormat4rccMethod instructionFormat4rccMethod(HashMap<String, Object> attributes,
                                                                   List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat4rccMethod.class);
        data.set(InstructionFormat4rccMethod.ATTRIBUTES, attributes);
        data.set(Instruction.FORMAT, Format.Format4rcc);

        return new InstructionFormat4rccMethod(data, children);
    }

    public LabelRef labelRef(String label) {
        var data = newDataStore(LabelRef.class);
        data.set(LabelRef.LABEL, label);

        return new LabelRef(data, null);
    }

    public RegisterReference register(String register) {
        var data = newDataStore(RegisterReference.class);
        data.set(RegisterReference.REGISTER, register);

        return new RegisterReference(data, null);
    }

    public RegisterList registerList(List<? extends SmaliNode> children) {
        var data = newDataStore(RegisterList.class);

        return new RegisterList(data, children);
    }

    public RegisterRange registerRange(List<? extends SmaliNode> children) {
        var data = newDataStore(RegisterRange.class);

        return new RegisterRange(data, children);
    }

    public EncodedArray encodedArray(List<? extends SmaliNode> children) {
        var data = newDataStore(EncodedArray.class);

        return new EncodedArray(data, children);
    }

    public EncodedEnum encodedEnum(List<? extends SmaliNode> children) {
        var data = newDataStore(EncodedEnum.class);

        return new EncodedEnum(data, children);
    }

    public SubannotationDirective subannotationDirective(HashMap<String, Object> attributes,
                                                         List<? extends SmaliNode> children) {
        var data = newDataStore(SubannotationDirective.class);
        data.set(SubannotationDirective.ATTRIBUTES, attributes);

        return new SubannotationDirective(data, children);
    }

    public PrimitiveLiteral primitiveLiteral(String value) {
        var data = newDataStore(PrimitiveLiteral.class);
        data.set(PrimitiveLiteral.VALUE, value);

        return new PrimitiveLiteral(data, null);
    }

    public NullLiteral nullLiteral() {
        var data = newDataStore(NullLiteral.class);

        return new NullLiteral(data, null);
    }

    public EncodedField encodedField(List<? extends SmaliNode> children) {
        var data = newDataStore(EncodedField.class);

        return new EncodedField(data, children);
    }

    public EncodedMethod encodedMethod(List<? extends SmaliNode> children) {
        var data = newDataStore(EncodedMethod.class);

        return new EncodedMethod(data, children);
    }

    public FieldReference fieldReference(HashMap<String, Object> attributes) {
        var data = newDataStore(FieldReference.class);
        data.set(FieldReference.ATTRIBUTES, attributes);

        return new FieldReference(data, null);
    }

    public MethodReference methodReference(HashMap<String, Object> attributes) {
        var data = newDataStore(MethodReference.class);
        data.set(MethodReference.ATTRIBUTES, attributes);

        return new MethodReference(data, null);
    }

    public ArrayDataDirective arrayDataDirective(int elementWidth,
                                                 List<? extends Literal> elements) {

        var data = newDataStore(ArrayDataDirective.class);
        data.set(ArrayDataDirective.ELEMENT_WIDTH, elementWidth);

        return new ArrayDataDirective(data, elements);
    }

    public PackedSwitchDirective packedSwitchDirective(HashMap<String, Object> attributes,
                                                       List<? extends SmaliNode> children) {
        var data = newDataStore(PackedSwitchDirective.class);
        data.set(PackedSwitchDirective.ATTRIBUTES, attributes);

        return new PackedSwitchDirective(data, children);
    }

    public SparseSwitchDirective sparseSwitchDirective(HashMap<String, Object> attributes,
                                                       List<? extends SmaliNode> children) {
        var data = newDataStore(SparseSwitchDirective.class);
        data.set(SparseSwitchDirective.ATTRIBUTES, attributes);

        return new SparseSwitchDirective(data, children);
    }

    public SparseSwitchElement sparseSwitchElement(List<? extends SmaliNode> children) {
        var data = newDataStore(SparseSwitchElement.class);

        return new SparseSwitchElement(data, children);
    }

    public LiteralStatement literalStmt(String code) {
        var attributes = new HashMap<String, Object>();
        attributes.put("code", code);
        var data = newDataStore(LiteralStatement.class);
        data.set(LiteralStatement.ATTRIBUTES, attributes);

        return new LiteralStatement(data, null);
    }

}
