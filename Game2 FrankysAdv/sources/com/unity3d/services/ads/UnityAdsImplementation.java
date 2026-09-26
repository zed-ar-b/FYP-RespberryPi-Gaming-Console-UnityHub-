package com.unity3d.services.ads;

import android.app.Activity;
import android.content.Context;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.IUnityAdsTokenListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsLoadOptions;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.services.UnityServices;
import com.unity3d.services.ads.operation.load.LoadModule;
import com.unity3d.services.ads.operation.load.LoadOperationState;
import com.unity3d.services.ads.operation.show.ShowModule;
import com.unity3d.services.ads.operation.show.ShowOperationState;
import com.unity3d.services.ads.token.AsyncTokenStorage;
import com.unity3d.services.ads.token.TokenStorage;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.request.metrics.AdOperationMetric;
import com.unity3d.services.core.request.metrics.SDKMetrics;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.bridge.WebViewBridgeInvoker;

public final class UnityAdsImplementation {
    private static Configuration configuration;
    private static WebViewBridgeInvoker webViewBridgeInvoker = new WebViewBridgeInvoker();

    public static void initialize(Context context, String str, IUnityAdsInitializationListener iUnityAdsInitializationListener) {
        initialize(context, str, false, iUnityAdsInitializationListener);
    }

    public static void initialize(Context context, String str, boolean z) {
        initialize(context, str, z, (IUnityAdsInitializationListener) null);
    }

    public static void initialize(Context context, String str, boolean z, IUnityAdsInitializationListener iUnityAdsInitializationListener) {
        DeviceLog.entered();
        UnityServices.initialize(context, str, z, iUnityAdsInitializationListener);
    }

    public static boolean isInitialized() {
        return UnityServices.isInitialized();
    }

    public static boolean isSupported() {
        return UnityServices.isSupported();
    }

    public static String getVersion() {
        return UnityServices.getVersion();
    }

    public static void show(Activity activity, String str) {
        show(activity, str, new UnityAdsShowOptions(), (IUnityAdsShowListener) null);
    }

    public static void show(Activity activity, String str, IUnityAdsShowListener iUnityAdsShowListener) {
        show(activity, str, new UnityAdsShowOptions(), iUnityAdsShowListener);
    }

    public static void show(Activity activity, String str, UnityAdsShowOptions unityAdsShowOptions, IUnityAdsShowListener iUnityAdsShowListener) {
        if (!isSupported()) {
            handleShowError(iUnityAdsShowListener, str, UnityAds.UnityAdsShowError.NOT_INITIALIZED, "Unity Ads is not supported for this device");
        } else if (!isInitialized()) {
            handleShowError(iUnityAdsShowListener, str, UnityAds.UnityAdsShowError.NOT_INITIALIZED, "Unity Ads is not initialized");
        } else if (activity == null) {
            handleShowError(iUnityAdsShowListener, str, UnityAds.UnityAdsShowError.INVALID_ARGUMENT, "Activity must not be null");
        } else {
            Configuration configuration2 = configuration;
            if (configuration2 == null) {
                configuration2 = new Configuration();
            }
            ClientProperties.setActivity(activity);
            ShowModule.getInstance().executeAdOperation(WebViewApp.getCurrentApp(), new ShowOperationState(str, iUnityAdsShowListener, activity, unityAdsShowOptions, configuration2));
        }
    }

    private static void handleShowError(IUnityAdsShowListener iUnityAdsShowListener, String str, UnityAds.UnityAdsShowError unityAdsShowError, String str2) {
        SDKMetrics.getInstance().sendMetricWithInitState(AdOperationMetric.newAdShowFailure(unityAdsShowError, (Long) 0L));
        if (iUnityAdsShowListener != null) {
            iUnityAdsShowListener.onUnityAdsShowFailure(str, unityAdsShowError, str2);
        }
    }

    public static void setDebugMode(boolean z) {
        UnityServices.setDebugMode(z);
    }

    public static boolean getDebugMode() {
        return UnityServices.getDebugMode();
    }

    public static void load(String str, UnityAdsLoadOptions unityAdsLoadOptions, IUnityAdsLoadListener iUnityAdsLoadListener) {
        Configuration configuration2 = configuration;
        if (configuration2 == null) {
            configuration2 = new Configuration();
        }
        LoadModule.getInstance().executeAdOperation(webViewBridgeInvoker, new LoadOperationState(str, iUnityAdsLoadListener, unityAdsLoadOptions, configuration2));
    }

    public static String getToken() {
        return TokenStorage.getToken();
    }

    public static void getToken(IUnityAdsTokenListener iUnityAdsTokenListener) {
        if (iUnityAdsTokenListener == null) {
            DeviceLog.info("Please provide non-null listener to UnityAds.GetToken method");
        } else if (ClientProperties.getApplicationContext() == null) {
            iUnityAdsTokenListener.onUnityAdsTokenReady((String) null);
        } else {
            AsyncTokenStorage.getInstance().getToken(iUnityAdsTokenListener);
        }
    }

    public static void setConfiguration(Configuration configuration2) {
        configuration = configuration2;
    }
}
