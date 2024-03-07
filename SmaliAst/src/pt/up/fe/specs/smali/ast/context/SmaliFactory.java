package pt.up.fe.specs.smali.ast.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

import pt.up.fe.specs.smali.ast.ClassNode;
import pt.up.fe.specs.smali.ast.FieldNode;
import pt.up.fe.specs.smali.ast.MethodNode;
import pt.up.fe.specs.smali.ast.Placeholder;
import pt.up.fe.specs.smali.ast.RegistersDirective;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.FieldReference;
import pt.up.fe.specs.smali.ast.expr.LiteralRef;
import pt.up.fe.specs.smali.ast.expr.MethodReference;
import pt.up.fe.specs.smali.ast.expr.RegisterList;
import pt.up.fe.specs.smali.ast.expr.RegisterRange;
import pt.up.fe.specs.smali.ast.expr.RegisterReference;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat10x;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat11n;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat11x;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat12x;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21cField;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21cString;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21cType;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21s;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat22b;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat22cField;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat22cType;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat22s;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat22x;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat23x;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat31c;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat32x;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat35cMethod;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat35cType;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat3rcMethod;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat3rcType;
import pt.up.fe.specs.smali.ast.type.ArrayType;
import pt.up.fe.specs.smali.ast.type.ClassType;
import pt.up.fe.specs.smali.ast.type.MethodPrototype;
import pt.up.fe.specs.smali.ast.type.PrimitiveType;
import pt.up.fe.specs.smali.ast.type.Type;

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

    public Type nonVoidType(String type) {
        switch (type.charAt(0)) {
        case 'Z', 'B', 'S', 'C', 'I', 'J', 'F', 'D' -> {
            var data = newDataStore(PrimitiveType.class);
            data.set(PrimitiveType.TYPE, type.substring(0, 1));
            return new PrimitiveType(data, null);
        }
        case 'L' -> {
            return classType(type);
        }
        default -> throw new RuntimeException("Not implemented: " + type);
        }
    }

    public Type type(String type) {
        if (type.charAt(0) == 'V') {
            var data = newDataStore(PrimitiveType.class);
            data.set(PrimitiveType.TYPE, "V");
            return new PrimitiveType(data, null);
        } else
            return nonVoidType(type);
    }

    public InstructionFormat10x instructionFormat10x(String instruction) {
        var data = newDataStore(InstructionFormat10x.class);
        data.set(InstructionFormat10x.INSTRUCTION, instruction);

        return new InstructionFormat10x(data, null);
    }

    public InstructionFormat11x instructionFormat11x(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat11x.class);
        data.set(InstructionFormat11x.INSTRUCTION, instruction);

        return new InstructionFormat11x(data, children);
    }

    public InstructionFormat11n instructionFormat11n(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat11n.class);
        data.set(InstructionFormat11n.INSTRUCTION, instruction);

        return new InstructionFormat11n(data, children);
    }

    public InstructionFormat12x instructionFormat12x(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat12x.class);
        data.set(InstructionFormat12x.INSTRUCTION, instruction);

        return new InstructionFormat12x(data, children);
    }

    public InstructionFormat21cField instructionFormat21cField(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21cField.class);
        data.set(InstructionFormat21cField.INSTRUCTION, instruction);

        return new InstructionFormat21cField(data, children);
    }

    public InstructionFormat21cString instructionFormat21cString(String instruction,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21cString.class);
        data.set(InstructionFormat21cString.INSTRUCTION, instruction);

        return new InstructionFormat21cString(data, children);
    }

    public InstructionFormat21cType instructionFormat21cType(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21cType.class);
        data.set(InstructionFormat21cType.INSTRUCTION, instruction);

        return new InstructionFormat21cType(data, children);
    }

    public InstructionFormat21s instructionFormat21s(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat21s.class);
        data.set(InstructionFormat21s.INSTRUCTION, instruction);

        return new InstructionFormat21s(data, children);
    }

    public InstructionFormat22cField instructionFormat22cField(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22cField.class);
        data.set(InstructionFormat22cField.INSTRUCTION, instruction);

        return new InstructionFormat22cField(data, children);
    }

    public InstructionFormat22cType instructionFormat22cType(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22cType.class);
        data.set(InstructionFormat22cType.INSTRUCTION, instruction);

        return new InstructionFormat22cType(data, children);
    }

    public InstructionFormat22b instructionFormat22b(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22b.class);
        data.set(InstructionFormat22b.INSTRUCTION, instruction);

        return new InstructionFormat22b(data, children);
    }

    public InstructionFormat22s instructionFormat22s(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22s.class);
        data.set(InstructionFormat22s.INSTRUCTION, instruction);

        return new InstructionFormat22s(data, children);
    }

    public InstructionFormat22x instructionFormat22x(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat22x.class);
        data.set(InstructionFormat22x.INSTRUCTION, instruction);

        return new InstructionFormat22x(data, children);
    }

    public InstructionFormat23x instructionFormat23x(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat23x.class);
        data.set(InstructionFormat23x.INSTRUCTION, instruction);

        return new InstructionFormat23x(data, children);
    }

    public InstructionFormat31c instructionFormat31c(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat31c.class);
        data.set(InstructionFormat31c.INSTRUCTION, instruction);

        return new InstructionFormat31c(data, children);
    }

    public InstructionFormat32x instructionFormat32x(String instruction, List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat32x.class);
        data.set(InstructionFormat32x.INSTRUCTION, instruction);

        return new InstructionFormat32x(data, children);
    }

    public InstructionFormat35cMethod instructionFormat35cMethod(String instruction,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat35cMethod.class);
        data.set(InstructionFormat35cMethod.INSTRUCTION, instruction);

        return new InstructionFormat35cMethod(data, children);
    }

    public InstructionFormat35cType instructionFormat35cType(String instruction,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat35cType.class);
        data.set(InstructionFormat35cType.INSTRUCTION, instruction);

        return new InstructionFormat35cType(data, children);
    }

    public InstructionFormat3rcMethod instructionFormat3rcMethod(String instruction,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat3rcMethod.class);
        data.set(InstructionFormat3rcMethod.INSTRUCTION, instruction);

        return new InstructionFormat3rcMethod(data, children);
    }

    public InstructionFormat3rcType instructionFormat3rcType(String instruction,
            List<? extends SmaliNode> children) {
        var data = newDataStore(InstructionFormat3rcType.class);
        data.set(InstructionFormat3rcType.INSTRUCTION, instruction);

        return new InstructionFormat3rcType(data, children);
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

    public LiteralRef literalRef(HashMap<String, Object> attributes) {
        var data = newDataStore(LiteralRef.class);
        data.set(LiteralRef.ATTRIBUTES, attributes);

        return new LiteralRef(data, null);
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

}
