package com.unity3d.services.core.misc;

import android.os.Handler;
import android.os.Looper;
import com.unity3d.services.core.log.DeviceLog;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class Utilities {
    public static void runOnUiThread(Runnable runnable) {
        runOnUiThread(runnable, 0);
    }

    public static void runOnUiThread(Runnable runnable, long j) {
        Handler handler = new Handler(Looper.getMainLooper());
        if (j > 0) {
            handler.postDelayed(runnable, j);
        } else {
            handler.post(runnable);
        }
    }

    public static String Sha256(String str) {
        return Sha256(str.getBytes());
    }

    public static String Sha256(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(bArr, 0, bArr.length);
            return toHexString(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            DeviceLog.exception("SHA-256 algorithm not found", e);
            return null;
        }
    }

    public static String Sha256(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            byte[] bArr = new byte[4096];
            while (true) {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    return toHexString(instance.digest());
                }
                instance.update(bArr, 0, read);
            }
        } catch (NoSuchAlgorithmException e) {
            DeviceLog.exception("SHA-256 algorithm not found", e);
            return null;
        }
    }

    public static String toHexString(byte[] bArr) {
        String str = "";
        for (byte b : bArr) {
            byte b2 = b & 255;
            if (b2 <= 15) {
                str = str + "0";
            }
            str = str + Integer.toHexString(b2);
        }
        return str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0031 A[SYNTHETIC, Splitter:B:23:0x0031] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0056 A[SYNTHETIC, Splitter:B:31:0x0056] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean writeFile(java.io.File r5, java.lang.String r6) {
        /*
            java.lang.String r0 = "Error closing FileOutputStream"
            r1 = 0
            if (r5 != 0) goto L_0x0006
            return r1
        L_0x0006:
            r2 = 0
            r3 = 1
            java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0029 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0029 }
            byte[] r6 = r6.getBytes()     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r4.write(r6)     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r4.flush()     // Catch:{ Exception -> 0x0024, all -> 0x0021 }
            r4.close()     // Catch:{ Exception -> 0x001b }
            goto L_0x001f
        L_0x001b:
            r6 = move-exception
            com.unity3d.services.core.log.DeviceLog.exception(r0, r6)
        L_0x001f:
            r1 = 1
            goto L_0x0039
        L_0x0021:
            r5 = move-exception
            r2 = r4
            goto L_0x0054
        L_0x0024:
            r6 = move-exception
            r2 = r4
            goto L_0x002a
        L_0x0027:
            r5 = move-exception
            goto L_0x0054
        L_0x0029:
            r6 = move-exception
        L_0x002a:
            java.lang.String r3 = "Could not write file"
            com.unity3d.services.core.log.DeviceLog.exception(r3, r6)     // Catch:{ all -> 0x0027 }
            if (r2 == 0) goto L_0x0039
            r2.close()     // Catch:{ Exception -> 0x0035 }
            goto L_0x0039
        L_0x0035:
            r6 = move-exception
            com.unity3d.services.core.log.DeviceLog.exception(r0, r6)
        L_0x0039:
            if (r1 == 0) goto L_0x0053
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "Wrote file: "
            r6.append(r0)
            java.lang.String r5 = r5.getAbsolutePath()
            r6.append(r5)
            java.lang.String r5 = r6.toString()
            com.unity3d.services.core.log.DeviceLog.debug(r5)
        L_0x0053:
            return r1
        L_0x0054:
            if (r2 == 0) goto L_0x005e
            r2.close()     // Catch:{ Exception -> 0x005a }
            goto L_0x005e
        L_0x005a:
            r6 = move-exception
            com.unity3d.services.core.log.DeviceLog.exception(r0, r6)
        L_0x005e:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.misc.Utilities.writeFile(java.io.File, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0050  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] readFileBytes(java.io.File r4) throws java.io.IOException {
        /*
            r0 = 0
            if (r4 == 0) goto L_0x0054
            boolean r1 = r4.exists()
            if (r1 != 0) goto L_0x000a
            goto L_0x0054
        L_0x000a:
            int r1 = android.os.Build.VERSION.SDK_INT
            r2 = 26
            if (r1 < r2) goto L_0x0019
            java.nio.file.Path r4 = r4.toPath()
            byte[] r4 = java.nio.file.Files.readAllBytes(r4)
            return r4
        L_0x0019:
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x004d }
            r1.<init>(r4)     // Catch:{ all -> 0x004d }
            long r2 = r4.length()     // Catch:{ all -> 0x004a }
            int r0 = (int) r2     // Catch:{ all -> 0x004a }
            byte[] r2 = new byte[r0]     // Catch:{ all -> 0x004a }
            int r3 = r1.read(r2)     // Catch:{ all -> 0x004a }
            if (r3 != r0) goto L_0x002f
            r1.close()
            return r2
        L_0x002f:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x004a }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x004a }
            r2.<init>()     // Catch:{ all -> 0x004a }
            java.lang.String r3 = "Failed to read all bytes from input file path: "
            r2.append(r3)     // Catch:{ all -> 0x004a }
            java.lang.String r4 = r4.getPath()     // Catch:{ all -> 0x004a }
            r2.append(r4)     // Catch:{ all -> 0x004a }
            java.lang.String r4 = r2.toString()     // Catch:{ all -> 0x004a }
            r0.<init>(r4)     // Catch:{ all -> 0x004a }
            throw r0     // Catch:{ all -> 0x004a }
        L_0x004a:
            r4 = move-exception
            r0 = r1
            goto L_0x004e
        L_0x004d:
            r4 = move-exception
        L_0x004e:
            if (r0 == 0) goto L_0x0053
            r0.close()
        L_0x0053:
            throw r4
        L_0x0054:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.misc.Utilities.readFileBytes(java.io.File):byte[]");
    }

    public static JSONObject mergeJsonObjects(JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        if (jSONObject == null) {
            return jSONObject2;
        }
        if (jSONObject2 == null) {
            return jSONObject;
        }
        JSONObject jSONObject3 = new JSONObject();
        Iterator<String> keys = jSONObject2.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            jSONObject3.put(next, jSONObject2.get(next));
        }
        Iterator<String> keys2 = jSONObject.keys();
        while (keys2.hasNext()) {
            String next2 = keys2.next();
            if (!jSONObject3.has(next2) || !(jSONObject3.get(next2) instanceof JSONObject) || !(jSONObject.get(next2) instanceof JSONObject)) {
                jSONObject3.put(next2, jSONObject.get(next2));
            } else {
                jSONObject3.put(next2, mergeJsonObjects(jSONObject.getJSONObject(next2), jSONObject3.getJSONObject(next2)));
            }
        }
        return jSONObject3;
    }

    public static Map<String, Object> combineJsonIntoMap(Map<String, Object> map, JSONObject jSONObject, String str) {
        HashMap hashMap = new HashMap(map);
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            hashMap.put(str + next, jSONObject.opt(next));
        }
        return hashMap;
    }

    public static Map<String, Object> combineJsonIntoMap(Map<String, Object> map, JSONObject jSONObject) {
        return combineJsonIntoMap(map, jSONObject, "");
    }

    public static Map<String, Object> convertJsonToMap(JSONObject jSONObject) {
        return combineJsonIntoMap(new HashMap(), jSONObject);
    }
}
