package pt.up.fe.specs.alpakka.ast.context;

import com.android.tools.smali.dexlib2.Opcode;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;
import pt.up.fe.specs.alpakka.ast.*;
import pt.up.fe.specs.alpakka.ast.expr.*;
import pt.up.fe.specs.alpakka.ast.expr.literal.*;
import pt.up.fe.specs.alpakka.ast.stmt.*;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.Instruction;
import pt.up.fe.specs.alpakka.ast.stmt.instruction.NopStatement;
import pt.up.fe.specs.alpakka.ast.type.*;
import pt.up.fe.specs.util.SpecsSystem;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class SmaliFactory {

    private static final Map<String, TypeDescriptor> TYPE_CACHE;

    static {
        TYPE_CACHE = new HashMap<>();
    }

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

    public Resource resource(File source) {
        var data = newDataStore(Resource.class)
                .put(Resource.SOURCE, source);

        return new Resource(data, null);
    }

    public Manifest manifest(File source, String packageName, HashMap<String, List<String>> components) {
        var data = newDataStore(Manifest.class)
                .put(Manifest.SOURCE, source)
                .put(Manifest.PACKAGE_NAME, packageName)
                .put(Manifest.COMPONENTS, components);

        return new Manifest(data, null);
    }

    public App app(Integer sdkVersion, List<? extends SmaliNode> children) {
        var data = newDataStore(App.class)
                .put(App.SDK_VERSION, sdkVersion);

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

    public RegistersDirective registersDirective(String type, Integer value) {
        var data = newDataStore(RegistersDirective.class)
                .put(RegistersDirective.TYPE, type)
                .put(RegistersDirective.VALUE, value);

        return new RegistersDirective(data, null);
    }

    public LineDirective lineDirective(int line) {
        var data = newDataStore(LineDirective.class)
                .put(LineDirective.LINE, line);

        return new LineDirective(data, null);
    }

    public PrologueDirective prologueDirective() {
        var data = newDataStore(PrologueDirective.class);

        return new PrologueDirective(data, null);
    }

    public EpilogueDirective epilogueDirective() {
        var data = newDataStore(EpilogueDirective.class);

        return new EpilogueDirective(data, null);
    }

    public LocalDirective localDirective(RegisterReference register) {
        var data = newDataStore(LocalDirective.class)
                .put(LocalDirective.REGISTER, register);

        return new LocalDirective(data, null);
    }

    public EndLocalDirective endLocalDirective(List<? extends SmaliNode> children) {
        var data = newDataStore(EndLocalDirective.class);

        return new EndLocalDirective(data, children);
    }

    public RestartLocalDirective restartLocalDirective(List<? extends SmaliNode> children) {
        var data = newDataStore(RestartLocalDirective.class);

        return new RestartLocalDirective(data, children);
    }

    public CatchDirective catchDirective(TypeDescriptor exceptionType, List<? extends LabelRef> children) {
        var data = newDataStore(CatchDirective.class);
        data.set(CatchDirective.EXCEPTION_TYPE, Optional.ofNullable(exceptionType));

        return new CatchDirective(data, children);
    }

    public ParameterDirective parameterDirective(RegisterReference register,
                                                 List<? extends SmaliNode> children) {
        var data = newDataStore(ParameterDirective.class)
                .put(ParameterDirective.REGISTER, register);

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

    public MethodNode methodNode(String name, MethodPrototype prototype, List<Modifier> modifiers, List<? extends SmaliNode> children) {
        var data = newDataStore(MethodNode.class)
                .put(MethodNode.NAME, name)
                .put(MethodNode.PROTOTYPE, prototype)
                .put(MethodNode.MODIFIERS, modifiers);

        return new MethodNode(data, children);
    }

    public FieldNode fieldNode(String memberName, TypeDescriptor fieldType, List<Modifier> modifiers, List<? extends SmaliNode> children) {
        var data = newDataStore(FieldNode.class)
                .put(FieldNode.MEMBER_NAME, memberName)
                .put(FieldNode.FIELD_TYPE, fieldType)
                .put(FieldNode.MODIFIERS, modifiers);

        return new FieldNode(data, children);
    }

    public MethodPrototype methodPrototype(TypeDescriptor returnType, List<TypeDescriptor> parameters) {
        var id = "M#" + returnType.getCode() + "#" + parameters.stream().map(TypeDescriptor::getCode).collect(Collectors.joining());
        if (TYPE_CACHE.containsKey(id)) {
            return (MethodPrototype) TYPE_CACHE.get(id);
        }

        var data = newDataStore(MethodPrototype.class)
                .put(MethodPrototype.RETURN_TYPE, returnType)
                .put(MethodPrototype.PARAMETERS, parameters);

        var methodPrototype = new MethodPrototype(data, null);
        TYPE_CACHE.put(id, methodPrototype);
        return methodPrototype;
    }

    public ClassType classType(String type) {
        if (TYPE_CACHE.containsKey(type)) {
            return (ClassType) TYPE_CACHE.get(type);
        }

        var data = newDataStore(ClassType.class);
        var classDescriptor = type.substring(1, type.length() - 1);
        var lastSlash = classDescriptor.lastIndexOf('/');
        if (lastSlash == -1) {
            data.set(ClassType.PACKAGE_NAME, "");
        } else {
            data.set(ClassType.PACKAGE_NAME, classDescriptor.substring(0, lastSlash));
        }

        data.set(ClassType.CLASS_NAME, classDescriptor.substring(lastSlash + 1));

        var classType = new ClassType(data, null);
        TYPE_CACHE.put(type, classType);
        return classType;
    }

    public ArrayType arrayType(String type) {
        var elementType = nonVoidType(type);
        return arrayType(elementType);
    }

    public ArrayType arrayType(TypeDescriptor type) {
        Objects.requireNonNull(type);

        var id = "[" + type.getCode();
        if (TYPE_CACHE.containsKey(id)) {
            return (ArrayType) TYPE_CACHE.get(id);
        }

        var data = newDataStore(ArrayType.class);

        var children = new ArrayList<SmaliNode>();
        children.add(type);

        var arrayType = new ArrayType(data, children);
        TYPE_CACHE.put(id, arrayType);
        return arrayType;
    }

    public TypeDescriptor nonVoidType(String type) {
        if (type.equals("V")) {
            throw new RuntimeException("Void type is not allowed here");
        }

        return type(type);
    }

    public TypeDescriptor type(String type) {
        if (TYPE_CACHE.containsKey(type)) {
            return TYPE_CACHE.get(type);
        }

        if (type.length() == 1) {
            switch (type) {
                case "Z", "B", "S", "C", "I", "J", "F", "D", "V" -> {
                    var data = newDataStore(PrimitiveType.class);
                    data.set(PrimitiveType.TYPE_DESCRIPTOR, type);
                    var primitiveType = new PrimitiveType(data, null);
                    TYPE_CACHE.put(type, primitiveType);
                    return primitiveType;
                }

                default -> throw new RuntimeException("Single char type not implemented: " + type);
            }
        }

        if (type.startsWith("L")) {
            return classType(type);
        }

        throw new RuntimeException("Type not implemented: " + type);
    }

    public NopStatement nopInstructionFormat() {
        var data = newDataStore(NopStatement.class);

        return new NopStatement(data, null);
    }

    public <T extends Instruction> T genericInstruction(Class<T> instructionClass, Opcode opcode,
                                                        List<? extends SmaliNode> children) {

        var data = newDataStore(instructionClass)
                .put(Instruction.OPCODE, opcode);

        return SpecsSystem.newInstance(instructionClass, data, children);
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

    public SubannotationDirective subannotationDirective(ClassType type,
                                                         List<? extends SmaliNode> children) {
        var data = newDataStore(SubannotationDirective.class)
                .setOptional(SubannotationDirective.TYPE, type);

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

    public FieldReference fieldReference(String memberName, TypeDescriptor fieldType) {
        var data = newDataStore(FieldReference.class)
                .put(FieldReference.MEMBER_NAME, memberName)
                .put(FieldReference.FIELD_TYPE, fieldType);

        return new FieldReference(data, null);
    }

    public MethodReference methodReference(String methodName, MethodPrototype methodType) {
        var data = newDataStore(MethodReference.class)
                .put(MethodReference.METHOD_NAME, methodName)
                .put(MethodReference.METHOD_TYPE, methodType);

        return new MethodReference(data, null);
    }

    public ArrayDataDirective arrayDataDirective(int elementWidth,
                                                 List<? extends Literal> elements) {

        var data = newDataStore(ArrayDataDirective.class);
        data.set(ArrayDataDirective.ELEMENT_WIDTH, elementWidth);

        return new ArrayDataDirective(data, elements);
    }

    public PackedSwitchDirective packedSwitchDirective(String value,
                                                       List<? extends SmaliNode> children) {
        var data = newDataStore(PackedSwitchDirective.class)
                .put(PackedSwitchDirective.VALUE, value);

        return new PackedSwitchDirective(data, children);
    }

    public SparseSwitchDirective sparseSwitchDirective(List<? extends SmaliNode> children) {
        var data = newDataStore(SparseSwitchDirective.class);

        return new SparseSwitchDirective(data, children);
    }

    public SparseSwitchElement sparseSwitchElement(List<? extends SmaliNode> children) {
        var data = newDataStore(SparseSwitchElement.class);

        return new SparseSwitchElement(data, children);
    }

    public LiteralStatement literalStmt(String code) {
        var data = newDataStore(LiteralStatement.class)
                .put(LiteralStatement.CODE, code);

        return new LiteralStatement(data, null);
    }

}
