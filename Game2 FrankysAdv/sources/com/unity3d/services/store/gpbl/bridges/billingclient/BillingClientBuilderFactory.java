package com.unity3d.services.store.gpbl.bridges.billingclient;

import android.content.Context;
import com.unity3d.services.store.gpbl.bridges.billingclient.p001v4.BillingClientBridge;
import java.lang.reflect.InvocationTargetException;

public class BillingClientBuilderFactory {
    public static IBillingClientBuilderBridge getBillingClientBuilder(Context context) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (BillingClientBridge.isAvailable()) {
            return BillingClientBridge.newBuilder(context);
        }
        return com.unity3d.services.store.gpbl.bridges.billingclient.p000v3.BillingClientBridge.newBuilder(context);
    }
}
