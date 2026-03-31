.class public Lcom/example/TestFormat11x;
.super Ljava/lang/Object;

# =========================
# Constructor
# =========================
.method public constructor <init>()V
    .registers 1

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V
    return-void
.end method

# =========================
# Helpers used by tests
# =========================

.method public static getInt()I
    .registers 1

    const/4 v0, 0x7
    return v0
.end method

.method public static getLong()J
    .registers 2

    const-wide/16 v0, 0x1234
    return-wide v0
.end method

.method public static getString()Ljava/lang/String;
    .registers 1

    const-string v0, "hello"
    return-object v0
.end method

.method public static alwaysThrow()V
    .registers 2

    new-instance v0, Ljava/lang/RuntimeException;
    const-string v1, "boom"
    invoke-direct {v0, v1}, Ljava/lang/RuntimeException;-><init>(Ljava/lang/String;)V
    throw v0
.end method

# =========================
# 1) move-result + return
# =========================
.method public static testMoveResult()I
    .registers 1

    invoke-static {}, Lcom/example/TestFormat11x;->getInt()I
    move-result v0

    return v0
.end method

# =========================
# 2) move-result-wide + return-wide
# =========================
.method public static testMoveResultWide()J
    .registers 2

    invoke-static {}, Lcom/example/TestFormat11x;->getLong()J
    move-result-wide v0

    return-wide v0
.end method

# =========================
# 3) move-result-object + return-object
# =========================
.method public static testMoveResultObject()Ljava/lang/String;
    .registers 1

    invoke-static {}, Lcom/example/TestFormat11x;->getString()Ljava/lang/String;
    move-result-object v0

    return-object v0
.end method

# =========================
# 4) monitor-enter / monitor-exit
#    also uses move-result-object
# =========================
.method public static testMonitor()V
    .registers 2

    new-instance v0, Ljava/lang/Object;
    invoke-direct {v0}, Ljava/lang/Object;-><init>()V

    monitor-enter v0
    :try_start
    invoke-static {}, Lcom/example/TestFormat11x;->getString()Ljava/lang/String;
    move-result-object v1
    monitor-exit v0
    return-void

    :try_end
    .catchall {:try_start .. :try_end} :catchall

    :catchall
    move-exception v1
    monitor-exit v0
    throw v1
.end method

# =========================
# 5) move-exception + return-object
# =========================
.method public static testMoveException()Ljava/lang/String;
    .registers 2

    :try_start
    invoke-static {}, Lcom/example/TestFormat11x;->alwaysThrow()V
    const-string v0, "unreachable"
    return-object v0
    :try_end
    .catch Ljava/lang/RuntimeException; {:try_start .. :try_end} :handler

    :handler
    move-exception v0

    invoke-virtual {v0}, Ljava/lang/RuntimeException;->getMessage()Ljava/lang/String;
    move-result-object v1

    return-object v1
.end method

# =========================
# 6) direct throw test
# =========================
.method public static testThrow()V
    .registers 2

    new-instance v0, Ljava/lang/IllegalStateException;
    const-string v1, "direct throw"
    invoke-direct {v0, v1}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw v0
.end method