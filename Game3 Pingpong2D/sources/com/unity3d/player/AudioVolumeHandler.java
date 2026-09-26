package com.unity3d.player;

import android.content.Context;
import com.unity3d.player.C0125b;

public class AudioVolumeHandler implements C0125b.C0127b {

    /* renamed from: a */
    private C0125b f12a;

    AudioVolumeHandler(Context context) {
        C0125b bVar = new C0125b(context);
        this.f12a = bVar;
        bVar.mo350a(this);
    }

    /* renamed from: a */
    public final void mo151a() {
        this.f12a.mo349a();
        this.f12a = null;
    }

    public final native void onAudioVolumeChanged(int i);
}
