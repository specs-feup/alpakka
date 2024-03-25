package pt.up.fe.specs.smali.ast.expr.literal.typeDescriptor;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.SmaliNode;

public class ClassType extends TypeDescriptor {

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
}
