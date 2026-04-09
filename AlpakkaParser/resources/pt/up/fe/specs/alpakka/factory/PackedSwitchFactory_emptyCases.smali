.class public Lfixture/PackedSwitchFactory_emptyCases;
.super Ljava/lang/Object;

.method public static test(I)V
    .registers 2

    packed-switch v0, :pswitch_data

    :case_0

    :case_1

    :pswitch_data
    .packed-switch 0x0
        :case_0
        :case_1
    .end packed-switch
.end method
