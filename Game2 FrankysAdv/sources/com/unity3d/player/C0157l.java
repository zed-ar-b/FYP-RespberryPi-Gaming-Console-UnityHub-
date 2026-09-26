package com.unity3d.player;

import java.lang.Thread;

/* renamed from: com.unity3d.player.l */
final class C0157l implements Thread.UncaughtExceptionHandler {

    /* renamed from: a */
    private volatile Thread.UncaughtExceptionHandler f259a;

    C0157l() {
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public final synchronized boolean mo442a() {
        boolean z;
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler == this) {
            z = false;
        } else {
            this.f259a = defaultUncaughtExceptionHandler;
            Thread.setDefaultUncaughtExceptionHandler(this);
            z = true;
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        r7.f259a.uncaughtException(r8, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x00a7, code lost:
        return;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x00a1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized void uncaughtException(java.lang.Thread r8, java.lang.Throwable r9) {
        /*
            r7 = this;
            monitor-enter(r7)
            java.lang.Error r0 = new java.lang.Error     // Catch:{ all -> 0x00a1 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00a1 }
            r1.<init>()     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = "FATAL EXCEPTION [%s]\n"
            r3 = 1
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00a1 }
            java.lang.String r5 = r8.getName()     // Catch:{ all -> 0x00a1 }
            r6 = 0
            r4[r6] = r5     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00a1 }
            r1.append(r2)     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = "Unity version     : %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00a1 }
            java.lang.String r5 = "2021.3.25f1"
            r4[r6] = r5     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00a1 }
            r1.append(r2)     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = "Device model      : %s %s\n"
            r4 = 2
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x00a1 }
            java.lang.String r5 = android.os.Build.MANUFACTURER     // Catch:{ all -> 0x00a1 }
            r4[r6] = r5     // Catch:{ all -> 0x00a1 }
            java.lang.String r5 = android.os.Build.MODEL     // Catch:{ all -> 0x00a1 }
            r4[r3] = r5     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00a1 }
            r1.append(r2)     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = "Device fingerprint: %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00a1 }
            java.lang.String r5 = android.os.Build.FINGERPRINT     // Catch:{ all -> 0x00a1 }
            r4[r6] = r5     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00a1 }
            r1.append(r2)     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = "Build Type        : %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00a1 }
            java.lang.String r5 = "Release"
            r4[r6] = r5     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00a1 }
            r1.append(r2)     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = "Scripting Backend : %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00a1 }
            java.lang.String r5 = "Mono"
            r4[r6] = r5     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00a1 }
            r1.append(r2)     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = "ABI               : %s\n"
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x00a1 }
            java.lang.String r5 = android.os.Build.CPU_ABI     // Catch:{ all -> 0x00a1 }
            r4[r6] = r5     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = java.lang.String.format(r2, r4)     // Catch:{ all -> 0x00a1 }
            r1.append(r2)     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = "Strip Engine Code : %s\n"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x00a1 }
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r6)     // Catch:{ all -> 0x00a1 }
            r3[r6] = r4     // Catch:{ all -> 0x00a1 }
            java.lang.String r2 = java.lang.String.format(r2, r3)     // Catch:{ all -> 0x00a1 }
            r1.append(r2)     // Catch:{ all -> 0x00a1 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00a1 }
            r0.<init>(r1)     // Catch:{ all -> 0x00a1 }
            java.lang.StackTraceElement[] r1 = new java.lang.StackTraceElement[r6]     // Catch:{ all -> 0x00a1 }
            r0.setStackTrace(r1)     // Catch:{ all -> 0x00a1 }
            r0.initCause(r9)     // Catch:{ all -> 0x00a1 }
            java.lang.Thread$UncaughtExceptionHandler r1 = r7.f259a     // Catch:{ all -> 0x00a1 }
            r1.uncaughtException(r8, r0)     // Catch:{ all -> 0x00a1 }
            monitor-exit(r7)
            return
        L_0x00a1:
            java.lang.Thread$UncaughtExceptionHandler r0 = r7.f259a     // Catch:{ all -> 0x00a8 }
            r0.uncaughtException(r8, r9)     // Catch:{ all -> 0x00a8 }
            monitor-exit(r7)
            return
        L_0x00a8:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.C0157l.uncaughtException(java.lang.Thread, java.lang.Throwable):void");
    }
}
