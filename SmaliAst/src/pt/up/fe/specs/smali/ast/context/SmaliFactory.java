package pt.up.fe.specs.smali.ast.context;

import java.util.List;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

import pt.up.fe.specs.smali.ast.Placeholder;
import pt.up.fe.specs.smali.ast.SmaliNode;

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

}
