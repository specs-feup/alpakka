.class public final LFormat10xTest;
.super Ljava/lang/Object;

# Comprehensive test file for standard format 10x instructions:
#   - nop
#   - return-void
#
# Notes:
# - This is standard DEX/smali, not odex-only syntax.
# - The goal is to hit common compiler/assembler cases:
#     * bare return-void
#     * nop at method start / middle / end
#     * consecutive nops
#     * nops around labels
#     * nops inside try/catch regions
#     * nops in branches and unreachable regions

.method public constructor <init>()V
    .registers 1

    # Standard constructor path, plus a nop before return-void
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V
    nop
    return-void
.end method

.method public static empty()V
    .registers 0

    # Smallest useful return-void-only method
    return-void
.end method

.method public static singleNop()V
    .registers 0

    nop
    return-void
.end method

.method public static leadingAndTrailingNop()V
    .registers 0

    nop
    nop
    return-void
    # Unreachable trailing nop is intentionally omitted here because
    # some pipelines normalize or reject code after terminal returns.
.end method

.method public static manyNops()V
    .registers 0

    nop
    nop
    nop
    nop
    nop
    return-void
.end method

.method public static labelsAroundNop()V
    .registers 0

    :start
    nop

    :middle
    nop

    :end
    return-void
.end method

.method public static branchOverNop()V
    .registers 1

    const/4 v0, 0x0
    if-nez v0, :target

    # Fallthrough path contains a nop
    nop
    return-void

    :target
    nop
    return-void
.end method

.method public static tryCatchWithNop()V
    .registers 1

    :try_start
    nop
    const/4 v0, 0x0
    div-int/lit8 v0, v0, 0x1
    nop
    :try_end
    return-void

    .catch Ljava/lang/Throwable; {:try_start .. :try_end} :handler

    :handler
    move-exception v0
    nop
    return-void
.end method

.method public static nestedFlow()V
    .registers 1

    const/4 v0, 0x1

    :loop
    nop
    add-int/lit8 v0, v0, -0x1
    if-gtz v0, :loop

    nop
    return-void
.end method

.method public static packedStructureSmoke()V
    .registers 1

    const/4 v0, 0x1
    packed-switch v0, :pswitch_data

    nop
    return-void

    :case_0
    nop
    return-void

    :case_1
    nop
    return-void

    :pswitch_data
    .packed-switch 0x0
        :case_0
        :case_1
    .end packed-switch
.end method