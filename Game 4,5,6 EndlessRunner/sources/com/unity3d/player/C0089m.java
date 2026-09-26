package com.unity3d.player;

/* renamed from: com.unity3d.player.m */
final class C0089m {

    /* renamed from: a */
    private static boolean f261a = false;

    /* renamed from: b */
    private boolean f262b = false;

    /* renamed from: c */
    private boolean f263c = false;

    /* renamed from: d */
    private boolean f264d = true;

    /* renamed from: e */
    private boolean f265e = false;

    C0089m() {
    }

    /* renamed from: a */
    static void m155a() {
        f261a = true;
    }

    /* renamed from: b */
    static void m156b() {
        f261a = false;
    }

    /* renamed from: c */
    static boolean m157c() {
        return f261a;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public final void mo318a(boolean z) {
        this.f262b = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public final void mo319b(boolean z) {
        this.f264d = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: c */
    public final void mo320c(boolean z) {
        this.f265e = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: d */
    public final void mo321d(boolean z) {
        this.f263c = z;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: d */
    public final boolean mo322d() {
        return this.f264d;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: e */
    public final boolean mo323e() {
        return this.f265e;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: e */
    public final boolean mo324e(boolean z) {
        if (f261a) {
            return (z || this.f262b) && !this.f264d && !this.f263c;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: f */
    public final boolean mo325f() {
        return this.f263c;
    }

    public final String toString() {
        return super.toString();
    }
}
