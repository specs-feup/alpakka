.class public Lfixture/InstructionFormat22s;
.super Ljava/lang/Object;

.method public static test()V
    .registers 2

    const/4 v1, 0x2
    add-int/lit16 v0, v1, 0x1234

    return-void
.end method
