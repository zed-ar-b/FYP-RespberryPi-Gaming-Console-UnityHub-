package com.unity3d.services.core.device.reader;

import android.webkit.WebSettings;
import com.unity3d.services.core.device.Device;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.properties.SdkProperties;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DeviceInfoReaderExtended implements IDeviceInfoReader {
    private final IDeviceInfoReader _deviceInfoReader;

    public DeviceInfoReaderExtended(IDeviceInfoReader iDeviceInfoReader) {
        this._deviceInfoReader = iDeviceInfoReader;
    }

    public Map<String, Object> getDeviceInfoData() {
        Map<String, Object> deviceInfoData = this._deviceInfoReader.getDeviceInfoData();
        deviceInfoData.put("bundleId", ClientProperties.getAppName());
        deviceInfoData.put("encrypted", Boolean.valueOf(ClientProperties.isAppDebuggable()));
        deviceInfoData.put("rooted", Boolean.valueOf(Device.isRooted()));
        deviceInfoData.put("osVersion", Device.getOsVersion());
        deviceInfoData.put("deviceModel", Device.getModel());
        deviceInfoData.put("language", Locale.getDefault().toString());
        deviceInfoData.put("connectionType", Device.getConnectionType());
        deviceInfoData.put("screenHeight", Integer.valueOf(Device.getScreenHeight()));
        deviceInfoData.put("screenWidth", Integer.valueOf(Device.getScreenWidth()));
        deviceInfoData.put("deviceMake", Device.getManufacturer());
        deviceInfoData.put("screenDensity", Integer.valueOf(Device.getScreenDensity()));
        deviceInfoData.put("screenSize", Integer.valueOf(Device.getScreenLayout()));
        deviceInfoData.put("limitAdTracking", Boolean.valueOf(Device.isLimitAdTrackingEnabled()));
        deviceInfoData.put("networkOperator", Device.getNetworkOperator());
        deviceInfoData.put("volume", Integer.valueOf(Device.getStreamVolume(1)));
        deviceInfoData.put("deviceFreeSpace", Long.valueOf(Device.getFreeSpace(ClientProperties.getApplicationContext().getCacheDir())));
        deviceInfoData.put("apiLevel", String.valueOf(Device.getApiLevel()));
        deviceInfoData.put("networkType", Integer.valueOf(Device.getNetworkType()));
        deviceInfoData.put("bundleVersion", ClientProperties.getAppVersion());
        try {
            deviceInfoData.put("timeZone", TimeZone.getDefault().getDisplayName(false, 0, Locale.US));
        } catch (AssertionError e) {
            DeviceLog.error("Could not read timeZone information: %s", e.getMessage());
        }
        deviceInfoData.put("timeZoneOffset", Integer.valueOf(TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000));
        deviceInfoData.put("webviewUa", WebSettings.getDefaultUserAgent(ClientProperties.getApplicationContext()));
        deviceInfoData.put("networkOperatorName", Device.getNetworkOperatorName());
        deviceInfoData.put("wiredHeadset", Boolean.valueOf(Device.isWiredHeadsetOn()));
        deviceInfoData.put("versionCode", Integer.valueOf(SdkProperties.getVersionCode()));
        deviceInfoData.put("stores", "google");
        deviceInfoData.put("appStartTime", Long.valueOf(SdkProperties.getInitializationTimeEpoch() / 1000));
        deviceInfoData.put("sdkVersionName", SdkProperties.getVersionName());
        deviceInfoData.put("eventTimeStamp", Long.valueOf(System.currentTimeMillis() / 1000));
        deviceInfoData.put("cpuCount", Long.valueOf(Device.getCPUCount()));
        deviceInfoData.put("usbConnected", Boolean.valueOf(Device.isUSBConnected()));
        try {
            deviceInfoData.put("apkHash", Device.getApkDigest());
        } catch (Exception unused) {
            DeviceLog.error("Could not get APK Digest");
        }
        deviceInfoData.put("apkDeveloperSigningCertificateHash", Device.getCertificateFingerprint());
        deviceInfoData.put("deviceUpTime", Long.valueOf(Device.getUptime()));
        deviceInfoData.put("deviceElapsedRealtime", Long.valueOf(Device.getElapsedRealtime()));
        deviceInfoData.put("adbEnabled", Device.isAdbEnabled());
        deviceInfoData.put("androidFingerprint", Device.getFingerprint());
        deviceInfoData.put("batteryStatus", Integer.valueOf(Device.getBatteryStatus()));
        deviceInfoData.put("batteryLevel", Float.valueOf(Device.getBatteryLevel()));
        deviceInfoData.put("networkMetered", Boolean.valueOf(Device.getNetworkMetered()));
        deviceInfoData.put("test", Boolean.valueOf(SdkProperties.isTestMode()));
        deviceInfoData.put("callType", "token");
        return deviceInfoData;
    }
}
