package pt.up.fe.specs.alpakka.ast;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import pt.up.fe.specs.alpakka.ast.expr.literal.MethodPrototype;
import pt.up.fe.specs.alpakka.ast.stmt.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MethodNode extends SmaliNode {

    public static final DataKey<String> NAME = KeyFactory.string("name");

    public static final DataKey<Optional<RegistersDirective>> LOCALS = KeyFactory.optional("locals");

    public static final DataKey<MethodPrototype> PROTOTYPE = KeyFactory.object("prototype", MethodPrototype.class);

    public static final DataKey<List<Modifier>> MODIFIERS = KeyFactory.list("modifiers", Modifier.class);


    public MethodNode(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, reorderMethodItems(children));
    }

    private static List<SmaliNode> reorderMethodItems(Collection<? extends SmaliNode> children) {
        List<SmaliNode> reorderedChildren = new ArrayList<>();

        children.stream()
                .filter(c -> c instanceof AnnotationDirective)
                .forEach(reorderedChildren::add);

        children.stream()
                .filter(c -> c instanceof ParameterDirective)
                .forEach(reorderedChildren::add);

        children.stream()
                .filter(c -> !(c instanceof AnnotationDirective || c instanceof ParameterDirective))
                .forEach(reorderedChildren::add);

        return reorderedChildren;
    }

    @Override
    public String getCode() {
        var name = getMethodName();
        var prototype = getPrototype();
        var accessList = getAccessList();
        var registersDirective = getRegistersDirective();

        var sb = new StringBuilder();
        sb.append(".method ");
        accessList.forEach(a -> sb.append(a.getLabel()).append(" "));
        sb.append(name);
        sb.append(prototype.getCode());
        sb.append("\n");

        registersDirective.ifPresent(locals -> sb.append(indentCode(locals.getCode())).append("\n"));

        var childrenExceptCatch = getChildren().stream()
                .filter(c -> !(c instanceof CatchDirective))
                .toList();

        for (var child : childrenExceptCatch) {
            sb.append("\n").append(indentCode(child.getCode()));
            if (child instanceof Label label) {
                var catchDirectives = getChildren().stream()
                        .filter(c -> c instanceof CatchDirective)
                        .map(c -> (CatchDirective) c)
                        .filter(c -> c.getTryEndLabelRef().getName().equals(label.getLabelReferenceName()))
                        .toList();

                for (var catchDir : catchDirectives) {
                    sb.append("\n").append(indentCode(catchDir.getCode()));
                }
                if (!catchDirectives.isEmpty()) {
                    sb.append("\n");
                }

            } else {
                sb.append("\n");
            }
        }

        sb.append(".end method\n");

        return sb.toString();
    }

    public String getMethodName() {
        return get(NAME);
    }

    public String getMethodReferenceName() {
        var sb = new StringBuilder();
        var parentClassDescriptor = ((ClassNode) getParent()).getClassDescriptor();

        if (parentClassDescriptor != null) {
            sb.append(parentClassDescriptor.getCode()).append("->");
        }

        sb.append(getMethodName());
        sb.append(getPrototype().getCode());

        return sb.toString();
    }

    public Optional<RegistersDirective> getRegistersDirective() {
        return get(LOCALS);
    }

    public MethodPrototype getPrototype() {
        return get(PROTOTYPE);
    }

    public List<Modifier> getAccessList() {
        return get(MODIFIERS);
    }

    public boolean isStatic() {
        return getAccessList().contains(AccessSpec.STATIC);
    }

}
