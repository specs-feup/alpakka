.class public Lcom/example/LabelRefTest;
.super Ljava/lang/Object;

.method public static test(I)I
    .registers 4

    # p0 is the input value (in a real file you may see it as p0 or vX depending on style)

    ############################################################
    # 1) unconditional label_ref
    ############################################################
    goto :entry

    const/4 v0, -0x1      # unreachable, just here to ensure parser sees layout
    return v0

    :entry
    const/4 v0, 0x0

    ############################################################
    # 2) conditional label_ref (21t/22t family style usage)
    ############################################################
    if-ltz p0, :negative
    if-eqz p0, :zero
    if-gtz p0, :positive

    # fallback, should not happen
    goto :done

    :negative
    const/4 v0, -0x1
    goto :done

    :zero
    const/4 v0, 0x0
    goto :done

    :positive
    ############################################################
    # 3) packed-switch label_ref
    ############################################################
    packed-switch p0, :pswitch_data

    # default path if no switch case matches
    const/4 v0, 0x7
    goto :done

    :case_one
    const/4 v0, 0x1
    goto :done

    :case_two
    const/4 v0, 0x2
    goto :done

    :case_three
    const/4 v0, 0x3
    goto :done

    ############################################################
    # 4) sparse-switch payload and case label_refs
    ############################################################
    :after_packed
    sparse-switch p0, :sswitch_data

    const/16 v0, 0x63     # default = 99
    goto :done

    :s_case_ten
    const/16 v0, 0xa
    goto :done

    :s_case_hundred
    const/16 v0, 0x64
    goto :done

    ############################################################
    # 5) try/catch label_refs:
    #    - range start label_ref
    #    - range end label_ref
    #    - handler label_ref
    ############################################################
    :try_demo
    :try_start
    const/4 v1, 0x1
    div-int/2addr v1, p0   # may throw if p0 == 0
    move v0, v1
    goto :after_try
    :try_end

    .catch Ljava/lang/ArithmeticException; {:try_start .. :try_end} :catch_arith

    :catch_arith
    const/16 v0, -0xa

    :after_try
    goto :done

    ############################################################
    # shared return
    ############################################################
    :done
    return v0

    ############################################################
    # packed-switch payload
    ############################################################
    :pswitch_data
    .packed-switch 0x1
        :case_one
        :case_two
        :case_three
    .end packed-switch

    ############################################################
    # sparse-switch payload
    ############################################################
    :sswitch_data
    .sparse-switch
        0xa  -> :s_case_ten
        0x64 -> :s_case_hundred
    .end sparse-switch
.end method