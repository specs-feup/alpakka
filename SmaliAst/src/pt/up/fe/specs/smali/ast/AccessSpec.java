package pt.up.fe.specs.smali.ast;

public enum AccessSpec {
	
	PUBLIC("public"),
	PRIVATE("private"),
	PROTECTED("protected"),
	STATIC("static"),
	FINAL("final"),
	SYNCHRONIZED("synchronized"),
	BRIDGE("bridge"),
	VARARGS("varargs"),
	NATIVE("native"),
	ABSTRACT("abstract"), 
	STRICTFP("strictfp"),
	SYNTHETIC("synthetic"),
	CONSTRUCTOR("constructor"),
	DECLARED_SYNCHRONIZED("declared-synchronized"),
	INTERFACE("interface"),
	ENUM("enum"),
	ANNOTATION("annotation"),
	VOLATILE("volatile"),
	TRANSIENT("transient");

	private String label;
	
	private AccessSpec(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static AccessSpec getFromLabel(String label) {
		for (AccessSpec accessSpec : AccessSpec.values()) {
			if (accessSpec.getLabel().equals(label)) {
				return accessSpec;
			}
		}
		return null;
	}
}
