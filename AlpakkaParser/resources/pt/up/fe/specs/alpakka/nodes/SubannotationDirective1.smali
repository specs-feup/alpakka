.class public Lfixture/SubannotationDirective1;
.super Ljava/lang/Object;

.annotation runtime Lfixture/MyAnnotation;
    numbers = {
        0x1,
        0x2
    }
    nested = .subannotation Lfixture/Nested;
        name = "inner"
        nothing = null
    .end subannotation
    enumValue = .enum Ljava/lang/annotation/RetentionPolicy;->RUNTIME:Ljava/lang/annotation/RetentionPolicy;
    fieldValue = Ljava/lang/Integer;->MAX_VALUE:I
    methodValue = Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;
.end annotation
