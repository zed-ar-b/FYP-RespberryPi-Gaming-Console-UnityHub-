package com.unity3d.services.store.gpbl;

import com.unity3d.services.store.gpbl.bridges.BillingResultBridge;

public interface IBillingClientStateListener {
    void onBillingServiceDisconnected();

    void onBillingSetupFinished(BillingResultBridge billingResultBridge);
}
