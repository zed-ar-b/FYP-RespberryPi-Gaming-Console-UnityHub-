package com.unity3d.scar.adapter.common;

import android.os.Handler;
import android.os.Looper;

public class Utils {
    public static void runOnUiThread(Runnable runnable) {
        runOnUiThread(runnable, 0);
    }

    public static void runOnUiThread(Runnable runnable, long j) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, j);
    }
}
