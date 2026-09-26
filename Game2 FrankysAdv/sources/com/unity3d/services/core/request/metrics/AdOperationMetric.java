package com.unity3d.services.core.request.metrics;

import com.google.unity.ads.PluginUtils;
import com.unity3d.ads.UnityAds;
import java.util.HashMap;
import java.util.Map;

public class AdOperationMetric {
    private static final String AD_LOAD_FAIL = "native_load_time_failure";
    private static final String AD_LOAD_START = "native_load_started";
    private static final String AD_LOAD_SUCCESS = "native_load_time_success";
    private static final String AD_SHOW_FAIL = "native_show_time_failure";
    private static final String AD_SHOW_START = "native_show_started";
    private static final String AD_SHOW_SUCCESS = "native_show_time_success";
    public static final String INIT_STATE = "state";
    private static final String REASON = "reason";
    private static final String UNKNOWN = "unknown";

    public static Metric newAdLoadStart() {
        return new Metric(AD_LOAD_START, (Object) null, (Map<String, String>) null);
    }

    public static Metric newAdShowStart() {
        return new Metric(AD_SHOW_START, (Object) null, (Map<String, String>) null);
    }

    public static Metric newAdLoadSuccess(Long l) {
        return new Metric(AD_LOAD_SUCCESS, l, (Map<String, String>) null);
    }

    public static Metric newAdShowSuccess(Long l) {
        return new Metric(AD_SHOW_SUCCESS, l, (Map<String, String>) null);
    }

    public static Metric newAdLoadFailure(AdOperationError adOperationError, Long l) {
        return new Metric(AD_LOAD_FAIL, l, getTags(adOperationError));
    }

    public static Metric newAdLoadFailure(UnityAds.UnityAdsLoadError unityAdsLoadError, Long l) {
        return newAdLoadFailure(mapUnityAdsLoadError(unityAdsLoadError), l);
    }

    public static Metric newAdShowFailure(AdOperationError adOperationError, Long l) {
        return new Metric(AD_SHOW_FAIL, l, getTags(adOperationError));
    }

    public static Metric newAdShowFailure(UnityAds.UnityAdsShowError unityAdsShowError, Long l) {
        return newAdShowFailure(mapUnityAdsShowError(unityAdsShowError), l);
    }

    private static HashMap<String, String> getTags(AdOperationError adOperationError) {
        final String adOperationError2 = adOperationError == null ? UNKNOWN : adOperationError.toString();
        return new HashMap<String, String>() {
            {
                put(AdOperationMetric.REASON, adOperationError2);
            }
        };
    }

    private static AdOperationError mapUnityAdsLoadError(UnityAds.UnityAdsLoadError unityAdsLoadError) {
        int i = C03142.$SwitchMap$com$unity3d$ads$UnityAds$UnityAdsLoadError[unityAdsLoadError.ordinal()];
        if (i == 1) {
            return AdOperationError.init_failed;
        }
        if (i == 2) {
            return AdOperationError.internal;
        }
        if (i == 3) {
            return AdOperationError.invalid;
        }
        if (i == 4) {
            return AdOperationError.no_fill;
        }
        if (i != 5) {
            return null;
        }
        return AdOperationError.timeout;
    }

