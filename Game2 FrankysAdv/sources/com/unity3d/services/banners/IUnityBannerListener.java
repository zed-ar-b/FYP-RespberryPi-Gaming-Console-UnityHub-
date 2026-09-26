package com.unity3d.services.banners;

import android.view.View;

public interface IUnityBannerListener {
    void onUnityBannerClick(String str);

    void onUnityBannerError(String str);

    void onUnityBannerHide(String str);

    void onUnityBannerLoaded(String str, View view);

    void onUnityBannerShow(String str);

    void onUnityBannerUnloaded(String str);
}
