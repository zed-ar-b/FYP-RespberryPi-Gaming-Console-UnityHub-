package com.unity3d.player;

import android.os.Build;

public class PlatformSupport {
    static final boolean MARSHMALLOW_SUPPORT = (Build.VERSION.SDK_INT >= 23);
    static final boolean NOUGAT_SUPPORT;

    static {
        boolean z = true;
        if (Build.VERSION.SDK_INT < 24) {
            z = false;
        }
        NOUGAT_SUPPORT = z;
    }
}
