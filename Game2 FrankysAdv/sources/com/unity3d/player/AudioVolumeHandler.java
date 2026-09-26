package com.unity3d.player;

import android.content.Context;
import com.unity3d.player.C0127b;

public class AudioVolumeHandler implements C0127b.C0129b {

    /* renamed from: a */
    private C0127b f12a;

    AudioVolumeHandler(Context context) {
        C0127b bVar = new C0127b(context);
        this.f12a = bVar;
        bVar.mo378a(this);
    }

    /* renamed from: a */
    public final void mo179a() {
        this.f12a.mo377a();
        this.f12a = null;
    }

    public final native void onAudioVolumeChanged(int i);
}
