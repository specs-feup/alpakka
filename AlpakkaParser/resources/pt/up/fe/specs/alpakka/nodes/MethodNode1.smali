.class public Lfixture/MethodNode1;
.super Ljava/lang/Object;

.field private value:I

.method public static build(Ljava/lang/String;[I)Ljava/lang/String;
    .locals 2

    .param p0, "name"
    .end param
    .param p1, "values"
        .annotation runtime Ljava/lang/Deprecated;
        .end annotation
    .end param

    .prologue
    .line 10
    const/4 v0, 0x0

    .local v0, "temp":I
    .line 11
    const-string v1, "done"

    .end local v0
    .restart local v0
    .epilogue
    return-object v1
.end method
