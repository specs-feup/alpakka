package pt.up.fe.specs.alpakka.ast;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.ClassType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ClassNode extends SmaliNode {


    public static final DataKey<ClassType> CLASS_DESCRIPTOR = KeyFactory.object("classDescriptor", ClassType.class);

    public static final DataKey<Optional<ClassType>> SUPER_DESCRIPTOR = KeyFactory.optional("superClassDescriptor");

    public static final DataKey<List<AccessSpec>> ACCESS_LIST = KeyFactory.list("accessList", AccessSpec.class);

    public static final DataKey<List<ClassType>> IMPLEMENTS_DESCRIPTORS = KeyFactory.list("implementsDescriptors", ClassType.class);

    public static final DataKey<Optional<String>> DEX_CLASS = KeyFactory.optional("dexClass");

    public static final DataKey<Optional<String>> SOURCE = KeyFactory.optional("source");


    public ClassNode(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, reorderClassItems(children));
    }

    private static List<SmaliNode> reorderClassItems(Collection<? extends SmaliNode> children) {
        List<SmaliNode> reorderedChildren = new ArrayList<>();

        children.stream()
                .filter(c -> c instanceof FieldNode)
                .forEach(reorderedChildren::add);

        children.stream()
                .filter(c -> !(c instanceof FieldNode))
                .forEach(reorderedChildren::add);

        return reorderedChildren;
    }

    @Override
    public String getCode() {
        var accessList = get(ACCESS_LIST);
        var classDescriptor = getClassDescriptor();
        var superClassDescriptor = getSuperClass().orElse(null);
        var implementsDescriptors = get(IMPLEMENTS_DESCRIPTORS);
        var source = get(SOURCE).orElse(null);


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

        getChildren().forEach(c -> builder.append(c.getCode() + "\n"));

        return builder.toString();
    }

    public ClassType getClassDescriptor() {
        return get(CLASS_DESCRIPTOR);
    }

    public String getDexClassName() {
        return get(DEX_CLASS).orElse("");
    }

    public List<MethodNode> getMethods() {
        return getChildren().stream()
                .filter(c -> c instanceof MethodNode)
                .map(c -> (MethodNode) c)
                .toList();
    }

    public List<FieldNode> getFields() {
        return getChildren().stream()
                .filter(c -> c instanceof FieldNode)
                .map(c -> (FieldNode) c)
                .toList();
    }

    public Optional<ClassType> getSuperClass() {
        return get(SUPER_DESCRIPTOR);
    }

}
