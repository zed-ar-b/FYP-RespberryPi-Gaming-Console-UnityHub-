package com.unity3d.services.core.api;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.util.SparseArray;
import com.unity3d.ads.metadata.MediationMetaData;
import com.unity3d.services.core.device.Device;
import com.unity3d.services.core.device.DeviceError;
import com.unity3d.services.core.device.IVolumeChangeListener;
import com.unity3d.services.core.device.VolumeChange;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceInfo {
    private static SparseArray<IVolumeChangeListener> _volumeChangeListeners;

    public enum DeviceInfoEvent {
        VOLUME_CHANGED
    }

    public enum StorageType {
        EXTERNAL,
        INTERNAL
    }

    @WebViewExposed
    public static void getAdvertisingTrackingId(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getAdvertisingTrackingId());
    }

    @WebViewExposed
    public static void getLimitAdTrackingFlag(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Boolean.valueOf(Device.isLimitAdTrackingEnabled()));
    }

    @WebViewExposed
    public static void getOpenAdvertisingTrackingId(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getOpenAdvertisingTrackingId());
    }

    @WebViewExposed
    public static void getLimitOpenAdTrackingFlag(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Boolean.valueOf(Device.isLimitOpenAdTrackingEnabled()));
    }

    @WebViewExposed
    public static void getApiLevel(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Integer.valueOf(Device.getApiLevel()));
    }

    @WebViewExposed
    public static void getOsVersion(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getOsVersion());
    }

    @WebViewExposed
    public static void getManufacturer(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getManufacturer());
    }

    @WebViewExposed
    public static void getModel(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getModel());
    }

    @WebViewExposed
    public static void getScreenLayout(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Integer.valueOf(Device.getScreenLayout()));
    }

    @WebViewExposed
    public static void getDisplayMetricDensity(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Float.valueOf(Device.getDisplayMetricDensity()));
    }

    @WebViewExposed
    public static void getScreenDensity(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Integer.valueOf(Device.getScreenDensity()));
    }

    @WebViewExposed
    public static void getScreenWidth(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Integer.valueOf(Device.getScreenWidth()));
    }

    @WebViewExposed
    public static void getScreenHeight(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Integer.valueOf(Device.getScreenHeight()));
    }

    @WebViewExposed
    public static void getTimeZone(Boolean bool, WebViewCallback webViewCallback) {
        webViewCallback.invoke(TimeZone.getDefault().getDisplayName(bool.booleanValue(), 0, Locale.US));
    }

    @WebViewExposed
    public static void getTimeZoneOffset(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Integer.valueOf(TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000));
    }

    @WebViewExposed
    public static void getConnectionType(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getConnectionType());
    }

    @WebViewExposed
    public static void getNetworkType(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Integer.valueOf(Device.getNetworkType()));
    }

    @WebViewExposed
    public static void getNetworkMetered(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Boolean.valueOf(Device.getNetworkMetered()));
    }

    @WebViewExposed
    public static void getNetworkOperator(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getNetworkOperator());
    }

    @WebViewExposed
    public static void getNetworkOperatorName(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getNetworkOperatorName());
    }

    @WebViewExposed
    public static void getNetworkCountryISO(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getNetworkCountryISO());
    }

    @WebViewExposed
    public static void isRooted(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Boolean.valueOf(Device.isRooted()));
    }

    @WebViewExposed
    public static void isAdbEnabled(WebViewCallback webViewCallback) {
        Boolean isAdbEnabled = Device.isAdbEnabled();
        if (isAdbEnabled != null) {
            webViewCallback.invoke(isAdbEnabled);
            return;
        }
        webViewCallback.error(DeviceError.COULDNT_GET_ADB_STATUS, new Object[0]);
    }

    @WebViewExposed
    public static void getPackageInfo(WebViewCallback webViewCallback) {
        if (ClientProperties.getApplicationContext() != null) {
            String appName = ClientProperties.getAppName();
            try {
                webViewCallback.invoke(Device.getPackageInfo(ClientProperties.getApplicationContext().getPackageManager()));
            } catch (PackageManager.NameNotFoundException unused) {
                webViewCallback.error(DeviceError.APPLICATION_INFO_NOT_AVAILABLE, appName);
            } catch (JSONException e) {
                webViewCallback.error(DeviceError.JSON_ERROR, e.getMessage());
            }
        } else {
            webViewCallback.error(DeviceError.APPLICATION_CONTEXT_NULL, new Object[0]);
        }
    }

    @WebViewExposed
    public static void getUniqueEventId(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getUniqueEventId());
    }

    @WebViewExposed
    public static void getHeadset(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Boolean.valueOf(Device.isWiredHeadsetOn()));
    }

    @WebViewExposed
    public static void getSystemProperty(String str, String str2, WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getSystemProperty(str, str2));
    }

    @WebViewExposed
    public static void getRingerMode(WebViewCallback webViewCallback) {
        int ringerMode = Device.getRingerMode();
        if (ringerMode > -1) {
            webViewCallback.invoke(Integer.valueOf(ringerMode));
        } else if (ringerMode == -2) {
            webViewCallback.error(DeviceError.AUDIOMANAGER_NULL, Integer.valueOf(ringerMode));
        } else if (ringerMode != -1) {
            DeviceLog.error("Unhandled ringerMode error: " + ringerMode);
        } else {
            webViewCallback.error(DeviceError.APPLICATION_CONTEXT_NULL, Integer.valueOf(ringerMode));
        }
    }

    @WebViewExposed
    public static void getSystemLanguage(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Locale.getDefault().toString());
    }

    @WebViewExposed
    public static void getDeviceVolume(Integer num, WebViewCallback webViewCallback) {
        int streamVolume = Device.getStreamVolume(num.intValue());
        if (streamVolume > -1) {
            webViewCallback.invoke(Integer.valueOf(streamVolume));
        } else if (streamVolume == -2) {
            webViewCallback.error(DeviceError.AUDIOMANAGER_NULL, Integer.valueOf(streamVolume));
        } else if (streamVolume != -1) {
            DeviceLog.error("Unhandled deviceVolume error: " + streamVolume);
        } else {
            webViewCallback.error(DeviceError.APPLICATION_CONTEXT_NULL, Integer.valueOf(streamVolume));
        }
    }

    @WebViewExposed
    public static void getDeviceMaxVolume(Integer num, WebViewCallback webViewCallback) {
        int streamMaxVolume = Device.getStreamMaxVolume(num.intValue());
        if (streamMaxVolume > -1) {
            webViewCallback.invoke(Integer.valueOf(streamMaxVolume));
        } else if (streamMaxVolume == -2) {
            webViewCallback.error(DeviceError.AUDIOMANAGER_NULL, Integer.valueOf(streamMaxVolume));
        } else if (streamMaxVolume != -1) {
            DeviceLog.error("Unhandled deviceMaxVolume error: " + streamMaxVolume);
        } else {
            webViewCallback.error(DeviceError.APPLICATION_CONTEXT_NULL, Integer.valueOf(streamMaxVolume));
        }
    }

    @WebViewExposed
    public static void registerVolumeChangeListener(final Integer num, WebViewCallback webViewCallback) {
        if (_volumeChangeListeners == null) {
            _volumeChangeListeners = new SparseArray<>();
        }
        if (_volumeChangeListeners.get(num.intValue()) == null) {
            C02811 r0 = new IVolumeChangeListener() {
                private int _streamType = num.intValue();

                public void onVolumeChanged(int i) {
                    WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.DEVICEINFO, DeviceInfoEvent.VOLUME_CHANGED, Integer.valueOf(getStreamType()), Integer.valueOf(i), Integer.valueOf(Device.getStreamMaxVolume(this._streamType)));
                }

                public int getStreamType() {
                    return this._streamType;
                }
            };
            _volumeChangeListeners.append(num.intValue(), r0);
            VolumeChange.registerListener(r0);
        }
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void unregisterVolumeChangeListener(Integer num, WebViewCallback webViewCallback) {
        SparseArray<IVolumeChangeListener> sparseArray = _volumeChangeListeners;
        if (!(sparseArray == null || sparseArray.get(num.intValue()) == null)) {
            VolumeChange.unregisterListener(_volumeChangeListeners.get(num.intValue()));
            _volumeChangeListeners.remove(num.intValue());
        }
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void getScreenBrightness(WebViewCallback webViewCallback) {
        int screenBrightness = Device.getScreenBrightness();
        if (screenBrightness > -1) {
            webViewCallback.invoke(Integer.valueOf(screenBrightness));
        } else if (screenBrightness != -1) {
            DeviceLog.error("Unhandled screenBrightness error: " + screenBrightness);
        } else {
            webViewCallback.error(DeviceError.APPLICATION_CONTEXT_NULL, Integer.valueOf(screenBrightness));
        }
    }

    private static StorageType getStorageTypeFromString(String str) {
        try {
            return StorageType.valueOf(str);
        } catch (IllegalArgumentException e) {
            DeviceLog.exception("Illegal argument: " + str, e);
            return null;
        }
    }

    /* renamed from: com.unity3d.services.core.api.DeviceInfo$2 */
    static /* synthetic */ class C02822 {
        static final /* synthetic */ int[] $SwitchMap$com$unity3d$services$core$api$DeviceInfo$StorageType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.unity3d.services.core.api.DeviceInfo$StorageType[] r0 = com.unity3d.services.core.api.DeviceInfo.StorageType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$unity3d$services$core$api$DeviceInfo$StorageType = r0
                com.unity3d.services.core.api.DeviceInfo$StorageType r1 = com.unity3d.services.core.api.DeviceInfo.StorageType.INTERNAL     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$unity3d$services$core$api$DeviceInfo$StorageType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.services.core.api.DeviceInfo$StorageType r1 = com.unity3d.services.core.api.DeviceInfo.StorageType.EXTERNAL     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.api.DeviceInfo.C02822.<clinit>():void");
        }
    }

    private static File getFileForStorageType(StorageType storageType) {
        int i = C02822.$SwitchMap$com$unity3d$services$core$api$DeviceInfo$StorageType[storageType.ordinal()];
        if (i == 1) {
            return ClientProperties.getApplicationContext().getCacheDir();
        }
        if (i == 2) {
            return ClientProperties.getApplicationContext().getExternalCacheDir();
        }
        DeviceLog.error("Unhandled storagetype: " + storageType);
        return null;
    }

    @WebViewExposed
    public static void getFreeSpace(String str, WebViewCallback webViewCallback) {
        StorageType storageTypeFromString = getStorageTypeFromString(str);
        if (storageTypeFromString == null) {
            webViewCallback.error(DeviceError.INVALID_STORAGETYPE, str);
            return;
        }
        long freeSpace = Device.getFreeSpace(getFileForStorageType(storageTypeFromString));
        if (freeSpace > -1) {
            webViewCallback.invoke(Long.valueOf(freeSpace));
            return;
        }
        webViewCallback.error(DeviceError.COULDNT_GET_STORAGE_LOCATION, Long.valueOf(freeSpace));
    }

    @WebViewExposed
    public static void getTotalSpace(String str, WebViewCallback webViewCallback) {
        StorageType storageTypeFromString = getStorageTypeFromString(str);
        if (storageTypeFromString == null) {
            webViewCallback.error(DeviceError.INVALID_STORAGETYPE, str);
            return;
        }
        long totalSpace = Device.getTotalSpace(getFileForStorageType(storageTypeFromString));
        if (totalSpace > -1) {
            webViewCallback.invoke(Long.valueOf(totalSpace));
            return;
        }
        webViewCallback.error(DeviceError.COULDNT_GET_STORAGE_LOCATION, Long.valueOf(totalSpace));
    }

    @WebViewExposed
    public static void getBatteryLevel(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Float.valueOf(Device.getBatteryLevel()));
    }

    @WebViewExposed
    public static void getBatteryStatus(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Integer.valueOf(Device.getBatteryStatus()));
    }

    @WebViewExposed
    public static void getFreeMemory(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Long.valueOf(Device.getFreeMemory()));
    }

    @WebViewExposed
    public static void getTotalMemory(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Long.valueOf(Device.getTotalMemory()));
    }

    @WebViewExposed
    public static void getGLVersion(WebViewCallback webViewCallback) {
        String gLVersion = Device.getGLVersion();
        if (gLVersion != null) {
            webViewCallback.invoke(gLVersion);
            return;
        }
        webViewCallback.error(DeviceError.COULDNT_GET_GL_VERSION, new Object[0]);
    }

    @WebViewExposed
    public static void getApkDigest(WebViewCallback webViewCallback) {
        try {
            webViewCallback.invoke(Device.getApkDigest());
        } catch (Exception e) {
            webViewCallback.error(DeviceError.COULDNT_GET_DIGEST, e.toString());
        }
    }

    @WebViewExposed
    public static void getCertificateFingerprint(WebViewCallback webViewCallback) {
        String certificateFingerprint = Device.getCertificateFingerprint();
        if (certificateFingerprint != null) {
            webViewCallback.invoke(certificateFingerprint);
            return;
        }
        webViewCallback.error(DeviceError.COULDNT_GET_FINGERPRINT, new Object[0]);
    }

    @WebViewExposed
    public static void getBoard(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getBoard());
    }

    @WebViewExposed
    public static void getBootloader(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getBootloader());
    }

    @WebViewExposed
    public static void getBrand(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getBrand());
    }

    @WebViewExposed
    public static void getDevice(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getDevice());
    }

    @WebViewExposed
    public static void getHardware(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getHardware());
    }

    @WebViewExposed
    public static void getHost(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getHost());
    }

    @WebViewExposed
    public static void getProduct(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getProduct());
    }

    @WebViewExposed
    public static void getFingerprint(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getFingerprint());
    }

    @WebViewExposed
    public static void getSupportedAbis(WebViewCallback webViewCallback) {
        JSONArray jSONArray = new JSONArray();
        Iterator<String> it = Device.getSupportedAbis().iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next());
        }
        webViewCallback.invoke(jSONArray);
    }

    @WebViewExposed
    public static void getSensorList(WebViewCallback webViewCallback) {
        JSONArray jSONArray = new JSONArray();
        List<Sensor> sensorList = Device.getSensorList();
        if (sensorList != null) {
            for (Sensor next : sensorList) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put(MediationMetaData.KEY_NAME, next.getName());
                    jSONObject.put("type", next.getType());
                    jSONObject.put("vendor", next.getVendor());
                    jSONObject.put("maximumRange", (double) next.getMaximumRange());
                    jSONObject.put("power", (double) next.getPower());
                    jSONObject.put(MediationMetaData.KEY_VERSION, next.getVersion());
                    jSONObject.put("resolution", (double) next.getResolution());
                    jSONObject.put("minDelay", next.getMinDelay());
                    jSONArray.put(jSONObject);
                } catch (JSONException e) {
                    webViewCallback.error(DeviceError.JSON_ERROR, e.getMessage());
                    return;
                }
            }
        }
        webViewCallback.invoke(jSONArray);
    }

    @WebViewExposed
    public static void getProcessInfo(WebViewCallback webViewCallback) {
        JSONObject jSONObject = new JSONObject();
        Map<String, String> processInfo = Device.getProcessInfo();
        if (processInfo != null) {
            try {
                if (processInfo.containsKey("stat")) {
                    jSONObject.put("stat", processInfo.get("stat"));
                }
                if (processInfo.containsKey("uptime")) {
                    jSONObject.put("uptime", processInfo.get("uptime"));
                }
            } catch (Exception e) {
                DeviceLog.exception("Error while constructing process info", e);
            }
        }
        webViewCallback.invoke(jSONObject);
    }

    @WebViewExposed
    public static void isUSBConnected(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Boolean.valueOf(Device.isUSBConnected()));
    }

    @WebViewExposed
    public static void getCPUCount(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Long.valueOf(Device.getCPUCount()));
    }

    @WebViewExposed
    public static void getUptime(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Long.valueOf(Device.getUptime()));
    }

    @WebViewExposed
    public static void getElapsedRealtime(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Long.valueOf(Device.getElapsedRealtime()));
    }

    @WebViewExposed
    public static void getBuildId(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getBuildId());
    }

    @WebViewExposed
    public static void getBuildVersionIncremental(WebViewCallback webViewCallback) {
        webViewCallback.invoke(Device.getBuildVersionIncremental());
    }
}
