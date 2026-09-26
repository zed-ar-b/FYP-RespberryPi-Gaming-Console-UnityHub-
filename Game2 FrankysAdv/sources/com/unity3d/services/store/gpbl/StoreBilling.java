package com.unity3d.services.store.gpbl;

import android.content.Context;
import com.unity3d.services.store.gpbl.bridges.SkuDetailsParamsBridge;
import com.unity3d.services.store.gpbl.bridges.billingclient.BillingClientBuilderFactory;
import com.unity3d.services.store.gpbl.bridges.billingclient.IBillingClient;
import com.unity3d.services.store.gpbl.proxies.BillingClientStateListenerProxy;
import com.unity3d.services.store.gpbl.proxies.PurchaseHistoryResponseListenerProxy;
import com.unity3d.services.store.gpbl.proxies.PurchaseUpdatedListenerProxy;
import com.unity3d.services.store.gpbl.proxies.PurchasesResponseListenerProxy;
import com.unity3d.services.store.gpbl.proxies.SkuDetailsResponseListenerProxy;
import com.unity3d.services.store.listeners.IPurchaseHistoryResponseListener;
import com.unity3d.services.store.listeners.IPurchaseUpdatedResponseListener;
import com.unity3d.services.store.listeners.IPurchasesResponseListener;
import com.unity3d.services.store.listeners.ISkuDetailsResponseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class StoreBilling {
    private final IBillingClient _billingClientBridge;

    public StoreBilling(Context context, IPurchaseUpdatedResponseListener iPurchaseUpdatedResponseListener) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this._billingClientBridge = BillingClientBuilderFactory.getBillingClientBuilder(context).setListener(new PurchaseUpdatedListenerProxy(iPurchaseUpdatedResponseListener)).enablePendingPurchases().build();
    }

    public void initialize(IBillingClientStateListener iBillingClientStateListener) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this._billingClientBridge.startConnection(new BillingClientStateListenerProxy(iBillingClientStateListener));
    }

    public int isFeatureSupported(String str) {
        boolean z;
        if (str.equals("inapp")) {
            z = this._billingClientBridge.isReady();
        } else {
            if (str.equals("subs")) {
                str = "subscriptions";
            }
            z = this._billingClientBridge.isFeatureSupported(str) == BillingResultResponseCode.OK;
        }
        if (z) {
            return 0;
        }
        return -1;
    }

    public void getPurchases(String str, IPurchasesResponseListener iPurchasesResponseListener) throws ClassNotFoundException {
        this._billingClientBridge.queryPurchasesAsync(str, new PurchasesResponseListenerProxy(iPurchasesResponseListener));
    }

    public void getSkuDetails(String str, ArrayList<String> arrayList, ISkuDetailsResponseListener iSkuDetailsResponseListener) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        this._billingClientBridge.querySkuDetailsAsync(SkuDetailsParamsBridge.newBuilder().setSkuList(arrayList).setType(str).build(), new SkuDetailsResponseListenerProxy(iSkuDetailsResponseListener));
    }

    public void getPurchaseHistory(String str, int i, IPurchaseHistoryResponseListener iPurchaseHistoryResponseListener) throws ClassNotFoundException {
        this._billingClientBridge.queryPurchaseHistoryAsync(str, new PurchaseHistoryResponseListenerProxy(iPurchaseHistoryResponseListener, i));
    }
}
