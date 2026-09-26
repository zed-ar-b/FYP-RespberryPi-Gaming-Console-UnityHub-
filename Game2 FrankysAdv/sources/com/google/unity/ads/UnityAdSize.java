package com.google.unity.ads;

import android.app.Activity;
import android.util.DisplayMetrics;
import com.google.android.gms.ads.AdSize;

public class UnityAdSize {
    private static final int FULL_WIDTH = -1;

    private UnityAdSize() {
    }

    public static AdSize getCurrentOrientationAnchoredAdaptiveBannerAdSize(Activity activity, int i) {
        if (i == -1) {
            i = getScreenWidth(activity);
        }
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, i);
    }

    public static AdSize getLandscapeAnchoredAdaptiveBannerAdSize(Activity activity, int i) {
        if (i == -1) {
            i = getScreenWidth(activity);
        }
        return AdSize.getLandscapeAnchoredAdaptiveBannerAdSize(activity, i);
    }

    public static AdSize getPortraitAnchoredAdaptiveBannerAdSize(Activity activity, int i) {
        if (i == -1) {
            i = getScreenWidth(activity);
        }
        return AdSize.getPortraitAnchoredAdaptiveBannerAdSize(activity, i);
    }

    public static AdSize getSmartBannerAdSize() {
        return AdSize.SMART_BANNER;
    }

    private static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
    }
}
