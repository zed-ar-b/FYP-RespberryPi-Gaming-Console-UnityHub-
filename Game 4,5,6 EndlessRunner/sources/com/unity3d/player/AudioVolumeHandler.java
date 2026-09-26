package com.unity3d.player;

import android.content.Context;
import com.unity3d.player.C0058b;

public class AudioVolumeHandler implements C0058b.C0060b {

    /* renamed from: a */
    private C0058b f13a;

    AudioVolumeHandler(Context context) {
        C0058b bVar = new C0058b(context);
        this.f13a = bVar;
        bVar.mo252a(this);
    }

    /* renamed from: a */
    public final void mo53a() {
        this.f13a.mo251a();
        this.f13a = null;
    }

    public final native void onAudioVolumeChanged(int i);
}
