package com.unity3d.services.banners.api;

import com.unity3d.services.banners.BannerViewCache;
import com.unity3d.services.banners.UnityBannerSize;
import com.unity3d.services.banners.bridge.BannerBridge;
import com.unity3d.services.banners.properties.BannerRefreshInfo;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;

public class Banner {

    private enum BannerViewType {
        WEB_PLAYER,
        UNKNOWN;

        public static BannerViewType fromString(String str) {
            try {
                return valueOf(str);
            } catch (IllegalArgumentException unused) {
                return UNKNOWN;
            }
        }
    }

    /* renamed from: com.unity3d.services.banners.api.Banner$1 */
    static /* synthetic */ class C02761 {

        /* renamed from: $SwitchMap$com$unity3d$services$banners$api$Banner$BannerViewType */
        static final /* synthetic */ int[] f342xf0b64357;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.unity3d.services.banners.api.Banner$BannerViewType[] r0 = com.unity3d.services.banners.api.Banner.BannerViewType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f342xf0b64357 = r0
                com.unity3d.services.banners.api.Banner$BannerViewType r1 = com.unity3d.services.banners.api.Banner.BannerViewType.WEB_PLAYER     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f342xf0b64357     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.services.banners.api.Banner$BannerViewType r1 = com.unity3d.services.banners.api.Banner.BannerViewType.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.banners.api.Banner.C02761.<clinit>():void");
        }
    }

    @WebViewExposed
    public static void load(String str, Integer num, Integer num2, String str2, WebViewCallback webViewCallback) {
        if (C02761.f342xf0b64357[BannerViewType.fromString(str).ordinal()] == 1 && BannerViewCache.getInstance().loadWebPlayer(str2, new UnityBannerSize(num.intValue(), num2.intValue()))) {
            BannerBridge.didLoad(str2);
        }
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void setRefreshRate(String str, Integer num, WebViewCallback webViewCallback) {
        if (!(str == null || num == null)) {
            BannerRefreshInfo.getInstance().setRefreshRate(str, num);
        }
        webViewCallback.invoke(new Object[0]);
    }
}
