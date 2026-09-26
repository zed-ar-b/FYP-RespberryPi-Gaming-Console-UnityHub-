package com.unity3d.services.core.webview.bridge;

import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.webview.WebViewApp;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class NativeCallback {
    private static AtomicInteger _callbackCount = new AtomicInteger(0);
    private Method _callback;
    private String _id = (this._callback.getName().toUpperCase(Locale.US) + "_" + _callbackCount.getAndIncrement());

    public NativeCallback(Method method) {
        this._callback = method;
    }

    public String getId() {
        return this._id;
    }

    public void invoke(String str, Object... objArr) throws InvocationTargetException, IllegalAccessException, IllegalArgumentException {
        Object[] objArr2;
        try {
            CallbackStatus valueOf = CallbackStatus.valueOf(str);
            if (objArr == null) {
                objArr2 = new Object[]{valueOf};
            } else {
                ArrayList arrayList = new ArrayList(Arrays.asList(objArr));
                arrayList.add(0, valueOf);
                objArr2 = arrayList.toArray();
            }
            this._callback.invoke((Object) null, objArr2);
            WebViewApp.getCurrentApp().removeCallback(this);
        } catch (Exception e) {
            DeviceLog.error("Illegal status");
            WebViewApp.getCurrentApp().removeCallback(this);
            throw e;
        }
    }
}
