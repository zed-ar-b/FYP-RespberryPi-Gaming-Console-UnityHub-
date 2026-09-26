package com.unity3d.scar.adapter.common;

public interface IUnityAdsError {
    int getCode();

    String getDescription();

    String getDomain();
}
