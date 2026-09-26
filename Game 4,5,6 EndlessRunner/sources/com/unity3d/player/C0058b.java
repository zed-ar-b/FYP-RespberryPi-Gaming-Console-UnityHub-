package com.unity3d.player;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;

/* renamed from: com.unity3d.player.b */
final class C0058b {

    /* renamed from: a */
    private final Context f169a;

    /* renamed from: b */
    private final AudioManager f170b;

    /* renamed from: c */
    private C0059a f171c;

    /* renamed from: com.unity3d.player.b$a */
    private class C0059a extends ContentObserver {

        /* renamed from: b */
        private final C0060b f173b;

        /* renamed from: c */
        private final AudioManager f174c;

        /* renamed from: d */
        private final int f175d = 3;

        /* renamed from: e */
        private int f176e;

        public C0059a(Handler handler, AudioManager audioManager, int i, C0060b bVar) {
            super(handler);
            this.f174c = audioManager;
            this.f173b = bVar;
            this.f176e = audioManager.getStreamVolume(3);
        }

        public final boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        public final void onChange(boolean z, Uri uri) {
            int streamVolume;
            AudioManager audioManager = this.f174c;
            if (audioManager != null && this.f173b != null && (streamVolume = audioManager.getStreamVolume(this.f175d)) != this.f176e) {
                this.f176e = streamVolume;
                this.f173b.onAudioVolumeChanged(streamVolume);
            }
        }
    }

    /* renamed from: com.unity3d.player.b$b */
    public interface C0060b {
        void onAudioVolumeChanged(int i);
    }

    public C0058b(Context context) {
        this.f169a = context;
        this.f170b = (AudioManager) context.getSystemService("audio");
    }

    /* renamed from: a */
    public final void mo251a() {
        if (this.f171c != null) {
            this.f169a.getContentResolver().unregisterContentObserver(this.f171c);
            this.f171c = null;
        }
    }

    /* renamed from: a */
    public final void mo252a(C0060b bVar) {
        this.f171c = new C0059a(new Handler(), this.f170b, 3, bVar);
        this.f169a.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, this.f171c);
    }
}
