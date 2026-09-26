package com.unity3d.player;

import android.content.Context;
import android.provider.Settings;
import com.unity3d.player.C0154k;

public class OrientationLockListener implements C0154k.C0155a {

    /* renamed from: a */
    private C0154k f34a;

    /* renamed from: b */
    private Context f35b;

    OrientationLockListener(Context context) {
        this.f35b = context;
        this.f34a = new C0154k(context);
        nativeUpdateOrientationLockState(Settings.System.getInt(this.f35b.getContentResolver(), "accelerometer_rotation", 0));
        this.f34a.mo439a(this, "accelerometer_rotation");
    }

    /* renamed from: a */
    public final void mo221a() {
        this.f34a.mo438a();
        this.f34a = null;
    }

    /* renamed from: b */
    public final void mo222b() {
        nativeUpdateOrientationLockState(Settings.System.getInt(this.f35b.getContentResolver(), "accelerometer_rotation", 0));
    }

    public final native void nativeUpdateOrientationLockState(int i);
}
