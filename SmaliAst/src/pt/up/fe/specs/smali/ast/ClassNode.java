package pt.up.fe.specs.smali.ast;

import java.util.Collection;
import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.type.ClassType;

public class ClassNode extends SmaliNode {

	public static final DataKey<ClassType> CLASS_DESCRIPTOR = KeyFactory.object("classDescriptor", ClassType.class);
	public static final DataKey<List<String>> ACCESS_LIST = KeyFactory.list("accessList", String.class);
	public static final DataKey<ClassType> SUPER_CLASS_DESCRIPTOR = KeyFactory.object("superClassDescriptor",
			ClassType.class);
	public static final DataKey<List<ClassType>> IMPLEMENTS_DESCRIPTORS = KeyFactory.list("implementsList",
			ClassType.class);
	public static final DataKey<String> SOURCE = KeyFactory.string("source");

	public ClassNode(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	@Override
	public String getCode() {
		var builder = new StringBuilder();
		builder.append(".class ");
		for (String access : get(ACCESS_LIST)) {
			builder.append(access.toLowerCase().replace("_", "-"));
			builder.append(" ");
		}
		builder.append(get(CLASS_DESCRIPTOR).getCode());
		builder.append("\n");

		if (!get(SUPER_CLASS_DESCRIPTOR).getCode().isEmpty()) {
			builder.append(".super ");
			builder.append(get(SUPER_CLASS_DESCRIPTOR).getCode());
			builder.append("\n");
		}

		for (ClassType implement : get(IMPLEMENTS_DESCRIPTORS)) {
			builder.append(".implements ");
			builder.append(implement.getCode());
			builder.append("\n");
		}

		if (!get(SOURCE).isEmpty()) {
			builder.append(".source ");
			builder.append(get(SOURCE));
			builder.append("\n");
		}

		for (SmaliNode child : getChildren()) {
			builder.append(child.getCode());
		}

		return builder.toString();
	}

}
