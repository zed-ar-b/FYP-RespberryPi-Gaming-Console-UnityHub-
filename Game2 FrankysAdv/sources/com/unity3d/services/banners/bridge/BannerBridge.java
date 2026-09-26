package com.unity3d.services.banners.bridge;

import com.unity3d.services.banners.BannerErrorCode;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.BannerViewCache;
import com.unity3d.services.banners.UnityBannerSize;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;

public class BannerBridge {

    public enum BannerEvent {
        BANNER_VISIBILITY_CHANGED,
        BANNER_RESIZED,
        BANNER_LOADED,
        BANNER_DESTROYED,
        BANNER_ATTACHED,
        BANNER_DETACHED,
        BANNER_LOAD_PLACEMENT,
        BANNER_DESTROY_BANNER
    }

    public static void load(String str, String str2, UnityBannerSize unityBannerSize) {
        WebViewApp currentApp = WebViewApp.getCurrentApp();
        if (currentApp != null) {
            currentApp.sendEvent(WebViewEventCategory.BANNER, BannerEvent.BANNER_LOAD_PLACEMENT, str, str2, Integer.valueOf(unityBannerSize.getWidth()), Integer.valueOf(unityBannerSize.getHeight()));
            return;
        }
        BannerView bannerView = BannerViewCache.getInstance().getBannerView(str2);
        if (bannerView != null && bannerView.getListener() != null) {
            bannerView.getListener().onBannerFailedToLoad(bannerView, new BannerErrorInfo("WebViewApp was not available, this is likely because UnityAds has not been initialized", BannerErrorCode.WEBVIEW_ERROR));
        }
    }

    public static void destroy(String str) {
        WebViewApp currentApp = WebViewApp.getCurrentApp();
        if (currentApp != null) {
            currentApp.sendEvent(WebViewEventCategory.BANNER, BannerEvent.BANNER_DESTROY_BANNER, str);
        }
    }

    public static void resize(String str, int i, int i2, int i3, int i4, float f) {
        WebViewApp currentApp = WebViewApp.getCurrentApp();
        if (currentApp != null) {
            currentApp.sendEvent(WebViewEventCategory.BANNER, BannerEvent.BANNER_RESIZED, str, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Float.valueOf(f));
        }
    }

    public static void visibilityChanged(String str, int i) {
        WebViewApp currentApp = WebViewApp.getCurrentApp();
        if (currentApp != null) {
            currentApp.sendEvent(WebViewEventCategory.BANNER, BannerEvent.BANNER_VISIBILITY_CHANGED, str, Integer.valueOf(i));
        }
    }

    public static void didLoad(String str) {
        WebViewApp currentApp = WebViewApp.getCurrentApp();
        if (currentApp != null) {
            currentApp.sendEvent(WebViewEventCategory.BANNER, BannerEvent.BANNER_LOADED, str);
        }
    }

    public static void didDestroy(String str) {
        WebViewApp currentApp = WebViewApp.getCurrentApp();
        if (currentApp != null) {
            currentApp.sendEvent(WebViewEventCategory.BANNER, BannerEvent.BANNER_DESTROYED, str);
        }
    }

    public static void didAttach(String str) {
        WebViewApp currentApp = WebViewApp.getCurrentApp();
        if (currentApp != null) {
            currentApp.sendEvent(WebViewEventCategory.BANNER, BannerEvent.BANNER_ATTACHED, str);
        }
    }

    public static void didDetach(String str) {
        WebViewApp currentApp = WebViewApp.getCurrentApp();
        if (currentApp != null) {
            currentApp.sendEvent(WebViewEventCategory.BANNER, BannerEvent.BANNER_DETACHED, str);
        }
    }
}
