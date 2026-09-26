package com.unity3d.services.core.misc;

import com.unity3d.services.core.log.DeviceLog;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonStorageAggregator implements IJsonStorageReader {
    private final List<IJsonStorageReader> _jsonStorageReaders;

    public JsonStorageAggregator(List<IJsonStorageReader> list) {
        this._jsonStorageReaders = list;
    }

    public JSONObject getData() {
        JSONObject jSONObject = new JSONObject();
        for (IJsonStorageReader next : this._jsonStorageReaders) {
            if (next != null) {
                try {
                    jSONObject = Utilities.mergeJsonObjects(jSONObject, next.getData());
                } catch (JSONException unused) {
                    DeviceLog.error("Failed to merge storage: " + next);
                }
            }
        }
        return jSONObject;
    }

    public Object get(String str) {
        Iterator<IJsonStorageReader> it = this._jsonStorageReaders.iterator();
        Object obj = null;
        while (it.hasNext() && ((r2 = it.next()) == null || (obj = r2.get(str)) == null)) {
        }
        return obj;
    }
}
