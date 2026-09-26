package com.unity3d.services.core.configuration;

import com.unity3d.ads.UnityAds;
import com.unity3d.services.core.api.Broadcast;
import com.unity3d.services.core.api.Cache;
import com.unity3d.services.core.api.ClassDetection;
import com.unity3d.services.core.api.Connectivity;
import com.unity3d.services.core.api.DeviceInfo;
import com.unity3d.services.core.api.Intent;
import com.unity3d.services.core.api.Lifecycle;
import com.unity3d.services.core.api.Permissions;
import com.unity3d.services.core.api.Preferences;
import com.unity3d.services.core.api.Request;
import com.unity3d.services.core.api.Resolve;
import com.unity3d.services.core.api.Sdk;
import com.unity3d.services.core.api.SensorInfo;
import com.unity3d.services.core.api.Storage;
import com.unity3d.services.core.broadcast.BroadcastMonitor;
import com.unity3d.services.core.cache.CacheThread;
import com.unity3d.services.core.connectivity.ConnectivityMonitor;
import com.unity3d.services.core.device.AdvertisingId;
import com.unity3d.services.core.device.OpenAdvertisingId;
import com.unity3d.services.core.device.StorageManager;
import com.unity3d.services.core.device.VolumeChange;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.properties.SdkProperties;
import com.unity3d.services.core.request.WebRequestThread;
import com.unity3d.services.core.request.metrics.SDKMetrics;

public class CoreModuleConfiguration implements IModuleConfiguration {
    public Class[] getWebAppApiClassList() {
        return new Class[]{Broadcast.class, Cache.class, Connectivity.class, DeviceInfo.class, ClassDetection.class, Storage.class, Sdk.class, Request.class, Resolve.class, Intent.class, Lifecycle.class, Preferences.class, SensorInfo.class, Permissions.class};
    }

    public boolean resetState(Configuration configuration) {
        SDKMetrics.setConfiguration(configuration);
        BroadcastMonitor.removeAllBroadcastListeners();
        CacheThread.cancel();
        WebRequestThread.cancel();
        ConnectivityMonitor.stopAll();
        StorageManager.init(ClientProperties.getApplicationContext());
        AdvertisingId.init(ClientProperties.getApplicationContext());
        OpenAdvertisingId.init(ClientProperties.getApplicationContext());
        VolumeChange.clearAllListeners();
        return true;
    }

    public boolean initModuleState(Configuration configuration) {
        SDKMetrics.setConfiguration(configuration);
        return true;
    }

    public boolean initErrorState(Configuration configuration, ErrorState errorState, final String str) {
        final UnityAds.UnityAdsInitializationError unityAdsInitializationError;
        SDKMetrics.setConfiguration(configuration);
        int i = C02903.$SwitchMap$com$unity3d$services$core$configuration$ErrorState[errorState.ordinal()];
        if (i == 1) {
            unityAdsInitializationError = UnityAds.UnityAdsInitializationError.INTERNAL_ERROR;
        } else if (i != 2) {
            unityAdsInitializationError = UnityAds.UnityAdsInitializationError.INTERNAL_ERROR;
            str = "Unity Ads failed to initialize due to internal error";
        } else {
            unityAdsInitializationError = UnityAds.UnityAdsInitializationError.AD_BLOCKER_DETECTED;
        }
        InitializationNotificationCenter.getInstance().triggerOnSdkInitializationFailed(str, errorState, 0);
        Utilities.runOnUiThread(new Runnable() {
            public void run() {
                SdkProperties.notifyInitializationFailed(unityAdsInitializationError, str);
            }
        });
        return true;
    }

    /* renamed from: com.unity3d.services.core.configuration.CoreModuleConfiguration$3 */
    static /* synthetic */ class C02903 {
        static final /* synthetic */ int[] $SwitchMap$com$unity3d$services$core$configuration$ErrorState;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.unity3d.services.core.configuration.ErrorState[] r0 = com.unity3d.services.core.configuration.ErrorState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$unity3d$services$core$configuration$ErrorState = r0
                com.unity3d.services.core.configuration.ErrorState r1 = com.unity3d.services.core.configuration.ErrorState.CreateWebApp     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$unity3d$services$core$configuration$ErrorState     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.services.core.configuration.ErrorState r1 = com.unity3d.services.core.configuration.ErrorState.InitModules     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.configuration.CoreModuleConfiguration.C02903.<clinit>():void");
        }
    }

    public boolean initCompleteState(Configuration configuration) {
        SDKMetrics.setConfiguration(configuration);
        InitializationNotificationCenter.getInstance().triggerOnSdkInitialized();
        Utilities.runOnUiThread(new Runnable() {
            public void run() {
                SdkProperties.notifyInitializationComplete();
            }
        });
        return true;
    }
}
