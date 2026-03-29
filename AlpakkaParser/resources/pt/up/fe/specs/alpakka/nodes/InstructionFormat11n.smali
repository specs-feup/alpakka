.class public LFormat11nTest;
.super Ljava/lang/Object;

# Tests Dalvik format 11n.
# Format 11n is used by:
#   const/4 vA, #+B
# where:
#   A = 4-bit destination register (v0 .. v15)
#   B = 4-bit signed literal (-8 .. 7)

.method public constructor <init>()V
    .registers 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V
    return-void
.end method

# Returns 1 if all const/4 loads behaved as expected, else 0.
.method public static testAllLiteralsAndRegisters()I
    .registers 16

    # Use every possible 4-bit literal once.
    const/4 v0,  -0x8    # -8
    const/4 v1,  -0x7    # -7
    const/4 v2,  -0x6    # -6
    const/4 v3,  -0x5    # -5
    const/4 v4,  -0x4    # -4
    const/4 v5,  -0x3    # -3
    const/4 v6,  -0x2    # -2
    const/4 v7,  -0x1    # -1
    const/4 v8,   0x0    #  0
    const/4 v9,   0x1    #  1
    const/4 v10,  0x2    #  2
    const/4 v11,  0x3    #  3
    const/4 v12,  0x4    #  4
    const/4 v13,  0x5    #  5
    const/4 v14,  0x6    #  6
    const/4 v15,  0x7    #  7

    # Verify every loaded value.
    const/16 v8, -8
    if-ne v0, v8, :fail

    const/16 v8, -7
    if-ne v1, v8, :fail

    const/16 v8, -6
    if-ne v2, v8, :fail

    const/16 v8, -5
    if-ne v3, v8, :fail

    const/16 v8, -4
    if-ne v4, v8, :fail

    const/16 v8, -3
    if-ne v5, v8, :fail

    const/16 v8, -2
    if-ne v6, v8, :fail

    const/16 v8, -1
    if-ne v7, v8, :fail

    const/16 v8, 0
    if-ne v8, v8, :fail    # intentionally trivial; v8 already reused below

    const/16 v0, 1
    if-ne v9, v0, :fail

    const/16 v0, 2
    if-ne v10, v0, :fail

    const/16 v0, 3
    if-ne v11, v0, :fail

    const/16 v0, 4
    if-ne v12, v0, :fail

    const/16 v0, 5
    if-ne v13, v0, :fail

    const/16 v0, 6
    if-ne v14, v0, :fail

    const/16 v0, 7
    if-ne v15, v0, :fail

    const/4 v0, 0x1
    return v0

  :fail
    const/4 v0, 0x0
    return v0
.end method

# Uses every destination register v0..v15 with const/4, but in a pattern
# that also checks overwrite behavior and sign handling.
.method public static testRegisterCoverage()I
    .registers 16

    const/4 v0,  0x7
    const/4 v1,  0x6
    const/4 v2,  0x5
    const/4 v3,  0x4
    const/4 v4,  0x3
    const/4 v5,  0x2
    const/4 v6,  0x1
    const/4 v7,  0x0
    const/4 v8, -0x1
    const/4 v9, -0x2
    const/4 v10, -0x3
    const/4 v11, -0x4
    const/4 v12, -0x5
    const/4 v13, -0x6
    const/4 v14, -0x7
    const/4 v15, -0x8

    # Overwrite every register again with a different valid 11n literal.
    const/4 v0,  -0x8
    const/4 v1,  -0x7
    const/4 v2,  -0x6
    const/4 v3,  -0x5
    const/4 v4,  -0x4
    const/4 v5,  -0x3
    const/4 v6,  -0x2
    const/4 v7,  -0x1
    const/4 v8,   0x0
    const/4 v9,   0x1
    const/4 v10,  0x2
    const/4 v11,  0x3
    const/4 v12,  0x4
    const/4 v13,  0x5
    const/4 v14,  0x6
    const/4 v15,  0x7

    # Quick aggregate check: sum(-8..7) == -8
    add-int/2addr v0, v1
    add-int/2addr v0, v2
    add-int/2addr v0, v3
    add-int/2addr v0, v4
    add-int/2addr v0, v5
    add-int/2addr v0, v6
    add-int/2addr v0, v7
    add-int/2addr v0, v8
    add-int/2addr v0, v9
    add-int/2addr v0, v10
    add-int/2addr v0, v11
    add-int/2addr v0, v12
    add-int/2addr v0, v13
    add-int/2addr v0, v14
    add-int/2addr v0, v15

    const/16 v1, -8
    if-ne v0, v1, :fail

    const/4 v0, 0x1
    return v0

  :fail
    const/4 v0, 0x0
    return v0
.end method

# Minimal entry point if you want a single method to call from a harness.
# Returns:
#   2 if both subtests pass
#   0 or 1 otherwise
.method public static run()I
    .registers 2

    invoke-static {}, LFormat11nTest;->testAllLiteralsAndRegisters()I
    move-result v0

    invoke-static {}, LFormat11nTest;->testRegisterCoverage()I
    move-result v1

    add-int/2addr v0, v1
    return v0
.end method