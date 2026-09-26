package com.unity3d.player;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

/* renamed from: com.unity3d.player.b */
final class C0125b {

    /* renamed from: a */
    private final Context f168a;

    /* renamed from: b */
    private final AudioManager f169b;

    /* renamed from: c */
    private C0126a f170c;

    /* renamed from: com.unity3d.player.b$a */
    private class C0126a extends ContentObserver {

        /* renamed from: b */
        private final C0127b f172b;

        /* renamed from: c */
        private final AudioManager f173c;

        /* renamed from: d */
        private final int f174d = 3;

        /* renamed from: e */
        private int f175e;

        public C0126a(Handler handler, AudioManager audioManager, int i, C0127b bVar) {
            super(handler);
            this.f173c = audioManager;
            this.f172b = bVar;
            this.f175e = audioManager.getStreamVolume(3);
        }

        public final boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        public final void onChange(boolean z, Uri uri) {
            int streamVolume;
            AudioManager audioManager = this.f173c;
            if (audioManager != null && this.f172b != null && (streamVolume = audioManager.getStreamVolume(this.f174d)) != this.f175e) {
                this.f175e = streamVolume;
                this.f172b.onAudioVolumeChanged(streamVolume);
            }
        }
    }

    /* renamed from: com.unity3d.player.b$b */
    public interface C0127b {
        void onAudioVolumeChanged(int i);
    }

    public C0125b(Context context) {
        this.f168a = context;
        this.f169b = (AudioManager) context.getSystemService("audio");
    }

    /* renamed from: a */
    public final void mo349a() {
        if (this.f170c != null) {
            this.f168a.getContentResolver().unregisterContentObserver(this.f170c);
            this.f170c = null;
        }
    }

    /* renamed from: a */
    public final void mo350a(C0127b bVar) {
        this.f170c = new C0126a(new Handler(), this.f169b, 3, bVar);
        this.f168a.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, this.f170c);
    }
}
