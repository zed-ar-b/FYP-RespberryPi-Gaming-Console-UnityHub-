package com.unity3d.player;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

/* renamed from: com.unity3d.player.k */
final class C0152k {

    /* renamed from: a */
    private Context f255a;

    /* renamed from: b */
    private C0154b f256b;

    /* renamed from: com.unity3d.player.k$a */
    public interface C0153a {
        /* renamed from: b */
        void mo194b();
    }

    /* renamed from: com.unity3d.player.k$b */
    private class C0154b extends ContentObserver {

        /* renamed from: b */
        private C0153a f258b;

        public C0154b(Handler handler, C0153a aVar) {
            super(handler);
            this.f258b = aVar;
        }

        public final boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        public final void onChange(boolean z) {
            C0153a aVar = this.f258b;
            if (aVar != null) {
                aVar.mo194b();
            }
        }
    }

    public C0152k(Context context) {
        this.f255a = context;
    }

    /* renamed from: a */
    public final void mo410a() {
        if (this.f256b != null) {
            this.f255a.getContentResolver().unregisterContentObserver(this.f256b);
            this.f256b = null;
        }
    }

    /* renamed from: a */
    public final void mo411a(C0153a aVar, String str) {
        this.f256b = new C0154b(new Handler(Looper.getMainLooper()), aVar);
        this.f255a.getContentResolver().registerContentObserver(Settings.System.getUriFor(str), true, this.f256b);
    }
}
