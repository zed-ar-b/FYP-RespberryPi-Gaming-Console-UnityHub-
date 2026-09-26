package com.unity3d.player;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

/* renamed from: com.unity3d.player.k */
final class C0085k {

    /* renamed from: a */
    private Context f256a;

    /* renamed from: b */
    private C0087b f257b;

    /* renamed from: com.unity3d.player.k$a */
    public interface C0086a {
        /* renamed from: b */
        void mo96b();
    }

    /* renamed from: com.unity3d.player.k$b */
    private class C0087b extends ContentObserver {

        /* renamed from: b */
        private C0086a f259b;

        public C0087b(Handler handler, C0086a aVar) {
            super(handler);
            this.f259b = aVar;
        }

        public final boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        public final void onChange(boolean z) {
            C0086a aVar = this.f259b;
            if (aVar != null) {
                aVar.mo96b();
            }
        }
    }

    public C0085k(Context context) {
        this.f256a = context;
    }

    /* renamed from: a */
    public final void mo312a() {
        if (this.f257b != null) {
            this.f256a.getContentResolver().unregisterContentObserver(this.f257b);
            this.f257b = null;
        }
    }

    /* renamed from: a */
    public final void mo313a(C0086a aVar, String str) {
        this.f257b = new C0087b(new Handler(Looper.getMainLooper()), aVar);
        this.f256a.getContentResolver().registerContentObserver(Settings.System.getUriFor(str), true, this.f257b);
    }
}
