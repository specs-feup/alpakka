package pt.up.fe.specs.smali.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.smali.ast.stmt.AnnotationDirective;
import pt.up.fe.specs.smali.ast.stmt.CatchDirective;
import pt.up.fe.specs.smali.ast.stmt.Instruction;
import pt.up.fe.specs.smali.ast.stmt.Label;
import pt.up.fe.specs.smali.ast.stmt.ParameterDirective;
import pt.up.fe.specs.smali.ast.stmt.RegistersDirective;
import pt.up.fe.specs.smali.ast.type.MethodPrototype;

public class MethodNode extends SmaliNode {

    public static final DataKey<Map<String, Object>> ATTRIBUTES = KeyFactory.generic("attributes",
            () -> new HashMap<String, Object>());

    public MethodNode(DataStore data, Collection<? extends SmaliNode> children) {
        super(data, children);
    }

    @Override
    public String getCode() {
        var attributes = get(ATTRIBUTES);
        var name = (String) attributes.get("name");
        var prototype = (MethodPrototype) attributes.get("prototype");
        var accessList = (ArrayList<Modifier>) attributes.get("accessOrRestrictionList");
        var registersDirective = (RegistersDirective) attributes.get("registersOrLocals");

        var sb = new StringBuilder();
        sb.append(".method ");
        accessList.forEach(a -> sb.append(a.getLabel()).append(" "));
        sb.append(name);
        sb.append(prototype.getCode());
        sb.append("\n");

        if (registersDirective != null) {
            sb.append(registersDirective.getCode()).append("\n");
        }

        getChildren().stream()
                .filter(c -> c instanceof AnnotationDirective)
                .forEach(c -> sb.append(c.getCode()));

        getChildren().stream()
                .filter(c -> c instanceof ParameterDirective)
                .forEach(c -> sb.append(c.getCode()));

        var methodItems = getChildren().stream()
                .filter(c -> (c instanceof Instruction || c instanceof Label))
                .toList();

        for (var child : methodItems) {
            sb.append("\n").append(child.getCode());
            if (child instanceof Label) {
                var label = (Label) child;
                var catchDirectives = getChildren().stream()
                        .filter(c -> c instanceof CatchDirective)
                        .map(c -> (CatchDirective) c)
                        .filter(c -> c.getEndLabelRef().getLabel().equals(label.getLabel()))
                        .toList();

                for (var catchDir : catchDirectives) {
                    sb.append("\n").append(catchDir.getCode());
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

}
