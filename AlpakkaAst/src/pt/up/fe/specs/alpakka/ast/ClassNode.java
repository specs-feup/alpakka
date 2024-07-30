package pt.up.fe.specs.alpakka.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.alpakka.ast.expr.literal.Literal;
import pt.up.fe.specs.alpakka.ast.expr.literal.typeDescriptor.ClassType;

public class ClassNode extends SmaliNode {

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
        var attributes = get(ATTRIBUTES);
        var accessList = (List<AccessSpec>) attributes.get("accessList");
        var classDescriptor = getClassDescriptor();
        var superClassDescriptor = getSuperClass();
        var implementsDescriptors = (List<ClassType>) attributes.get("implementsDescriptors");
        var source = (Literal) attributes.get("source");

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
            builder.append(source.getCode());
            builder.append("\n");
        }

        builder.append("\n");

        getChildren().forEach(c -> builder.append(c.getCode() + "\n"));

        return builder.toString();
    }

    public ClassType getClassDescriptor() {
        return (ClassType) get(ATTRIBUTES).get("classDescriptor");
    }

    public String getDexClassName() {
        return get(ATTRIBUTES).get("dexClass") != null ? (String) get(ATTRIBUTES).get("dexClass") : "";
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

    public ClassType getSuperClass() {
        return (ClassType) get(ATTRIBUTES).get("superClassDescriptor");
    }

}
