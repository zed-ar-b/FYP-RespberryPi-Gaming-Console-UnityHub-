package com.unity3d.player;

import android.content.Context;
import android.provider.Settings;
import com.unity3d.player.C0085k;

public class OrientationLockListener implements C0085k.C0086a {

    /* renamed from: a */
    private C0085k f35a;

    /* renamed from: b */
    private Context f36b;

    OrientationLockListener(Context context) {
        this.f36b = context;
        this.f35a = new C0085k(context);
        nativeUpdateOrientationLockState(Settings.System.getInt(this.f36b.getContentResolver(), "accelerometer_rotation", 0));
        this.f35a.mo313a(this, "accelerometer_rotation");
    }

    /* renamed from: a */
    public final void mo95a() {
        this.f35a.mo312a();
        this.f35a = null;
    }

    /* renamed from: b */
    public final void mo96b() {
        nativeUpdateOrientationLockState(Settings.System.getInt(this.f36b.getContentResolver(), "accelerometer_rotation", 0));
    }

    public final native void nativeUpdateOrientationLockState(int i);
}
