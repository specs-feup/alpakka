.class public Lfixture/PackedSwitchFactory_threeCases;
.super Ljava/lang/Object;

.method public static test(I)V
    .registers 2

    packed-switch v0, :pswitch_data

    :case_0
    return-void

    :case_1
    return-void

    :case_2
    return-void

    :pswitch_data
    .packed-switch 0x0
        :case_0
        :case_1
        :case_2
    .end packed-switch
.end method
