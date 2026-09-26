package com.unity3d.services.ads.gmascar.bridges;

import android.content.Context;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.reflection.GenericBridge;
import java.util.HashMap;

public class MobileAdsBridge extends GenericBridge {
    private static final String initializationStatusMethodName = "getInitializationStatus";
    private static final String initializeMethodName = "initialize";
    private static final String versionStringMethodName = "getVersionString";

    public String getClassName() {
        return "com.google.android.gms.ads.MobileAds";
    }

    public MobileAdsBridge() {
        super(new HashMap<String, Class<?>[]>() {
            {
                try {
                    put(MobileAdsBridge.initializeMethodName, new Class[]{Context.class, Class.forName("com.google.android.gms.ads.initialization.OnInitializationCompleteListener")});
                } catch (ClassNotFoundException e) {
                    DeviceLog.debug("Could not find class \"com.google.android.gms.ads.initialization.OnInitializationCompleteListener\" %s", e.getLocalizedMessage());
                }
                put(MobileAdsBridge.initializationStatusMethodName, new Class[0]);
                put(MobileAdsBridge.versionStringMethodName, new Class[0]);
            }
        });
    }

    public void initialize(Context context, Object obj) {
        callVoidMethod(initializeMethodName, (Object) null, context, obj);
    }

    public String getVersionString() {
        Object callNonVoidMethod = callNonVoidMethod(versionStringMethodName, (Object) null, new Object[0]);
        if (callNonVoidMethod == null) {
            return "0.0.0";
        }
        return callNonVoidMethod.toString();
    }

    public Object getInitializationStatus() {
        return callNonVoidMethod(initializationStatusMethodName, (Object) null, new Object[0]);
    }
}
