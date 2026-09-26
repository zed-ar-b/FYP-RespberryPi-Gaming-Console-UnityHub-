package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import com.unity3d.player.C0090n;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* renamed from: com.unity3d.player.o */
final class C0093o {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public UnityPlayer f295a = null;
    /* access modifiers changed from: private */

    /* renamed from: b */
    public Context f296b = null;

    /* renamed from: c */
    private C0100a f297c;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public final Semaphore f298d = new Semaphore(0);
    /* access modifiers changed from: private */

    /* renamed from: e */
    public final Lock f299e = new ReentrantLock();
    /* access modifiers changed from: private */

    /* renamed from: f */
    public C0090n f300f = null;
    /* access modifiers changed from: private */

    /* renamed from: g */
    public int f301g = 2;

    /* renamed from: h */
    private boolean f302h = false;
    /* access modifiers changed from: private */

    /* renamed from: i */
    public boolean f303i = false;

    /* renamed from: com.unity3d.player.o$a */
    public interface C0100a {
        /* renamed from: a */
        void mo179a();
    }

    C0093o(UnityPlayer unityPlayer) {
        this.f295a = unityPlayer;
    }

    /* access modifiers changed from: private */
    /* renamed from: d */
    public void m181d() {
        C0090n nVar = this.f300f;
        if (nVar != null) {
            this.f295a.removeViewFromPlayer(nVar);
            this.f303i = false;
            this.f300f.destroyPlayer();
            this.f300f = null;
            C0100a aVar = this.f297c;
            if (aVar != null) {
                aVar.mo179a();
            }
        }
    }

    /* renamed from: a */
    public final void mo355a() {
        this.f299e.lock();
        C0090n nVar = this.f300f;
        if (nVar != null) {
            if (this.f301g == 0) {
                nVar.CancelOnPrepare();
            } else if (this.f303i) {
                boolean a = nVar.mo328a();
                this.f302h = a;
                if (!a) {
                    this.f300f.pause();
                }
            }
        }
        this.f299e.unlock();
    }

    /* renamed from: a */
    public final boolean mo356a(Context context, String str, int i, int i2, int i3, boolean z, long j, long j2, C0100a aVar) {
        this.f299e.lock();
        this.f297c = aVar;
        this.f296b = context;
        this.f298d.drainPermits();
        this.f301g = 2;
        final String str2 = str;
        final int i4 = i;
        final int i5 = i2;
        final int i6 = i3;
        final boolean z2 = z;
        final long j3 = j;
        final long j4 = j2;
        runOnUiThread(new Runnable() {
            public final void run() {
                if (C0093o.this.f300f != null) {
                    C0070f.Log(5, "Video already playing");
                    int unused = C0093o.this.f301g = 2;
                    C0093o.this.f298d.release();
                    return;
                }
                C0090n unused2 = C0093o.this.f300f = new C0090n(C0093o.this.f296b, str2, i4, i5, i6, z2, j3, j4, new C0090n.C0091a() {
                    /* renamed from: a */
                    public final void mo352a(int i) {
                        C0093o.this.f299e.lock();
                        int unused = C0093o.this.f301g = i;
                        if (i == 3 && C0093o.this.f303i) {
                            C0093o.this.runOnUiThread(new Runnable() {
                                public final void run() {
                                    C0093o.this.m181d();
                                    C0093o.this.f295a.resume();
                                }
                            });
                        }
                        if (i != 0) {
                            C0093o.this.f298d.release();
                        }
                        C0093o.this.f299e.unlock();
                    }
                });
                if (C0093o.this.f300f != null) {
                    C0093o.this.f295a.addView(C0093o.this.f300f);
                }
            }
        });
        boolean z3 = false;
        try {
            this.f299e.unlock();
            this.f298d.acquire();
            this.f299e.lock();
            if (this.f301g != 2) {
                z3 = true;
            }
        } catch (InterruptedException unused) {
        }
        runOnUiThread(new Runnable() {
            public final void run() {
                C0093o.this.f295a.pause();
            }
        });
        runOnUiThread((!z3 || this.f301g == 3) ? new Runnable() {
            public final void run() {
                C0093o.this.m181d();
                C0093o.this.f295a.resume();
            }
        } : new Runnable() {
            public final void run() {
                if (C0093o.this.f300f != null) {
                    C0093o.this.f295a.addViewToPlayer(C0093o.this.f300f, true);
                    boolean unused = C0093o.this.f303i = true;
                    C0093o.this.f300f.requestFocus();
                }
            }
        });
        this.f299e.unlock();
        return z3;
    }

    /* renamed from: b */
    public final void mo357b() {
        this.f299e.lock();
        C0090n nVar = this.f300f;
        if (nVar != null && this.f303i && !this.f302h) {
            nVar.start();
        }
        this.f299e.unlock();
    }

    /* renamed from: c */
    public final void mo358c() {
        this.f299e.lock();
        C0090n nVar = this.f300f;
        if (nVar != null) {
            nVar.updateVideoLayout();
        }
        this.f299e.unlock();
    }

    /* access modifiers changed from: protected */
    public final void runOnUiThread(Runnable runnable) {
        Context context = this.f296b;
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(runnable);
        } else {
            C0070f.Log(5, "Not running from an Activity; Ignoring execution request...");
        }
    }
}
