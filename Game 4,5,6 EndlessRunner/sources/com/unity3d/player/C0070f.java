package com.unity3d.player;

import android.util.Log;

/* renamed from: com.unity3d.player.f */
final class C0070f {

    /* renamed from: a */
    protected static boolean f216a = false;

    protected static void Log(int i, String str) {
        if (!f216a) {
            if (i == 6) {
                Log.e("Unity", str);
            }
            if (i == 5) {
                Log.w("Unity", str);
            }
        }
    }
}
