.class public Ltest/FieldCases;
.super Ljava/lang/Object;

# ----------------------------------------
# Basic instance/static fields
# ----------------------------------------

.field public instanceInt:I
.field private static counter:I
.field protected name:Ljava/lang/String;
.field packageVisible:Z

# ----------------------------------------
# Final/static-final primitives and strings
# Good for testing constant parsing
# ----------------------------------------

.field public static final CONST_INT:I = 123
.field public static final CONST_NEG_INT:I = -7
.field public static final CONST_LONG:J = 0x1122334455667788L
.field public static final CONST_FLOAT:F = 3.5f
.field public static final CONST_DOUBLE:D = -9.25
.field public static final CONST_STRING:Ljava/lang/String; = "hello"
.field public static final CONST_BOOL_TRUE:Z = true
.field public static final CONST_BOOL_FALSE:Z = false

# ----------------------------------------
# Visibility / storage / metadata flags
# ----------------------------------------

.field public final finalField:I
.field private volatile volField:I
.field private transient transientField:Ljava/lang/Object;
.field public synthetic syntheticField:I

# ----------------------------------------
# Wide and reference types
# ----------------------------------------

.field public longValue:J
.field public doubleValue:D
.field public objectValue:Ljava/lang/Object;
.field public stringArray:[Ljava/lang/String;
.field public int2DArray:[[I

# ----------------------------------------
# Inner-class style / compiler-ish names
# ----------------------------------------

.field final this$0:Ltest/Outer;
.field private $cached:Ljava/lang/String;

# ----------------------------------------
# Enum-like fields
# ----------------------------------------

.field public static final enum RED:Ltest/Color;
.field public static final enum BLUE:Ltest/Color;
.field private static final synthetic $VALUES:[Ltest/Color;

# ----------------------------------------
# Field with annotation
# ----------------------------------------

.field public annotated:I
    .annotation runtime Ljava/lang/Deprecated;
    .end annotation
.end field

# ----------------------------------------
# Field with annotation element values
# ----------------------------------------

.field public tagged:Ljava/lang/String;
    .annotation runtime Ltest/MyFieldAnnotation;
        value = "tagged-field"
        count = 3
    .end annotation
.end field

# ----------------------------------------
# Constructor just so the class is usable
# ----------------------------------------

.method public constructor <init>()V
    .registers 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V
    return-void
.end method