    /* renamed from: com.unity3d.services.core.request.metrics.AdOperationMetric$2 */
    static /* synthetic */ class C03142 {
        static final /* synthetic */ int[] $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsLoadError;
        static final /* synthetic */ int[] $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsShowError;

        /* JADX WARNING: Can't wrap try/catch for region: R(26:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|(2:21|22)|23|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(27:0|(2:1|2)|3|(2:5|6)|7|9|10|11|(2:13|14)|15|17|18|19|20|(2:21|22)|23|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(29:0|1|2|3|(2:5|6)|7|9|10|11|13|14|15|17|18|19|20|(2:21|22)|23|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Can't wrap try/catch for region: R(30:0|1|2|3|(2:5|6)|7|9|10|11|13|14|15|17|18|19|20|21|22|23|25|26|27|28|29|30|31|32|33|34|36) */
        /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0065 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x006f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0079 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x0083 */
        static {
            /*
                com.unity3d.ads.UnityAds$UnityAdsShowError[] r0 = com.unity3d.ads.UnityAds.UnityAdsShowError.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsShowError = r0
                r1 = 1
                com.unity3d.ads.UnityAds$UnityAdsShowError r2 = com.unity3d.ads.UnityAds.UnityAdsShowError.NOT_INITIALIZED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r2 = r2.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r0[r2] = r1     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                r0 = 2
                int[] r2 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsShowError     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.ads.UnityAds$UnityAdsShowError r3 = com.unity3d.ads.UnityAds.UnityAdsShowError.NOT_READY     // Catch:{ NoSuchFieldError -> 0x001d }
                int r3 = r3.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2[r3] = r0     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                r2 = 3
                int[] r3 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsShowError     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.unity3d.ads.UnityAds$UnityAdsShowError r4 = com.unity3d.ads.UnityAds.UnityAdsShowError.VIDEO_PLAYER_ERROR     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r4 = r4.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r3[r4] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                r3 = 4
                int[] r4 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsShowError     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.unity3d.ads.UnityAds$UnityAdsShowError r5 = com.unity3d.ads.UnityAds.UnityAdsShowError.INVALID_ARGUMENT     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r4[r5] = r3     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                r4 = 5
                int[] r5 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsShowError     // Catch:{ NoSuchFieldError -> 0x003e }
                com.unity3d.ads.UnityAds$UnityAdsShowError r6 = com.unity3d.ads.UnityAds.UnityAdsShowError.NO_CONNECTION     // Catch:{ NoSuchFieldError -> 0x003e }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r5[r6] = r4     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r5 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsShowError     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.unity3d.ads.UnityAds$UnityAdsShowError r6 = com.unity3d.ads.UnityAds.UnityAdsShowError.ALREADY_SHOWING     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r7 = 6
                r5[r6] = r7     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r5 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsShowError     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.unity3d.ads.UnityAds$UnityAdsShowError r6 = com.unity3d.ads.UnityAds.UnityAdsShowError.INTERNAL_ERROR     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r7 = 7
                r5[r6] = r7     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                com.unity3d.ads.UnityAds$UnityAdsLoadError[] r5 = com.unity3d.ads.UnityAds.UnityAdsLoadError.values()
                int r5 = r5.length
                int[] r5 = new int[r5]
                $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsLoadError = r5
                com.unity3d.ads.UnityAds$UnityAdsLoadError r6 = com.unity3d.ads.UnityAds.UnityAdsLoadError.INITIALIZE_FAILED     // Catch:{ NoSuchFieldError -> 0x0065 }
                int r6 = r6.ordinal()     // Catch:{ NoSuchFieldError -> 0x0065 }
                r5[r6] = r1     // Catch:{ NoSuchFieldError -> 0x0065 }
            L_0x0065:
                int[] r1 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsLoadError     // Catch:{ NoSuchFieldError -> 0x006f }
                com.unity3d.ads.UnityAds$UnityAdsLoadError r5 = com.unity3d.ads.UnityAds.UnityAdsLoadError.INTERNAL_ERROR     // Catch:{ NoSuchFieldError -> 0x006f }
                int r5 = r5.ordinal()     // Catch:{ NoSuchFieldError -> 0x006f }
                r1[r5] = r0     // Catch:{ NoSuchFieldError -> 0x006f }
            L_0x006f:
                int[] r0 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsLoadError     // Catch:{ NoSuchFieldError -> 0x0079 }
                com.unity3d.ads.UnityAds$UnityAdsLoadError r1 = com.unity3d.ads.UnityAds.UnityAdsLoadError.INVALID_ARGUMENT     // Catch:{ NoSuchFieldError -> 0x0079 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0079 }
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0079 }
            L_0x0079:
                int[] r0 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsLoadError     // Catch:{ NoSuchFieldError -> 0x0083 }
                com.unity3d.ads.UnityAds$UnityAdsLoadError r1 = com.unity3d.ads.UnityAds.UnityAdsLoadError.NO_FILL     // Catch:{ NoSuchFieldError -> 0x0083 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0083 }
                r0[r1] = r3     // Catch:{ NoSuchFieldError -> 0x0083 }
            L_0x0083:
                int[] r0 = $SwitchMap$com$unity3d$ads$UnityAds$UnityAdsLoadError     // Catch:{ NoSuchFieldError -> 0x008d }
                com.unity3d.ads.UnityAds$UnityAdsLoadError r1 = com.unity3d.ads.UnityAds.UnityAdsLoadError.TIMEOUT     // Catch:{ NoSuchFieldError -> 0x008d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x008d }
                r0[r1] = r4     // Catch:{ NoSuchFieldError -> 0x008d }
            L_0x008d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.request.metrics.AdOperationMetric.C03142.<clinit>():void");
        }
    }

    private static AdOperationError mapUnityAdsShowError(UnityAds.UnityAdsShowError unityAdsShowError) {
        switch (C03142.$SwitchMap$com$unity3d$ads$UnityAds$UnityAdsShowError[unityAdsShowError.ordinal()]) {
            case 1:
                return AdOperationError.init_failed;
            case 2:
                return AdOperationError.not_ready;
            case 3:
                return AdOperationError.player;
            case 4:
                return AdOperationError.invalid;
            case PluginUtils.POSITION_BOTTOM_RIGHT:
                return AdOperationError.no_connection;
            case PluginUtils.POSITION_CENTER:
                return AdOperationError.already_showing;
            case 7:
                return AdOperationError.internal;
            default:
                return null;
        }
    }
}
