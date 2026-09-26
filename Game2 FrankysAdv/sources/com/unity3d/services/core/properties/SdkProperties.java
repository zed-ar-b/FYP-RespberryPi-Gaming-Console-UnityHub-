package com.unity3d.services.core.properties;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.unity3d.ads.BuildConfig;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.core.cache.CacheDirectory;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.device.Device;
import com.unity3d.services.core.log.DeviceLog;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicReference;

public class SdkProperties {
    private static final String CACHE_DIR_NAME = "UnityAdsCache";
    private static final String CHINA_ISO_ALPHA_2_CODE = "CN";
    private static final String CHINA_ISO_ALPHA_3_CODE = "CHN";
    private static final String CONFIG_VERSION_METADATA_KEY = "com.unity3d.ads.configversion";
    private static final String DEFAULT_CONFIG_VERSION = "configv2";
    private static final String LOCAL_CACHE_FILE_PREFIX = "UnityAdsCache-";
    private static final String LOCAL_STORAGE_FILE_PREFIX = "UnityAdsStorage-";
    private static CacheDirectory _cacheDirectory;
    private static String _configUrl;
    private static final AtomicReference<InitializationState> _currentInitializationState = new AtomicReference<>(InitializationState.NOT_INITIALIZED);
    private static boolean _debugMode = false;
    private static final LinkedHashSet<IUnityAdsInitializationListener> _initializationListeners = new LinkedHashSet<>();
    private static long _initializationTime;
    private static long _initializationTimeEpochMs;
    private static boolean _initialized = false;
    private static Configuration _latestConfiguration;
    private static boolean _reinitialized = false;
    private static boolean _testMode = false;

    public enum InitializationState {
        NOT_INITIALIZED,
        INITIALIZING,
        INITIALIZED_SUCCESSFULLY,
        INITIALIZED_FAILED
    }

    public static String getCacheDirectoryName() {
        return CACHE_DIR_NAME;
    }

    public static String getCacheFilePrefix() {
        return LOCAL_CACHE_FILE_PREFIX;
    }

    public static String getLocalStorageFilePrefix() {
        return LOCAL_STORAGE_FILE_PREFIX;
    }

    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static String getVersionName() {
        return "4.4.2";
    }

    public static void notifyInitializationFailed(UnityAds.UnityAdsInitializationError unityAdsInitializationError, String str) {
        setInitializeState(InitializationState.INITIALIZED_FAILED);
        for (IUnityAdsInitializationListener onInitializationFailed : getInitializationListeners()) {
            onInitializationFailed.onInitializationFailed(unityAdsInitializationError, str);
        }
        resetInitializationListeners();
    }

    public static void notifyInitializationComplete() {
        setInitializeState(InitializationState.INITIALIZED_SUCCESSFULLY);
        for (IUnityAdsInitializationListener onInitializationComplete : getInitializationListeners()) {
            onInitializationComplete.onInitializationComplete();
        }
        resetInitializationListeners();
    }

    public static void setInitializeState(InitializationState initializationState) {
        _currentInitializationState.set(initializationState);
    }

    public static InitializationState getCurrentInitializationState() {
        return _currentInitializationState.get();
    }

    public static boolean isInitialized() {
        return _initialized;
    }

    public static void setInitialized(boolean z) {
        _initialized = z;
    }

    public static boolean isTestMode() {
        return _testMode;
    }

    public static void setTestMode(boolean z) {
        _testMode = z;
    }

    public static void setConfigUrl(String str) throws URISyntaxException, MalformedURLException {
        if (str == null) {
            throw new MalformedURLException();
        } else if (str.startsWith("http://") || str.startsWith("https://")) {
            new URL(str).toURI();
            _configUrl = str;
        } else {
            throw new MalformedURLException();
        }
    }

    public static String getConfigUrl() {
        if (_configUrl == null) {
            _configUrl = getDefaultConfigUrl("release");
        }
        return _configUrl;
    }

