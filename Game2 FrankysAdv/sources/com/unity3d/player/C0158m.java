package com.unity3d.player;

/* renamed from: com.unity3d.player.m */
final class C0158m {

    /* renamed from: a */
    private static boolean f260a = false;

    /* renamed from: b */
    private boolean f261b = false;

    /* renamed from: c */
    private boolean f262c = false;

    /* renamed from: d */
    private boolean f263d = true;

    /* renamed from: e */
    private boolean f264e = false;

    C0158m() {
    }

    /* renamed from: a */
    static void m155a() {
        f260a = true;
    }

    /* renamed from: b */
    static void m156b() {
        f260a = false;
    }

    /* renamed from: c */
    static boolean m157c() {
        return f260a;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public final void mo444a(boolean z) {
        this.f261b = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public final void mo445b(boolean z) {
        this.f263d = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: c */
    public final void mo446c(boolean z) {
        this.f264e = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: d */
    public final void mo447d(boolean z) {
        this.f262c = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: d */
    public final boolean mo448d() {
        return this.f263d;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: e */
    public final boolean mo449e() {
        return this.f264e;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: e */
    public final boolean mo450e(boolean z) {
        if (f260a) {
            return (z || this.f261b) && !this.f263d && !this.f262c;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: f */
    public final boolean mo451f() {
        return this.f262c;
    }

    public final String toString() {
        return super.toString();
    }
}
