package pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;
import pt.up.fe.specs.smali.ast.expr.Reference;

public class ClassType extends TypeDescriptor implements Reference {

	public static String TYPE_LABEL = "class";

	public static final DataKey<String> CLASS_NAME = KeyFactory.string("className");
	public static final DataKey<String> PACKAGE_NAME = KeyFactory.string("packageName");

	public ClassType(DataStore data, Collection<? extends SmaliNode> children) {
		super(data, children);
	}

	public String getPackageName() {
		return get(PACKAGE_NAME);
	}

	public String getClassName() {
		return get(CLASS_NAME);
	}

	@Override
	public String getCode() {
		var sb = new StringBuilder();
		var packageName = get(PACKAGE_NAME);

		sb.append("L");

		if (!packageName.isEmpty()) {
			sb.append(packageName).append("/");
		}

		sb.append(get(CLASS_NAME)).append(";");

		return sb.toString();
	}

	@Override
	public void setDeclaration(SmaliNode decl) {
		set(DECL, decl);
	}

	@Override
	public SmaliNode getDeclaration() {
		return get(DECL);
	}

	@Override
	public String getName() {
		return this.getCode();
	}

	@Override
	public String getTypeLabel() {
		return TYPE_LABEL;
	}
}