    public static String getDefaultConfigUrl(String str) {
        String str2 = "https://" + getConfigVersion(ClientProperties.getApplicationContext()) + ".unityads.unity3d.com/webview/";
        if (isChinaLocale(Device.getNetworkCountryISO())) {
            str2 = "https://" + getConfigVersion(ClientProperties.getApplicationContext()) + ".unityads.unitychina.cn/webview/";
        }
        return str2 + getWebViewBranch() + "/" + str + "/config.json";
    }

    public static String getConfigVersion(Context context) {
        if (context == null) {
            return DEFAULT_CONFIG_VERSION;
        }
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            if (bundle != null) {
                return bundle.getString(CONFIG_VERSION_METADATA_KEY, DEFAULT_CONFIG_VERSION);
            }
            return DEFAULT_CONFIG_VERSION;
        } catch (PackageManager.NameNotFoundException unused) {
            DeviceLog.warning("Failed to retrieve application info for current package");
            return DEFAULT_CONFIG_VERSION;
        }
    }

    private static String getWebViewBranch() {
        return getVersionName();
    }

    public static String getLocalWebViewFile() {
        if (getCacheDirectory() == null) {
            return "";
        }
        return getCacheDirectory().getAbsolutePath() + "/UnityAdsWebApp.html";
    }

    public static String getLocalConfigurationFilepath() {
        if (getCacheDirectory() == null) {
            return "";
        }
        return getCacheDirectory().getAbsolutePath() + "/UnityAdsWebViewConfiguration.json";
    }

    public static void setLatestConfiguration(Configuration configuration) {
        _latestConfiguration = configuration;
    }

    public static Configuration getLatestConfiguration() {
        return _latestConfiguration;
    }

    public static File getCacheDirectory() {
        return getCacheDirectory(ClientProperties.getApplicationContext());
    }

    public static File getCacheDirectory(Context context) {
        if (_cacheDirectory == null) {
            setCacheDirectory(new CacheDirectory(CACHE_DIR_NAME));
        }
        return _cacheDirectory.getCacheDirectory(context);
    }

    public static void setCacheDirectory(CacheDirectory cacheDirectory) {
        _cacheDirectory = cacheDirectory;
    }

    public static CacheDirectory getCacheDirectoryObject() {
        return _cacheDirectory;
    }

    public static void setInitializationTime(long j) {
        _initializationTime = j;
    }

    public static long getInitializationTime() {
        return _initializationTime;
    }

    public static void setInitializationTimeSinceEpoch(long j) {
        _initializationTimeEpochMs = j;
    }

    public static long getInitializationTimeEpoch() {
        return _initializationTimeEpochMs;
    }

    public static void setReinitialized(boolean z) {
        _reinitialized = z;
    }

    public static boolean isReinitialized() {
        return _reinitialized;
    }

    public static void setDebugMode(boolean z) {
        _debugMode = z;
        if (z) {
            DeviceLog.setLogLevel(8);
        } else {
            DeviceLog.setLogLevel(4);
        }
    }

    public static boolean getDebugMode() {
        return _debugMode;
    }

    public static void addInitializationListener(IUnityAdsInitializationListener iUnityAdsInitializationListener) {
        if (iUnityAdsInitializationListener != null) {
            synchronized (_initializationListeners) {
                _initializationListeners.add(iUnityAdsInitializationListener);
            }
        }
    }

    public static IUnityAdsInitializationListener[] getInitializationListeners() {
        IUnityAdsInitializationListener[] iUnityAdsInitializationListenerArr;
        synchronized (_initializationListeners) {
            iUnityAdsInitializationListenerArr = new IUnityAdsInitializationListener[_initializationListeners.size()];
            _initializationListeners.toArray(iUnityAdsInitializationListenerArr);
        }
        return iUnityAdsInitializationListenerArr;
    }

    public static void resetInitializationListeners() {
        synchronized (_initializationListeners) {
            _initializationListeners.clear();
        }
    }

    public static boolean isChinaLocale(String str) {
        return str.equalsIgnoreCase(CHINA_ISO_ALPHA_2_CODE) || str.equalsIgnoreCase(CHINA_ISO_ALPHA_3_CODE);
    }
}
