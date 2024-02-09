package pt.up.fe.specs.smali.ast.context;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

public class SmaliContext extends ADataClass<SmaliContext> {

    public final static DataKey<SmaliFactory> FACTORY = KeyFactory.object("factory", SmaliFactory.class);

    public SmaliContext() {

        // Initialize factory
        set(FACTORY, new SmaliFactory(this));
    }

}
