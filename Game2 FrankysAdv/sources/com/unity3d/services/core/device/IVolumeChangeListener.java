package com.unity3d.services.core.device;

public interface IVolumeChangeListener {
    int getStreamType();

    void onVolumeChanged(int i);
}
