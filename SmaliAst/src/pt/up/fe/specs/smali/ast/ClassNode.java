package pt.up.fe.specs.smali.ast;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.type.ClassType;

public class ClassNode extends SmaliNode {

	public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
			() -> new HashMap<String, Object>());

	public ClassNode(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var attributes = get(ATTRIBUTES);
		var accessList = (List<AccessSpec>) attributes.get("accessList");
		var classDescriptor = (ClassType) attributes.get("classDescriptor");
		var superClassDescriptor = (ClassType) attributes.get("superClassDescriptor");
		var implementsDescriptors = (List<ClassType>) attributes.get("implementsDescriptors");
		var source = (String) attributes.get("source");

		var builder = new StringBuilder();
		builder.append(".class ");
		for (var access : accessList) {
			builder.append(access.getLabel());
			builder.append(" ");
		}
		builder.append(classDescriptor.getCode());
		builder.append("\n");

		if (superClassDescriptor != null) {
			builder.append(".super ");
			builder.append(superClassDescriptor.getCode());
			builder.append("\n");
		}

		for (var implement : implementsDescriptors) {
			builder.append(".implements ");
			builder.append(implement.getCode());
			builder.append("\n");
		}

		if (source != null) {
			builder.append(".source ");
			builder.append(source);
			builder.append("\n");
		}

		builder.append("\n");

		for (SmaliNode child : getChildren()) {
			builder.append(child.getCode() + "\n");
		}

		return builder.toString();
	}

}
