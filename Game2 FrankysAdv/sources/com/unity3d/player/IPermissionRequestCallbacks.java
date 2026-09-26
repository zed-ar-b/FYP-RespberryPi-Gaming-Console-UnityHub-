package com.unity3d.player;

public interface IPermissionRequestCallbacks {
    void onPermissionDenied(String str);

    void onPermissionDeniedAndDontAskAgain(String str);

    void onPermissionGranted(String str);
}
