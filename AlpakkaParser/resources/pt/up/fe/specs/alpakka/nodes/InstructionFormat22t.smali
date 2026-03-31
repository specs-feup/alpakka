.class public Lfixture/InstructionFormat22t;
.super Ljava/lang/Object;

.method public static test(II)V
    .registers 2

    if-eq p0, p1, :equal

    return-void

    :equal
    return-void
.end method
