.class public Lcom/example/MyClass;
.super Ljava/lang/Object;

.method public static testLiterals()V
    .locals 8

    # int
    const/4 v0, 0x7

    # long
    const-wide/16 v1, 0x8

    # float (1.0f)
    const/high16 v3, 0x3f80

    # double (1.0)
    const-wide v4, 0x3ff0000000000000L

    # boolean (true)
    const/4 v6, 0x1

    # byte (0x7f = 127)
    const/16 v7, 0x7f

    # short (0x1234 = 4660)
    const/16 v0, 0x1234

    # char ('A' = 65)
    const/16 v1, 0x41

    return-void
.end method