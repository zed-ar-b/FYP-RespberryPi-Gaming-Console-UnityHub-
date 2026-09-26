package com.unity3d.player;

import android.content.Context;
import android.provider.Settings;
import com.unity3d.player.C0152k;

public class OrientationLockListener implements C0152k.C0153a {

    /* renamed from: a */
    private C0152k f34a;

    /* renamed from: b */
    private Context f35b;

    OrientationLockListener(Context context) {
        this.f35b = context;
        this.f34a = new C0152k(context);
        nativeUpdateOrientationLockState(Settings.System.getInt(this.f35b.getContentResolver(), "accelerometer_rotation", 0));
        this.f34a.mo411a(this, "accelerometer_rotation");
    }

    /* renamed from: a */
    public final void mo193a() {
        this.f34a.mo410a();
        this.f34a = null;
    }

    /* renamed from: b */
    public final void mo194b() {
        nativeUpdateOrientationLockState(Settings.System.getInt(this.f35b.getContentResolver(), "accelerometer_rotation", 0));
    }

    public final native void nativeUpdateOrientationLockState(int i);
}
