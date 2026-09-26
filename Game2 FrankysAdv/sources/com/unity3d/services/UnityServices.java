package com.unity3d.services;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.configuration.EnvironmentCheck;
import com.unity3d.services.core.configuration.InitializeThread;
import com.unity3d.services.core.device.Device;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.properties.SdkProperties;

public class UnityServices {

    public enum UnityServicesError {
        INVALID_ARGUMENT,
        INIT_SANITY_CHECK_FAIL
    }

    public static void initialize(Context context, String str, boolean z, IUnityAdsInitializationListener iUnityAdsInitializationListener) {
        DeviceLog.entered();
        if (SdkProperties.getCurrentInitializationState() != SdkProperties.InitializationState.NOT_INITIALIZED) {
            String str2 = "";
            String gameId = ClientProperties.getGameId();
            if (gameId != null && !gameId.equals(str)) {
                str2 = str2 + createExpectedParametersString("Game ID", ClientProperties.getGameId(), str);
            }
            boolean isTestMode = SdkProperties.isTestMode();
            if (isTestMode != z) {
                str2 = str2 + createExpectedParametersString("Test Mode", Boolean.valueOf(isTestMode), Boolean.valueOf(z));
            }
            if (!TextUtils.isEmpty(str2)) {
                String str3 = "Unity Ads SDK failed to initialize due to already being initialized with different parameters" + str2;
                DeviceLog.warning(str3);
                if (iUnityAdsInitializationListener != null) {
                    iUnityAdsInitializationListener.onInitializationFailed(UnityAds.UnityAdsInitializationError.INVALID_ARGUMENT, str3);
                    return;
                }
                return;
            }
        }
        SdkProperties.addInitializationListener(iUnityAdsInitializationListener);
        if (SdkProperties.getCurrentInitializationState() == SdkProperties.InitializationState.INITIALIZED_SUCCESSFULLY) {
            SdkProperties.notifyInitializationComplete();
        } else if (SdkProperties.getCurrentInitializationState() == SdkProperties.InitializationState.INITIALIZED_FAILED) {
            SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INTERNAL_ERROR, "Unity Ads SDK failed to initialize due to previous failed reason");
        } else if (SdkProperties.getCurrentInitializationState() != SdkProperties.InitializationState.INITIALIZING) {
            SdkProperties.setInitializeState(SdkProperties.InitializationState.INITIALIZING);
            ClientProperties.setGameId(str);
            SdkProperties.setTestMode(z);
            if (!isSupported()) {
                DeviceLog.error("Error while initializing Unity Services: device is not supported");
                SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INTERNAL_ERROR, "Unity Ads SDK failed to initialize due to device is not supported");
                return;
            }
            SdkProperties.setInitializationTime(Device.getElapsedRealtime());
            SdkProperties.setInitializationTimeSinceEpoch(System.currentTimeMillis());
            if (str == null || str.length() == 0) {
                DeviceLog.error("Error while initializing Unity Services: empty game ID, halting Unity Ads init");
                SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INVALID_ARGUMENT, "Unity Ads SDK failed to initialize due to empty game ID");
            } else if (context == null) {
                DeviceLog.error("Error while initializing Unity Services: null context, halting Unity Ads init");
                SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INVALID_ARGUMENT, "Unity Ads SDK failed to initialize due to null context");
            } else {
                if (context instanceof Application) {
                    ClientProperties.setApplication((Application) context);
                } else if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    if (activity.getApplication() != null) {
                        ClientProperties.setApplication(activity.getApplication());
                    } else {
                        DeviceLog.error("Error while initializing Unity Services: cannot retrieve application from context, halting Unity Ads init");
                        SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INVALID_ARGUMENT, "Unity Ads SDK failed to initialize due to inability to retrieve application from context");
                        return;
                    }
                } else {
                    DeviceLog.error("Error while initializing Unity Services: invalid context, halting Unity Ads init");
                    SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INVALID_ARGUMENT, "Unity Ads SDK failed to initialize due to invalid context");
                    return;
                }
                if (z) {
                    DeviceLog.info("Initializing Unity Services " + SdkProperties.getVersionName() + " (" + SdkProperties.getVersionCode() + ") with game id " + str + " in test mode");
                } else {
                    DeviceLog.info("Initializing Unity Services " + SdkProperties.getVersionName() + " (" + SdkProperties.getVersionCode() + ") with game id " + str + " in production mode");
                }
                SdkProperties.setDebugMode(SdkProperties.getDebugMode());
                if (context.getApplicationContext() != null) {
                    ClientProperties.setApplicationContext(context.getApplicationContext());
                    if (!EnvironmentCheck.isEnvironmentOk()) {
                        DeviceLog.error("Error during Unity Services environment check, halting Unity Services init");
                        SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INTERNAL_ERROR, "Unity Ads SDK failed to initialize due to environment check failed");
                        return;
                    }
                    DeviceLog.info("Unity Services environment check OK");
                    InitializeThread.initialize(new Configuration());
                    return;
                }
                DeviceLog.error("Error while initializing Unity Services: cannot retrieve application context, halting Unity Ads init");
                SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INVALID_ARGUMENT, "Unity Ads SDK failed to initialize due to inability to retrieve application context");
            }
        }
    }

    public static boolean isSupported() {
        return Build.VERSION.SDK_INT >= 16;
    }

    public static boolean isInitialized() {
        return SdkProperties.isInitialized();
    }

    public static String getVersion() {
        return SdkProperties.getVersionName();
    }

    public static void setDebugMode(boolean z) {
        SdkProperties.setDebugMode(z);
    }

    public static boolean getDebugMode() {
        return SdkProperties.getDebugMode();
    }

    private static String createExpectedParametersString(String str, Object obj, Object obj2) {
        String str2 = "null";
        String obj3 = obj == null ? str2 : obj.toString();
        if (obj2 != null) {
            str2 = obj2.toString();
        }
        return "\n - " + str + " Current: " + obj3 + " | Received: " + str2;
    }
}
