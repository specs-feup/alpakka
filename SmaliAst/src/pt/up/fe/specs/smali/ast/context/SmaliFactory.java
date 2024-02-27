package pt.up.fe.specs.smali.ast.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

import pt.up.fe.specs.smali.ast.ClassNode;
import pt.up.fe.specs.smali.ast.MethodNode;
import pt.up.fe.specs.smali.ast.Placeholder;
import pt.up.fe.specs.smali.ast.RegistersDirective;
import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.FieldReference;
import pt.up.fe.specs.smali.ast.expr.MethodReference;
import pt.up.fe.specs.smali.ast.expr.RegisterList;
import pt.up.fe.specs.smali.ast.expr.RegisterReference;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat10x;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21cField;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat21cString;
import pt.up.fe.specs.smali.ast.stmt.InstructionFormat35cMethod;
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

	public InstructionFormat35cMethod instructionFormat35cMethod(String instruction,
			List<? extends SmaliNode> children) {
		var data = newDataStore(InstructionFormat35cMethod.class);
		data.set(InstructionFormat35cMethod.INSTRUCTION, instruction);

		return new InstructionFormat35cMethod(data, children);
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
