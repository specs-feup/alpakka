package pt.up.fe.specs.alpakka.weaver.abstracts.joinpoints;

import org.lara.interpreter.weaver.interf.events.Stage;
import java.util.Optional;
import org.lara.interpreter.exception.AttributeException;
import pt.up.fe.specs.alpakka.weaver.abstracts.ASmaliWeaverJoinPoint;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

/**
 * Auto-Generated class for join point AProgram
 * This class is overwritten by the Weaver Generator.
 * 
 * App node
 * @author Lara Weaver Generator
 */
public abstract class AProgram extends ASmaliWeaverJoinPoint {

    /**
     * 
     * @param outputName
     * @return 
     */
    public abstract Void buildApkImpl(String outputName);

    /**
     * 
     * @param outputName
     * @return 
     */
    public final Object buildApk(String outputName) {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "buildApk", Optional.empty(), outputName);
        	}
        	Void result = this.buildApkImpl(outputName);
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "buildApk", Optional.ofNullable(result), outputName);
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "buildApk", e);
        }
    }

    /**
     * Get value on attribute classes
     * @return the attribute's value
     */
    public abstract AClassNode[] getClassesArrayImpl();

    /**
     * Get value on attribute classes
     * @return the attribute's value
     */
    public Object getClassesImpl() {
        AClassNode[] aClassNodeArrayImpl0 = getClassesArrayImpl();
        Object nativeArray0 = aClassNodeArrayImpl0;
        return nativeArray0;
    }

    /**
     * Get value on attribute classes
     * @return the attribute's value
     */
    public final Object getClasses() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "classes", Optional.empty());
        	}
        	Object result = this.getClassesImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "classes", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "classes", e);
        }
    }

    /**
     * Get value on attribute manifest
     * @return the attribute's value
     */
    public abstract AManifest getManifestImpl();

    /**
     * Get value on attribute manifest
     * @return the attribute's value
     */
    public final Object getManifest() {
        try {
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.BEGIN, this, "manifest", Optional.empty());
        	}
        	AManifest result = this.getManifestImpl();
        	if(hasListeners()) {
        		eventTrigger().triggerAttribute(Stage.END, this, "manifest", Optional.ofNullable(result));
        	}
        	return result!=null?result:getUndefinedValue();
        } catch(Exception e) {
        	throw new AttributeException(get_class(), "manifest", e);
        }
    }

    /**
     * Returns the join point type of this class
     * @return The join point type
     */
    @Override
    public final String get_class() {
        return "program";
    }
    /**
     * 
     */
    protected enum ProgramAttributes {
        BUILDAPK("buildApk"),
        CLASSES("classes"),
        MANIFEST("manifest"),
        AST("ast"),
        CHILDREN("children"),
        CODE("code"),
        DESCENDANTS("descendants"),
        GETANCESTOR("getAncestor"),
        GETCHILD("getChild"),
        GETDESCENDANTS("getDescendants"),
        GETDESCENDANTSANDSELF("getDescendantsAndSelf"),
        ID("id"),
        PARENT("parent"),
        ROOT("root");
        private String name;

        /**
         * 
         */
        private ProgramAttributes(String name){
            this.name = name;
        }
        /**
         * Return an attribute enumeration item from a given attribute name
         */
        public static Optional<ProgramAttributes> fromString(String name) {
            return Arrays.asList(values()).stream().filter(attr -> attr.name.equals(name)).findAny();
        }

        /**
         * Return a list of attributes in String format
         */
        public static List<String> getNames() {
            return Arrays.asList(values()).stream().map(ProgramAttributes::name).collect(Collectors.toList());
        }

        /**
         * True if the enum contains the given attribute name, false otherwise.
         */
        public static boolean contains(String name) {
            return fromString(name).isPresent();
        }
    }
}
