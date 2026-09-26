package com.unity3d.player;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

/* renamed from: com.unity3d.player.k */
final class C0154k {

    /* renamed from: a */
    private Context f255a;

    /* renamed from: b */
    private C0156b f256b;

    /* renamed from: com.unity3d.player.k$a */
    public interface C0155a {
        /* renamed from: b */
        void mo222b();
    }

    /* renamed from: com.unity3d.player.k$b */
    private class C0156b extends ContentObserver {

        /* renamed from: b */
        private C0155a f258b;

        public C0156b(Handler handler, C0155a aVar) {
            super(handler);
            this.f258b = aVar;
        }

        public final boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        public final void onChange(boolean z) {
            C0155a aVar = this.f258b;
            if (aVar != null) {
                aVar.mo222b();
            }
        }
    }

    public C0154k(Context context) {
        this.f255a = context;
    }

    /* renamed from: a */
    public final void mo438a() {
        if (this.f256b != null) {
            this.f255a.getContentResolver().unregisterContentObserver(this.f256b);
            this.f256b = null;
        }
    }

    /* renamed from: a */
    public final void mo439a(C0155a aVar, String str) {
        this.f256b = new C0156b(new Handler(Looper.getMainLooper()), aVar);
        this.f255a.getContentResolver().registerContentObserver(Settings.System.getUriFor(str), true, this.f256b);
    }
}
