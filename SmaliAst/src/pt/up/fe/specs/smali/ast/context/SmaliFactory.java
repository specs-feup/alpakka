package pt.up.fe.specs.smali.ast.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

import pt.up.fe.specs.smali.ast.App;
import pt.up.fe.specs.smali.ast.ClassNode;
import pt.up.fe.specs.smali.ast.FieldNode;
import pt.up.fe.specs.smali.ast.MethodNode;
import pt.up.fe.specs.smali.ast.Placeholder;
import pt.up.fe.specs.smali.ast.Resource;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.AnnotationElement;
import pt.up.fe.specs.smali.ast.expr.FieldReference;
import pt.up.fe.specs.smali.ast.expr.LabelRef;
import pt.up.fe.specs.smali.ast.expr.MethodReference;
import pt.up.fe.specs.smali.ast.expr.RegisterList;
import pt.up.fe.specs.smali.ast.expr.RegisterRange;
import pt.up.fe.specs.smali.ast.expr.RegisterReference;
import pt.up.fe.specs.smali.ast.expr.SparseSwitchElement;
import pt.up.fe.specs.smali.ast.expr.literal.EncodedArray;
import pt.up.fe.specs.smali.ast.expr.literal.EncodedEnum;
import pt.up.fe.specs.smali.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.smali.ast.expr.literal.NullLiteral;
import pt.up.fe.specs.smali.ast.expr.literal.PrimitiveLiteral;
import pt.up.fe.specs.smali.ast.expr.literal.SubannotationDirective;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.ArrayType;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.ClassType;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.PrimitiveType;
import pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor.TypeDescriptor;
import pt.up.fe.specs.smali.ast.stmt.AnnotationDirective;
import pt.up.fe.specs.smali.ast.stmt.CatchDirective;
import pt.up.fe.specs.smali.ast.stmt.Label;
import pt.up.fe.specs.smali.ast.stmt.LineDirective;
import pt.up.fe.specs.smali.ast.stmt.LiteralStatement;
import pt.up.fe.specs.smali.ast.stmt.ParameterDirective;
import pt.up.fe.specs.smali.ast.stmt.RegistersDirective;
import pt.up.fe.specs.smali.ast.stmt.instruction.ArrayDataDirective;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat10t;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat10x;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat11n;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat11x;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat12x;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat20t;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21cField;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21cMethodType;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21cString;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21cType;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21ih;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21lh;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21s;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat21t;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat22b;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat22cField;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat22cType;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat22s;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat22t;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat22x;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat23x;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat30t;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat31c;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat31i;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat31t;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat32x;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat35cMethod;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat35cType;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat3rcMethod;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat3rcType;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat45ccMethod;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat4rccMethod;
import pt.up.fe.specs.smali.ast.stmt.instruction.InstructionFormat51l;
import pt.up.fe.specs.smali.ast.stmt.instruction.PackedSwitchDirective;
import pt.up.fe.specs.smali.ast.stmt.instruction.SparseSwitchDirective;

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

    public ClassNode classNode(HashMap<String, Object> attributes, List<? extends SmaliNode> children) {
        var data = newDataStore(ClassNode.class);
        data.set(ClassNode.ATTRIBUTES, attributes);

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

    public CatchDirective catchDirective(HashMap<String, Object> attributes) {
        var data = newDataStore(CatchDirective.class);
        data.set(CatchDirective.ATTRIBUTES, attributes);

        return new CatchDirective(data, null);
    }

    public ParameterDirective parameterDirective(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(ParameterDirective.class);
        data.set(ParameterDirective.ATTRIBUTES, attributes);

        return new ParameterDirective(data, children);
    }

    public AnnotationDirective annotationDirective(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(AnnotationDirective.class);
        data.set(AnnotationDirective.ATTRIBUTES, attributes);

        return new AnnotationDirective(data, children);
    }

    public AnnotationElement annotationElement(HashMap<String, Object> attributes) {
        var data = newDataStore(AnnotationElement.class);
        data.set(AnnotationElement.ATTRIBUTES, attributes);

        return new AnnotationElement(data, null);
    }

    public Label label(HashMap<String, Object> attributes) {
        var data = newDataStore(Label.class);
        data.set(Label.ATTRIBUTES, attributes);

        return new Label(data, null);
    }

    public MethodNode methodNode(HashMap<String, Object> attributes, List<? extends SmaliNode> children) {
        var data = newDataStore(MethodNode.class);
        data.set(MethodNode.ATTRIBUTES, attributes);

        return new MethodNode(data, children);
    }

    public FieldNode fieldNode(HashMap<String, Object> attributes, List<? extends SmaliNode> children) {
        var data = newDataStore(FieldNode.class);
        data.set(FieldNode.ATTRIBUTES, attributes);

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

    public TypeDescriptor nonVoidType(String type) {
        switch (type.charAt(0)) {
        case 'Z', 'B', 'S', 'C', 'I', 'J', 'F', 'D' -> {
            var data = newDataStore(PrimitiveType.class);
            data.set(PrimitiveType.TYPE_DESCRIPTOR, type.substring(0, 1));
            return new PrimitiveType(data, null);
        }
        case 'L' -> {
            return classType(type);
        }
        default -> throw new RuntimeException("Not implemented: " + type);
        }
    }

    public TypeDescriptor type(String type) {
        if (type.charAt(0) == 'V') {
            var data = newDataStore(PrimitiveType.class);
            data.set(PrimitiveType.TYPE_DESCRIPTOR, "V");
            return new PrimitiveType(data, null);
        } else
            return nonVoidType(type);
    }

    public InstructionFormat10x instructionFormat10x(HashMap<String, Object> attributes) {
        var data = newDataStore(InstructionFormat10x.class);
        data.set(InstructionFormat10x.ATTRIBUTES, attributes);

        return new InstructionFormat10x(data, null);
    }

    public InstructionFormat10t instructionFormat10t(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat10t.class);
        data.set(InstructionFormat10t.ATTRIBUTES, attributes);

        return new InstructionFormat10t(data, children);
    }

    public InstructionFormat11x instructionFormat11x(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat11x.class);
        data.set(InstructionFormat11x.ATTRIBUTES, attributes);

        return new InstructionFormat11x(data, children);
    }

    public InstructionFormat11n instructionFormat11n(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat11n.class);
        data.set(InstructionFormat11n.ATTRIBUTES, attributes);

        return new InstructionFormat11n(data, children);
    }

    public InstructionFormat12x instructionFormat12x(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat12x.class);
        data.set(InstructionFormat12x.ATTRIBUTES, attributes);

        return new InstructionFormat12x(data, children);
    }

    public InstructionFormat20t instructionFormat20t(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat20t.class);
        data.set(InstructionFormat20t.ATTRIBUTES, attributes);

        return new InstructionFormat20t(data, children);
    }

    public InstructionFormat21ih instructionFormat21ih(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21ih.class);
        data.set(InstructionFormat21ih.ATTRIBUTES, attributes);

        return new InstructionFormat21ih(data, children);
    }

    public InstructionFormat21lh instructionFormat21lh(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21lh.class);
        data.set(InstructionFormat21lh.ATTRIBUTES, attributes);

        return new InstructionFormat21lh(data, children);
    }

    public InstructionFormat21cField instructionFormat21cField(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21cField.class);
        data.set(InstructionFormat21cField.ATTRIBUTES, attributes);

        return new InstructionFormat21cField(data, children);
    }

    public InstructionFormat21cString instructionFormat21cString(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21cString.class);
        data.set(InstructionFormat21cString.ATTRIBUTES, attributes);

        return new InstructionFormat21cString(data, children);
    }

    public InstructionFormat21cType instructionFormat21cType(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21cType.class);
        data.set(InstructionFormat21cType.ATTRIBUTES, attributes);

        return new InstructionFormat21cType(data, children);
    }

    public InstructionFormat21cMethodType instructionFormat21cMethodType(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21cMethodType.class);
        data.set(InstructionFormat21cMethodType.ATTRIBUTES, attributes);

        return new InstructionFormat21cMethodType(data, children);
    }

    public InstructionFormat21s instructionFormat21s(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21s.class);
        data.set(InstructionFormat21s.ATTRIBUTES, attributes);

        return new InstructionFormat21s(data, children);
    }

    public InstructionFormat21t instructionFormat21t(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21t.class);
        data.set(InstructionFormat21t.ATTRIBUTES, attributes);

        return new InstructionFormat21t(data, children);
    }

    public InstructionFormat22cField instructionFormat22cField(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22cField.class);
        data.set(InstructionFormat22cField.ATTRIBUTES, attributes);

        return new InstructionFormat22cField(data, children);
    }

    public InstructionFormat22cType instructionFormat22cType(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22cType.class);
        data.set(InstructionFormat22cType.ATTRIBUTES, attributes);

        return new InstructionFormat22cType(data, children);
    }

    public InstructionFormat22b instructionFormat22b(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22b.class);
        data.set(InstructionFormat22b.ATTRIBUTES, attributes);

        return new InstructionFormat22b(data, children);
    }

    public InstructionFormat22s instructionFormat22s(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22s.class);
        data.set(InstructionFormat22s.ATTRIBUTES, attributes);

        return new InstructionFormat22s(data, children);
    }

    public InstructionFormat22t instructionFormat22t(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22t.class);
        data.set(InstructionFormat22t.ATTRIBUTES, attributes);

        return new InstructionFormat22t(data, children);
    }

    public InstructionFormat22x instructionFormat22x(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22x.class);
        data.set(InstructionFormat22x.ATTRIBUTES, attributes);

        return new InstructionFormat22x(data, children);
    }

    public InstructionFormat23x instructionFormat23x(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat23x.class);
        data.set(InstructionFormat23x.ATTRIBUTES, attributes);

        return new InstructionFormat23x(data, children);
    }

    public InstructionFormat30t instructionFormat30t(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat30t.class);
        data.set(InstructionFormat30t.ATTRIBUTES, attributes);

        return new InstructionFormat30t(data, children);
    }

    public InstructionFormat31c instructionFormat31c(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat31c.class);
        data.set(InstructionFormat31c.ATTRIBUTES, attributes);

        return new InstructionFormat31c(data, children);
    }

    public InstructionFormat31i instructionFormat31i(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat31i.class);
        data.set(InstructionFormat31i.ATTRIBUTES, attributes);

        return new InstructionFormat31i(data, children);
    }

    public InstructionFormat31t instructionFormat31t(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat31t.class);
        data.set(InstructionFormat31t.ATTRIBUTES, attributes);

        return new InstructionFormat31t(data, children);
    }

    public InstructionFormat32x instructionFormat32x(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat32x.class);
        data.set(InstructionFormat32x.ATTRIBUTES, attributes);

        return new InstructionFormat32x(data, children);
    }

    public InstructionFormat35cMethod instructionFormat35cMethod(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat35cMethod.class);
        data.set(InstructionFormat35cMethod.ATTRIBUTES, attributes);

        return new InstructionFormat35cMethod(data, children);
    }

    public InstructionFormat35cType instructionFormat35cType(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat35cType.class);
        data.set(InstructionFormat35cType.ATTRIBUTES, attributes);

        return new InstructionFormat35cType(data, children);
    }

    public InstructionFormat3rcMethod instructionFormat3rcMethod(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat3rcMethod.class);
        data.set(InstructionFormat3rcMethod.ATTRIBUTES, attributes);

        return new InstructionFormat3rcMethod(data, children);
    }

    public InstructionFormat3rcType instructionFormat3rcType(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat3rcType.class);
        data.set(InstructionFormat3rcType.ATTRIBUTES, attributes);

        return new InstructionFormat3rcType(data, children);
    }

    public InstructionFormat45ccMethod instructionFormat45ccMethod(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat45ccMethod.class);
        data.set(InstructionFormat45ccMethod.ATTRIBUTES, attributes);

        return new InstructionFormat45ccMethod(data, children);
    }

    public InstructionFormat4rccMethod instructionFormat4rccMethod(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat4rccMethod.class);
        data.set(InstructionFormat4rccMethod.ATTRIBUTES, attributes);

        return new InstructionFormat4rccMethod(data, children);
    }

    public InstructionFormat51l instructionFormat51l(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat51l.class);
        data.set(InstructionFormat51l.ATTRIBUTES, attributes);

        return new InstructionFormat51l(data, children);
    }

    public LabelRef labelRef(HashMap<String, Object> attributes) {
        var data = newDataStore(LabelRef.class);
        data.set(LabelRef.ATTRIBUTES, attributes);

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

    public PrimitiveLiteral primitiveLiteral(HashMap<String, Object> attributes) {
        var data = newDataStore(PrimitiveLiteral.class);
        data.set(PrimitiveLiteral.ATTRIBUTES, attributes);

        return new PrimitiveLiteral(data, null);
    }

    public NullLiteral nullLiteral() {
        var data = newDataStore(NullLiteral.class);

        return new NullLiteral(data, null);
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

    public ArrayDataDirective arrayDataDirective(HashMap<String, Object> attributes,
            List<? extends SmaliNode> children) {
        var data = newDataStore(ArrayDataDirective.class);
        data.set(ArrayDataDirective.ATTRIBUTES, attributes);

        return new ArrayDataDirective(data, children);
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

    public SparseSwitchElement sparseSwitchElement(HashMap<String, Object> attributes) {
        var data = newDataStore(SparseSwitchElement.class);
        data.set(SparseSwitchElement.ATTRIBUTES, attributes);

        return new SparseSwitchElement(data, null);
    }

    public LiteralStatement literalStmt(String code) {
        var attributes = new HashMap<String, Object>();
        attributes.put("code", code);
        var data = newDataStore(LiteralStatement.class);
        data.set(LiteralStatement.ATTRIBUTES, attributes);

        return new LiteralStatement(data, null);
    }

}
