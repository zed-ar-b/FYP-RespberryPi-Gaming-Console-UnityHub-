package com.unity3d.services.core.device;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.preferences.AndroidPreferences;
import com.unity3d.services.core.properties.ClientProperties;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

public class Device {

    public enum MemoryInfoType {
        TOTAL_MEMORY,
        FREE_MEMORY
    }

    public static int getApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static int getScreenLayout() {
        if (ClientProperties.getApplicationContext() != null) {
            return ClientProperties.getApplicationContext().getResources().getConfiguration().screenLayout;
        }
        return -1;
    }

    public static String getAdvertisingTrackingId() {
        return AdvertisingId.getAdvertisingTrackingId();
    }

    public static boolean isLimitAdTrackingEnabled() {
        return AdvertisingId.getLimitedAdTracking();
    }

    public static String getOpenAdvertisingTrackingId() {
        return OpenAdvertisingId.getOpenAdvertisingTrackingId();
    }

    public static boolean isLimitOpenAdTrackingEnabled() {
        return OpenAdvertisingId.getLimitedOpenAdTracking();
    }

    public static boolean isUsingWifi() {
        ConnectivityManager connectivityManager;
        if (ClientProperties.getApplicationContext() == null || (connectivityManager = (ConnectivityManager) ClientProperties.getApplicationContext().getSystemService("connectivity")) == null) {
            return false;
        }
        TelephonyManager telephonyManager = (TelephonyManager) ClientProperties.getApplicationContext().getSystemService("phone");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !connectivityManager.getBackgroundDataSetting() || !connectivityManager.getActiveNetworkInfo().isConnected() || telephonyManager == null || activeNetworkInfo.getType() != 1 || !activeNetworkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public static String getIdfi() {
        String string = AndroidPreferences.getString("unityads-installinfo", "unityads-idfi");
        if (string != null) {
            return string;
        }
        String uniqueEventId = getUniqueEventId();
        AndroidPreferences.setString("unityads-installinfo", "unityads-idfi", uniqueEventId);
        return uniqueEventId;
    }

    public static String getConnectionType() {
        if (isUsingWifi()) {
            return "wifi";
        }
        return isActiveNetworkConnected() ? "cellular" : "none";
    }

    public static int getNetworkType() {
        if (ClientProperties.getApplicationContext() == null) {
            return -1;
        }
        try {
            return ((TelephonyManager) ClientProperties.getApplicationContext().getSystemService("phone")).getNetworkType();
        } catch (SecurityException unused) {
            DeviceLog.warning("Unity Ads was not able to get current network type due to missing permission");
            return -1;
        }
    }

    public static boolean getNetworkMetered() {
        ConnectivityManager connectivityManager;
        if (ClientProperties.getApplicationContext() == null || Build.VERSION.SDK_INT < 16 || (connectivityManager = (ConnectivityManager) ClientProperties.getApplicationContext().getSystemService("connectivity")) == null) {
            return false;
        }
        return connectivityManager.isActiveNetworkMetered();
    }

    public static String getNetworkOperator() {
        return ClientProperties.getApplicationContext() != null ? ((TelephonyManager) ClientProperties.getApplicationContext().getSystemService("phone")).getNetworkOperator() : "";
    }

    public static String getNetworkOperatorName() {
        return ClientProperties.getApplicationContext() != null ? ((TelephonyManager) ClientProperties.getApplicationContext().getSystemService("phone")).getNetworkOperatorName() : "";
    }

    public static String getNetworkCountryISO() {
        return ClientProperties.getApplicationContext() != null ? ((TelephonyManager) ClientProperties.getApplicationContext().getSystemService("phone")).getNetworkCountryIso() : "";
    }

    public static float getDisplayMetricDensity() {
        if (ClientProperties.getApplicationContext() != null) {
            return ClientProperties.getApplicationContext().getResources().getDisplayMetrics().density;
        }
        return -1.0f;
    }

    public static int getScreenDensity() {
        if (ClientProperties.getApplicationContext() != null) {
            return ClientProperties.getApplicationContext().getResources().getDisplayMetrics().densityDpi;
        }
        return -1;
    }

    public static int getScreenWidth() {
        if (ClientProperties.getApplicationContext() != null) {
            return ClientProperties.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        }
        return -1;
    }

    public static int getScreenHeight() {
        if (ClientProperties.getApplicationContext() != null) {
            return ClientProperties.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        }
        return -1;
    }

    public static boolean isActiveNetworkConnected() {
        ConnectivityManager connectivityManager;
        NetworkInfo activeNetworkInfo;
        if (ClientProperties.getApplicationContext() == null || (connectivityManager = (ConnectivityManager) ClientProperties.getApplicationContext().getSystemService("connectivity")) == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    public static String getUniqueEventId() {
        return UUID.randomUUID().toString();
    }

    public static boolean isWiredHeadsetOn() {
        if (ClientProperties.getApplicationContext() != null) {
            return ((AudioManager) ClientProperties.getApplicationContext().getSystemService("audio")).isWiredHeadsetOn();
        }
        return false;
    }

    public static String getSystemProperty(String str, String str2) {
        if (str2 != null) {
            return System.getProperty(str, str2);
        }
        return System.getProperty(str);
    }

    public static int getRingerMode() {
        if (ClientProperties.getApplicationContext() == null) {
            return -1;
        }
        AudioManager audioManager = (AudioManager) ClientProperties.getApplicationContext().getSystemService("audio");
        if (audioManager != null) {
            return audioManager.getRingerMode();
        }
        return -2;
    }

    public static int getStreamVolume(int i) {
        if (ClientProperties.getApplicationContext() == null) {
            return -1;
        }
        AudioManager audioManager = (AudioManager) ClientProperties.getApplicationContext().getSystemService("audio");
        if (audioManager != null) {
            return audioManager.getStreamVolume(i);
        }
        return -2;
    }

    public static int getStreamMaxVolume(int i) {
        if (ClientProperties.getApplicationContext() == null) {
            return -1;
        }
        AudioManager audioManager = (AudioManager) ClientProperties.getApplicationContext().getSystemService("audio");
        if (audioManager != null) {
            return audioManager.getStreamMaxVolume(i);
        }
        return -2;
    }

    public static int getScreenBrightness() {
        if (ClientProperties.getApplicationContext() != null) {
            return Settings.System.getInt(ClientProperties.getApplicationContext().getContentResolver(), "screen_brightness", -1);
        }
        return -1;
    }

    public static long getFreeSpace(File file) {
        if (file == null || !file.exists()) {
            return -1;
        }
        return (long) Math.round((float) (file.getFreeSpace() / 1024));
    }

    public static long getTotalSpace(File file) {
        if (file == null || !file.exists()) {
            return -1;
        }
        return (long) Math.round((float) (file.getTotalSpace() / 1024));
    }

    public static float getBatteryLevel() {
        Intent registerReceiver;
        if (ClientProperties.getApplicationContext() == null || (registerReceiver = ClientProperties.getApplicationContext().registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) == null) {
            return -1.0f;
        }
        return ((float) registerReceiver.getIntExtra("level", -1)) / ((float) registerReceiver.getIntExtra("scale", -1));
    }

    public static int getBatteryStatus() {
        Intent registerReceiver;
        if (ClientProperties.getApplicationContext() == null || (registerReceiver = ClientProperties.getApplicationContext().registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) == null) {
            return -1;
        }
        return registerReceiver.getIntExtra("status", -1);
    }

    public static long getTotalMemory() {
        return getMemoryInfo(MemoryInfoType.TOTAL_MEMORY);
    }

    public static long getFreeMemory() {
        return getMemoryInfo(MemoryInfoType.FREE_MEMORY);
    }

    /* renamed from: com.unity3d.services.core.device.Device$1 */
    static /* synthetic */ class C03011 {

        /* renamed from: $SwitchMap$com$unity3d$services$core$device$Device$MemoryInfoType */
        static final /* synthetic */ int[] f344x950c1f09;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.unity3d.services.core.device.Device$MemoryInfoType[] r0 = com.unity3d.services.core.device.Device.MemoryInfoType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f344x950c1f09 = r0
                com.unity3d.services.core.device.Device$MemoryInfoType r1 = com.unity3d.services.core.device.Device.MemoryInfoType.TOTAL_MEMORY     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f344x950c1f09     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.services.core.device.Device$MemoryInfoType r1 = com.unity3d.services.core.device.Device.MemoryInfoType.FREE_MEMORY     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.device.Device.C03011.<clinit>():void");
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: java.io.RandomAccessFile} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: java.lang.String} */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* JADX WARNING: type inference failed for: r1v12 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long getMemoryInfo(com.unity3d.services.core.device.Device.MemoryInfoType r6) {
        /*
            java.lang.String r0 = "Error closing RandomAccessFile"
            int[] r1 = com.unity3d.services.core.device.Device.C03011.f344x950c1f09
            int r2 = r6.ordinal()
            r1 = r1[r2]
            r2 = 2
            r3 = 1
            if (r1 == r3) goto L_0x0012
            if (r1 == r2) goto L_0x0013
            r2 = -1
            goto L_0x0013
        L_0x0012:
            r2 = 1
        L_0x0013:
            r1 = 0
            java.io.RandomAccessFile r3 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x0038, all -> 0x0036 }
            java.lang.String r4 = "/proc/meminfo"
            java.lang.String r5 = "r"
            r3.<init>(r4, r5)     // Catch:{ IOException -> 0x0038, all -> 0x0036 }
            r4 = 0
        L_0x001e:
            if (r4 >= r2) goto L_0x0027
            java.lang.String r1 = r3.readLine()     // Catch:{ IOException -> 0x0034 }
            int r4 = r4 + 1
            goto L_0x001e
        L_0x0027:
            long r1 = getMemoryValueFromString(r1)     // Catch:{ IOException -> 0x0034 }
            r3.close()     // Catch:{ IOException -> 0x002f }
            goto L_0x0033
        L_0x002f:
            r6 = move-exception
            com.unity3d.services.core.log.DeviceLog.exception(r0, r6)
        L_0x0033:
            return r1
        L_0x0034:
            r1 = move-exception
            goto L_0x003b
        L_0x0036:
            r6 = move-exception
            goto L_0x005c
        L_0x0038:
            r2 = move-exception
            r3 = r1
            r1 = r2
        L_0x003b:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x005a }
            r2.<init>()     // Catch:{ all -> 0x005a }
            java.lang.String r4 = "Error while reading memory info: "
            r2.append(r4)     // Catch:{ all -> 0x005a }
            r2.append(r6)     // Catch:{ all -> 0x005a }
            java.lang.String r6 = r2.toString()     // Catch:{ all -> 0x005a }
            com.unity3d.services.core.log.DeviceLog.exception(r6, r1)     // Catch:{ all -> 0x005a }
            r3.close()     // Catch:{ IOException -> 0x0053 }
            goto L_0x0057
        L_0x0053:
            r6 = move-exception
            com.unity3d.services.core.log.DeviceLog.exception(r0, r6)
        L_0x0057:
            r0 = -1
            return r0
        L_0x005a:
            r6 = move-exception
            r1 = r3
        L_0x005c:
            r1.close()     // Catch:{ IOException -> 0x0060 }
            goto L_0x0064
        L_0x0060:
            r1 = move-exception
            com.unity3d.services.core.log.DeviceLog.exception(r0, r1)
        L_0x0064:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.device.Device.getMemoryInfo(com.unity3d.services.core.device.Device$MemoryInfoType):long");
    }

    private static long getMemoryValueFromString(String str) {
        if (str == null) {
            return -1;
        }
        Matcher matcher = Pattern.compile("(\\d+)").matcher(str);
        String str2 = "";
        while (matcher.find()) {
            str2 = matcher.group(1);
        }
        return Long.parseLong(str2);
    }

    public static boolean isRooted() {
        try {
            return searchPathForBinary("su");
        } catch (Exception e) {
            DeviceLog.exception("Rooted check failed", e);
            return false;
        }
    }

    public static Boolean isAdbEnabled() {
        if (getApiLevel() < 17) {
            return oldAdbStatus();
        }
        return newAdbStatus();
    }

    private static Boolean oldAdbStatus() {
        try {
            boolean z = false;
            if (1 == Settings.Secure.getInt(ClientProperties.getApplicationContext().getContentResolver(), "adb_enabled", 0)) {
                z = true;
            }
            return Boolean.valueOf(z);
        } catch (Exception e) {
            DeviceLog.exception("Problems fetching adb enabled status", e);
            return null;
        }
    }

    private static Boolean newAdbStatus() {
        try {
            boolean z = false;
            if (1 == Settings.Global.getInt(ClientProperties.getApplicationContext().getContentResolver(), "adb_enabled", 0)) {
                z = true;
            }
            return Boolean.valueOf(z);
        } catch (Exception e) {
            DeviceLog.exception("Problems fetching adb enabled status", e);
            return null;
        }
    }

    public static JSONObject getPackageInfo(PackageManager packageManager) throws PackageManager.NameNotFoundException, JSONException {
        String appName = ClientProperties.getAppName();
        PackageInfo packageInfo = packageManager.getPackageInfo(appName, 0);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("installer", packageManager.getInstallerPackageName(appName));
        jSONObject.put("firstInstallTime", packageInfo.firstInstallTime);
        jSONObject.put("lastUpdateTime", packageInfo.lastUpdateTime);
        jSONObject.put("versionCode", packageInfo.versionCode);
        jSONObject.put("versionName", packageInfo.versionName);
        jSONObject.put("packageName", packageInfo.packageName);
        return jSONObject;
    }

    private static boolean searchPathForBinary(String str) {
        File[] listFiles;
        for (String file : System.getenv("PATH").split(":")) {
            File file2 = new File(file);
            if (file2.exists() && file2.isDirectory() && (listFiles = file2.listFiles()) != null) {
                for (File name : listFiles) {
                    if (name.getName().equals(str)) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public static String getGLVersion() {
        ActivityManager activityManager;
        ConfigurationInfo deviceConfigurationInfo;
        if (ClientProperties.getApplicationContext() == null || (activityManager = (ActivityManager) ClientProperties.getApplicationContext().getSystemService("activity")) == null || (deviceConfigurationInfo = activityManager.getDeviceConfigurationInfo()) == null) {
            return null;
        }
        return deviceConfigurationInfo.getGlEsVersion();
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0021 A[SYNTHETIC, Splitter:B:13:0x0021] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getApkDigest() throws java.lang.Exception {
        /*
            android.content.Context r0 = com.unity3d.services.core.properties.ClientProperties.getApplicationContext()
            java.lang.String r0 = r0.getPackageCodePath()
            r1 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ all -> 0x001e }
            java.io.File r3 = new java.io.File     // Catch:{ all -> 0x001e }
            r3.<init>(r0)     // Catch:{ all -> 0x001e }
            r2.<init>(r3)     // Catch:{ all -> 0x001e }
            java.lang.String r0 = com.unity3d.services.core.misc.Utilities.Sha256((java.io.InputStream) r2)     // Catch:{ all -> 0x001b }
            r2.close()     // Catch:{ IOException -> 0x001a }
        L_0x001a:
            return r0
        L_0x001b:
            r0 = move-exception
            r1 = r2
            goto L_0x001f
        L_0x001e:
            r0 = move-exception
        L_0x001f:
            if (r1 == 0) goto L_0x0024
            r1.close()     // Catch:{ IOException -> 0x0024 }
        L_0x0024:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.device.Device.getApkDigest():java.lang.String");
    }

    public static String getCertificateFingerprint() {
        try {
            Signature[] signatureArr = ClientProperties.getApplicationContext().getPackageManager().getPackageInfo(ClientProperties.getApplicationContext().getPackageName(), 64).signatures;
            if (signatureArr == null || signatureArr.length < 1) {
                return null;
            }
            return Utilities.toHexString(MessageDigest.getInstance("SHA-1").digest(((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(signatureArr[0].toByteArray()))).getEncoded()));
        } catch (Exception e) {
            DeviceLog.exception("Exception when signing certificate fingerprint", e);
            return null;
        }
    }

    public static String getBoard() {
        return Build.BOARD;
    }

    public static String getBootloader() {
        return Build.BOOTLOADER;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static String getHardware() {
        return Build.HARDWARE;
    }

    public static String getHost() {
        return Build.HOST;
    }

    public static String getProduct() {
        return Build.PRODUCT;
    }

    public static String getFingerprint() {
        return Build.FINGERPRINT;
    }

    public static ArrayList<String> getSupportedAbis() {
        if (getApiLevel() < 21) {
            return getOldAbiList();
        }
        return getNewAbiList();
    }

    public static List<Sensor> getSensorList() {
        if (ClientProperties.getApplicationContext() != null) {
            return ((SensorManager) ClientProperties.getApplicationContext().getSystemService("sensor")).getSensorList(-1);
        }
        return null;
    }

    public static boolean isUSBConnected() {
        Intent registerReceiver;
        if (ClientProperties.getApplicationContext() == null || (registerReceiver = ClientProperties.getApplicationContext().registerReceiver((BroadcastReceiver) null, new IntentFilter("android.hardware.usb.action.USB_STATE"))) == null) {
            return false;
        }
        return registerReceiver.getBooleanExtra("connected", false);
    }

    public static long getCPUCount() {
        return (long) Runtime.getRuntime().availableProcessors();
    }

    public static long getUptime() {
        return SystemClock.uptimeMillis();
    }

    public static long getElapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }

    public static String getBuildId() {
        return Build.ID;
    }

    public static String getBuildVersionIncremental() {
        return Build.VERSION.INCREMENTAL;
    }

    private static ArrayList<String> getOldAbiList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(Build.CPU_ABI);
        arrayList.add(Build.CPU_ABI2);
        return arrayList;
    }

    private static ArrayList<String> getNewAbiList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.addAll(Arrays.asList(Build.SUPPORTED_ABIS));
        return arrayList;
    }

    public static Map<String, String> getProcessInfo() {
        RandomAccessFile randomAccessFile;
        IOException e;
        HashMap hashMap = new HashMap();
        RandomAccessFile randomAccessFile2 = null;
        try {
            randomAccessFile = new RandomAccessFile("/proc/self/stat", "r");
            try {
                hashMap.put("stat", randomAccessFile.readLine());
                try {
                    randomAccessFile.close();
                } catch (IOException e2) {
                    DeviceLog.exception("Error closing RandomAccessFile", e2);
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    DeviceLog.exception("Error while reading processor info: ", e);
                    randomAccessFile.close();
                    return hashMap;
                } catch (Throwable th) {
                    th = th;
                    randomAccessFile2 = randomAccessFile;
                    try {
                        randomAccessFile2.close();
                    } catch (IOException e4) {
                        DeviceLog.exception("Error closing RandomAccessFile", e4);
                    }
                    throw th;
                }
            }
        } catch (IOException e5) {
            IOException iOException = e5;
            randomAccessFile = null;
            e = iOException;
            DeviceLog.exception("Error while reading processor info: ", e);
            randomAccessFile.close();
            return hashMap;
        } catch (Throwable th2) {
            th = th2;
            randomAccessFile2.close();
            throw th;
        }
        return hashMap;
    }
}
