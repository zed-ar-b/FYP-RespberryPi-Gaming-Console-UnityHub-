package com.unity3d.services.store.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.store.StoreEvent;
import com.unity3d.services.store.gpbl.StoreBilling;
import com.unity3d.services.store.listeners.PurchasesResponseListener;
import java.util.ArrayList;
import java.util.Iterator;

public class StoreLifecycleListener implements Application.ActivityLifecycleCallbacks {
    private final PurchasesResponseListener _purchaseResponseListener = new PurchasesResponseListener(StoreEvent.PURCHASES_ON_RESUME_RESULT, StoreEvent.PURCHASES_ON_RESUME_ERROR);
    private final ArrayList<String> _purchaseTypes;
    private final StoreBilling _storeBilling;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public StoreLifecycleListener(ArrayList<String> arrayList, StoreBilling storeBilling) {
        this._purchaseTypes = arrayList;
        this._storeBilling = storeBilling;
    }

    public void onActivityResumed(Activity activity) {
        try {
            Iterator<String> it = this._purchaseTypes.iterator();
            while (it.hasNext()) {
                this._storeBilling.getPurchases(it.next(), this._purchaseResponseListener);
            }
        } catch (ClassNotFoundException e) {
            DeviceLog.warning("Couldn't fetch purchases onActivityResumed. " + e.getMessage());
        }
    }
}
