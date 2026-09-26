package com.unity3d.services.ads.gmascar.bridges;

import com.unity3d.services.core.reflection.GenericBridge;
import java.util.HashMap;
import java.util.Map;

public class InitializationStatusBridge extends GenericBridge {
    private static final String adapterStatusMapMethodName = "getAdapterStatusMap";

    public String getClassName() {
        return "com.google.android.gms.ads.initialization.InitializationStatus";
    }

    public InitializationStatusBridge() {
        super(new HashMap<String, Class<?>[]>() {
            {
                put(InitializationStatusBridge.adapterStatusMapMethodName, new Class[0]);
            }
        });
    }

    public Map<String, Object> getAdapterStatusMap(Object obj) {
        return (Map) callNonVoidMethod(adapterStatusMapMethodName, obj, new Object[0]);
    }
}
