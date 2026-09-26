package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import com.unity3d.player.C0159n;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* renamed from: com.unity3d.player.o */
final class C0162o {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public UnityPlayer f294a = null;
    /* access modifiers changed from: private */

    /* renamed from: b */
    public Context f295b = null;

    /* renamed from: c */
    private C0169a f296c;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public final Semaphore f297d = new Semaphore(0);
    /* access modifiers changed from: private */

    /* renamed from: e */
    public final Lock f298e = new ReentrantLock();
    /* access modifiers changed from: private */

    /* renamed from: f */
    public C0159n f299f = null;
    /* access modifiers changed from: private */

    /* renamed from: g */
    public int f300g = 2;

    /* renamed from: h */
    private boolean f301h = false;
    /* access modifiers changed from: private */

    /* renamed from: i */
    public boolean f302i = false;

    /* renamed from: com.unity3d.player.o$a */
    public interface C0169a {
        /* renamed from: a */
        void mo305a();
    }

    C0162o(UnityPlayer unityPlayer) {
        this.f294a = unityPlayer;
    }

    /* access modifiers changed from: private */
    /* renamed from: d */
    public void m181d() {
        C0159n nVar = this.f299f;
        if (nVar != null) {
            this.f294a.removeViewFromPlayer(nVar);
            this.f302i = false;
            this.f299f.destroyPlayer();
            this.f299f = null;
            C0169a aVar = this.f296c;
            if (aVar != null) {
                aVar.mo305a();
            }
        }
    }

    /* renamed from: a */
    public final void mo481a() {
        this.f298e.lock();
        C0159n nVar = this.f299f;
        if (nVar != null) {
            if (this.f300g == 0) {
                nVar.CancelOnPrepare();
            } else if (this.f302i) {
                boolean a = nVar.mo454a();
                this.f301h = a;
                if (!a) {
                    this.f299f.pause();
                }
            }
        }
        this.f298e.unlock();
    }

    /* renamed from: a */
    public final boolean mo482a(Context context, String str, int i, int i2, int i3, boolean z, long j, long j2, C0169a aVar) {
        this.f298e.lock();
        this.f296c = aVar;
        this.f295b = context;
        this.f297d.drainPermits();
        this.f300g = 2;
        final String str2 = str;
        final int i4 = i;
        final int i5 = i2;
        final int i6 = i3;
        final boolean z2 = z;
        final long j3 = j;
        final long j4 = j2;
        runOnUiThread(new Runnable() {
            public final void run() {
                if (C0162o.this.f299f != null) {
                    C0139f.Log(5, "Video already playing");
                    int unused = C0162o.this.f300g = 2;
                    C0162o.this.f297d.release();
                    return;
                }
                C0159n unused2 = C0162o.this.f299f = new C0159n(C0162o.this.f295b, str2, i4, i5, i6, z2, j3, j4, new C0159n.C0160a() {
                    /* renamed from: a */
                    public final void mo478a(int i) {
                        C0162o.this.f298e.lock();
                        int unused = C0162o.this.f300g = i;
                        if (i == 3 && C0162o.this.f302i) {
                            C0162o.this.runOnUiThread(new Runnable() {
                                public final void run() {
                                    C0162o.this.m181d();
                                    C0162o.this.f294a.resume();
                                }
                            });
                        }
                        if (i != 0) {
                            C0162o.this.f297d.release();
                        }
                        C0162o.this.f298e.unlock();
                    }
                });
                if (C0162o.this.f299f != null) {
                    C0162o.this.f294a.addView(C0162o.this.f299f);
                }
            }
        });
        boolean z3 = false;
        try {
            this.f298e.unlock();
            this.f297d.acquire();
            this.f298e.lock();
            if (this.f300g != 2) {
                z3 = true;
            }
        } catch (InterruptedException unused) {
        }
        runOnUiThread(new Runnable() {
            public final void run() {
                C0162o.this.f294a.pause();
            }
        });
        runOnUiThread((!z3 || this.f300g == 3) ? new Runnable() {
            public final void run() {
                C0162o.this.m181d();
                C0162o.this.f294a.resume();
            }
        } : new Runnable() {
            public final void run() {
                if (C0162o.this.f299f != null) {
                    C0162o.this.f294a.addViewToPlayer(C0162o.this.f299f, true);
                    boolean unused = C0162o.this.f302i = true;
                    C0162o.this.f299f.requestFocus();
                }
            }
        });
        this.f298e.unlock();
        return z3;
    }

    /* renamed from: b */
    public final void mo483b() {
        this.f298e.lock();
        C0159n nVar = this.f299f;
        if (nVar != null && this.f302i && !this.f301h) {
            nVar.start();
        }
        this.f298e.unlock();
    }

    /* renamed from: c */
    public final void mo484c() {
        this.f298e.lock();
        C0159n nVar = this.f299f;
        if (nVar != null) {
            nVar.updateVideoLayout();
        }
        this.f298e.unlock();
    }

    /* access modifiers changed from: protected */
    public final void runOnUiThread(Runnable runnable) {
        Context context = this.f295b;
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(runnable);
        } else {
            C0139f.Log(5, "Not running from an Activity; Ignoring execution request...");
        }
    }
}
