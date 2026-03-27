.class public Lcom/example/MyClass;
.super Ljava/lang/Object;

.method public static example()[I
    .registers 2

    const/4 v0, 0x3
    new-array v0, v0, [I

    fill-array-data v0, :array_data_0

    return-object v0

    :array_data_0
    .array-data 4
        0x1
        0x2
        0x3
    .end array-data
.end method