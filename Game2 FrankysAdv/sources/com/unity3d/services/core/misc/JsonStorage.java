package com.unity3d.services.core.misc;

import android.text.TextUtils;
import com.unity3d.services.core.log.DeviceLog;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONObject;

public class JsonStorage implements IJsonStorageReader {
    private JSONObject _data;

    public synchronized boolean initData() {
        if (this._data != null) {
            return false;
        }
        this._data = new JSONObject();
        return true;
    }

    public synchronized void setData(JSONObject jSONObject) {
        this._data = jSONObject;
    }

    public synchronized JSONObject getData() {
        return this._data;
    }

    public synchronized boolean hasData() {
        if (this._data == null || this._data.length() <= 0) {
            return false;
        }
        return true;
    }

    public synchronized void clearData() {
        this._data = null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0048, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean set(java.lang.String r5, java.lang.Object r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            org.json.JSONObject r0 = r4._data     // Catch:{ all -> 0x0078 }
            r1 = 0
            if (r0 == 0) goto L_0x0050
            if (r5 == 0) goto L_0x0050
            int r0 = r5.length()     // Catch:{ all -> 0x0078 }
            if (r0 == 0) goto L_0x0050
            if (r6 != 0) goto L_0x0011
            goto L_0x0050
        L_0x0011:
            java.lang.String r0 = r4.getParentObjectTreeFor(r5)     // Catch:{ all -> 0x0078 }
            r4.createObjectTree(r0)     // Catch:{ all -> 0x0078 }
            java.lang.String r0 = r4.getParentObjectTreeFor(r5)     // Catch:{ all -> 0x0078 }
            java.lang.Object r0 = r4.findObject(r0)     // Catch:{ all -> 0x0078 }
            boolean r0 = r0 instanceof org.json.JSONObject     // Catch:{ all -> 0x0078 }
            if (r0 == 0) goto L_0x0049
            java.lang.String r0 = r4.getParentObjectTreeFor(r5)     // Catch:{ all -> 0x0078 }
            java.lang.Object r0 = r4.findObject(r0)     // Catch:{ all -> 0x0078 }
            org.json.JSONObject r0 = (org.json.JSONObject) r0     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = "\\."
            java.lang.String[] r5 = r5.split(r2)     // Catch:{ all -> 0x0078 }
            r2 = 1
            if (r0 == 0) goto L_0x0047
            int r3 = r5.length     // Catch:{ JSONException -> 0x003f }
            int r3 = r3 - r2
            r5 = r5[r3]     // Catch:{ JSONException -> 0x003f }
            r0.put(r5, r6)     // Catch:{ JSONException -> 0x003f }
            goto L_0x0047
        L_0x003f:
            r5 = move-exception
            java.lang.String r6 = "Couldn't set value"
            com.unity3d.services.core.log.DeviceLog.exception(r6, r5)     // Catch:{ all -> 0x0078 }
            monitor-exit(r4)
            return r1
        L_0x0047:
            monitor-exit(r4)
            return r2
        L_0x0049:
            java.lang.String r5 = "Cannot set subvalue to an object that is not JSONObject"
            com.unity3d.services.core.log.DeviceLog.debug(r5)     // Catch:{ all -> 0x0078 }
            monitor-exit(r4)
            return r1
        L_0x0050:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0078 }
            r0.<init>()     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = "Storage not properly initialized or incorrect parameters:"
            r0.append(r2)     // Catch:{ all -> 0x0078 }
            org.json.JSONObject r2 = r4._data     // Catch:{ all -> 0x0078 }
            r0.append(r2)     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = ", "
            r0.append(r2)     // Catch:{ all -> 0x0078 }
            r0.append(r5)     // Catch:{ all -> 0x0078 }
            java.lang.String r5 = ", "
            r0.append(r5)     // Catch:{ all -> 0x0078 }
            r0.append(r6)     // Catch:{ all -> 0x0078 }
            java.lang.String r5 = r0.toString()     // Catch:{ all -> 0x0078 }
            com.unity3d.services.core.log.DeviceLog.error(r5)     // Catch:{ all -> 0x0078 }
            monitor-exit(r4)
            return r1
        L_0x0078:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.misc.JsonStorage.set(java.lang.String, java.lang.Object):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004a, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.Object get(java.lang.String r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            org.json.JSONObject r0 = r3._data     // Catch:{ all -> 0x004b }
            r1 = 0
            if (r0 != 0) goto L_0x000d
            java.lang.String r4 = "Data is NULL, readStorage probably not called"
            com.unity3d.services.core.log.DeviceLog.error(r4)     // Catch:{ all -> 0x004b }
            monitor-exit(r3)
            return r1
        L_0x000d:
            java.lang.String r0 = "\\."
            java.lang.String[] r0 = r4.split(r0)     // Catch:{ all -> 0x004b }
            java.lang.String r2 = r3.getParentObjectTreeFor(r4)     // Catch:{ all -> 0x004b }
            java.lang.Object r2 = r3.findObject(r2)     // Catch:{ all -> 0x004b }
            boolean r2 = r2 instanceof org.json.JSONObject     // Catch:{ all -> 0x004b }
            if (r2 == 0) goto L_0x0049
            java.lang.String r4 = r3.getParentObjectTreeFor(r4)     // Catch:{ all -> 0x004b }
            java.lang.Object r4 = r3.findObject(r4)     // Catch:{ all -> 0x004b }
            org.json.JSONObject r4 = (org.json.JSONObject) r4     // Catch:{ all -> 0x004b }
            if (r4 == 0) goto L_0x0049
            int r2 = r0.length     // Catch:{ Exception -> 0x0041 }
            int r2 = r2 + -1
            r2 = r0[r2]     // Catch:{ Exception -> 0x0041 }
            boolean r2 = r4.has(r2)     // Catch:{ Exception -> 0x0041 }
            if (r2 == 0) goto L_0x0047
            int r2 = r0.length     // Catch:{ Exception -> 0x0041 }
            int r2 = r2 + -1
            r0 = r0[r2]     // Catch:{ Exception -> 0x0041 }
            java.lang.Object r4 = r4.get(r0)     // Catch:{ Exception -> 0x0041 }
            r1 = r4
            goto L_0x0047
        L_0x0041:
            r4 = move-exception
            java.lang.String r0 = "Error getting data"
            com.unity3d.services.core.log.DeviceLog.exception(r0, r4)     // Catch:{ all -> 0x004b }
        L_0x0047:
            monitor-exit(r3)
            return r1
        L_0x0049:
            monitor-exit(r3)
            return r1
        L_0x004b:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.misc.JsonStorage.get(java.lang.String):java.lang.Object");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0071, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.List<java.lang.String> getKeys(java.lang.String r9, boolean r10) {
        /*
            r8 = this;
            monitor-enter(r8)
            java.lang.Object r0 = r8.get(r9)     // Catch:{ all -> 0x0074 }
            boolean r0 = r0 instanceof org.json.JSONObject     // Catch:{ all -> 0x0074 }
            r1 = 0
            if (r0 == 0) goto L_0x0072
            java.lang.Object r0 = r8.get(r9)     // Catch:{ all -> 0x0074 }
            org.json.JSONObject r0 = (org.json.JSONObject) r0     // Catch:{ all -> 0x0074 }
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0074 }
            r2.<init>()     // Catch:{ all -> 0x0074 }
            if (r0 == 0) goto L_0x0070
            java.util.Iterator r0 = r0.keys()     // Catch:{ all -> 0x0074 }
        L_0x001b:
            boolean r3 = r0.hasNext()     // Catch:{ all -> 0x0074 }
            if (r3 == 0) goto L_0x0070
            java.lang.Object r3 = r0.next()     // Catch:{ all -> 0x0074 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0074 }
            if (r10 == 0) goto L_0x0042
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0074 }
            r4.<init>()     // Catch:{ all -> 0x0074 }
            r4.append(r9)     // Catch:{ all -> 0x0074 }
            java.lang.String r5 = "."
            r4.append(r5)     // Catch:{ all -> 0x0074 }
            r4.append(r3)     // Catch:{ all -> 0x0074 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0074 }
            java.util.List r4 = r8.getKeys(r4, r10)     // Catch:{ all -> 0x0074 }
            goto L_0x0043
        L_0x0042:
            r4 = r1
        L_0x0043:
            r2.add(r3)     // Catch:{ all -> 0x0074 }
            if (r4 == 0) goto L_0x001b
            java.util.Iterator r4 = r4.iterator()     // Catch:{ all -> 0x0074 }
        L_0x004c:
            boolean r5 = r4.hasNext()     // Catch:{ all -> 0x0074 }
            if (r5 == 0) goto L_0x001b
            java.lang.Object r5 = r4.next()     // Catch:{ all -> 0x0074 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ all -> 0x0074 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x0074 }
            r6.<init>()     // Catch:{ all -> 0x0074 }
            r6.append(r3)     // Catch:{ all -> 0x0074 }
            java.lang.String r7 = "."
            r6.append(r7)     // Catch:{ all -> 0x0074 }
            r6.append(r5)     // Catch:{ all -> 0x0074 }
            java.lang.String r5 = r6.toString()     // Catch:{ all -> 0x0074 }
            r2.add(r5)     // Catch:{ all -> 0x0074 }
            goto L_0x004c
        L_0x0070:
            monitor-exit(r8)
            return r2
        L_0x0072:
            monitor-exit(r8)
            return r1
        L_0x0074:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.misc.JsonStorage.getKeys(java.lang.String, boolean):java.util.List");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0039, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean delete(java.lang.String r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            org.json.JSONObject r0 = r4._data     // Catch:{ all -> 0x003a }
            r1 = 0
            if (r0 != 0) goto L_0x000d
            java.lang.String r5 = "Data is NULL, readStorage probably not called"
            com.unity3d.services.core.log.DeviceLog.error(r5)     // Catch:{ all -> 0x003a }
            monitor-exit(r4)
            return r1
        L_0x000d:
            java.lang.String r0 = "\\."
            java.lang.String[] r0 = r5.split(r0)     // Catch:{ all -> 0x003a }
            java.lang.String r2 = r4.getParentObjectTreeFor(r5)     // Catch:{ all -> 0x003a }
            java.lang.Object r2 = r4.findObject(r2)     // Catch:{ all -> 0x003a }
            boolean r2 = r2 instanceof org.json.JSONObject     // Catch:{ all -> 0x003a }
            if (r2 == 0) goto L_0x0038
            java.lang.String r5 = r4.getParentObjectTreeFor(r5)     // Catch:{ all -> 0x003a }
            java.lang.Object r5 = r4.findObject(r5)     // Catch:{ all -> 0x003a }
            org.json.JSONObject r5 = (org.json.JSONObject) r5     // Catch:{ all -> 0x003a }
            if (r5 == 0) goto L_0x0038
            int r2 = r0.length     // Catch:{ all -> 0x003a }
            r3 = 1
            int r2 = r2 - r3
            r0 = r0[r2]     // Catch:{ all -> 0x003a }
            java.lang.Object r5 = r5.remove(r0)     // Catch:{ all -> 0x003a }
            if (r5 == 0) goto L_0x0038
            monitor-exit(r4)
            return r3
        L_0x0038:
            monitor-exit(r4)
            return r1
        L_0x003a:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.misc.JsonStorage.delete(java.lang.String):boolean");
    }

    private synchronized Object findObject(String str) {
        String[] split = str.split("\\.");
        JSONObject jSONObject = this._data;
        if (str.length() == 0) {
            return jSONObject;
        }
        int i = 0;
        while (i < split.length) {
            if (!jSONObject.has(split[i])) {
                return null;
            }
            try {
                jSONObject = jSONObject.getJSONObject(split[i]);
                i++;
            } catch (Exception e) {
                DeviceLog.exception("Couldn't read JSONObject: " + split[i], e);
                return null;
            }
        }
        return jSONObject;
    }

    private synchronized void createObjectTree(String str) {
        String[] split = str.split("\\.");
        JSONObject jSONObject = this._data;
        if (str.length() != 0) {
            for (int i = 0; i < split.length; i++) {
                if (!jSONObject.has(split[i])) {
                    try {
                        jSONObject = jSONObject.put(split[i], new JSONObject()).getJSONObject(split[i]);
                    } catch (Exception e) {
                        DeviceLog.exception("Couldn't create new JSONObject", e);
                    }
                } else {
                    try {
                        jSONObject = jSONObject.getJSONObject(split[i]);
                    } catch (Exception e2) {
                        DeviceLog.exception("Couldn't get existing JSONObject", e2);
                    }
                }
            }
        }
    }

    private synchronized String getParentObjectTreeFor(String str) {
        ArrayList arrayList;
        arrayList = new ArrayList(Arrays.asList(str.split("\\.")));
        arrayList.remove(arrayList.size() - 1);
        return TextUtils.join(".", arrayList.toArray());
    }
}
