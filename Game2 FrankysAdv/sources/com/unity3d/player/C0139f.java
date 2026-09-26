package com.unity3d.player;

import android.util.Log;

/* renamed from: com.unity3d.player.f */
final class C0139f {

    /* renamed from: a */
    protected static boolean f215a = false;

    protected static void Log(int i, String str) {
        if (!f215a) {
            if (i == 6) {
                Log.e("Unity", str);
            }
            if (i == 5) {
                Log.w("Unity", str);
            }
        }
    }
}
