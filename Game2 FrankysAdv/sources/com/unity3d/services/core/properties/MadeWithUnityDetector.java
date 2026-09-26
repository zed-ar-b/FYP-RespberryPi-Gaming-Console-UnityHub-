package com.unity3d.services.core.properties;

public class MadeWithUnityDetector {
    public static final String UNITY_PLAYER_CLASS_NAME = "com.unity3d.player.UnityPlayer";

    public static boolean isMadeWithUnity() {
        try {
            if (Class.forName(UNITY_PLAYER_CLASS_NAME) != null) {
            }
        } catch (ClassNotFoundException unused) {
        }
        return false;
    }
}
