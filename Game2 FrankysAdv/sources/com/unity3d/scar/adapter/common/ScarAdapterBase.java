package com.unity3d.scar.adapter.common;

import android.app.Activity;
import android.content.Context;
import com.unity3d.scar.adapter.common.scarads.IScarAd;
import com.unity3d.scar.adapter.common.signals.ISignalCollectionListener;
import com.unity3d.scar.adapter.common.signals.ISignalsReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ScarAdapterBase implements IScarAdapter {
    protected IAdsErrorHandler _adsErrorHandler;
    protected IScarAd _currentAdReference;
    protected Map<String, IScarAd> _loadedAds = new ConcurrentHashMap();
    protected ISignalsReader _scarSignalReader;

    public ScarAdapterBase(IAdsErrorHandler iAdsErrorHandler) {
        this._adsErrorHandler = iAdsErrorHandler;
    }

    public void getSCARSignals(Context context, String[] strArr, String[] strArr2, ISignalCollectionListener iSignalCollectionListener) {
        this._scarSignalReader.getSCARSignals(context, strArr, strArr2, iSignalCollectionListener);
    }

    public void show(final Activity activity, String str, String str2) {
        IScarAd iScarAd = this._loadedAds.get(str2);
        if (iScarAd == null) {
            IAdsErrorHandler iAdsErrorHandler = this._adsErrorHandler;
            iAdsErrorHandler.handleError(GMAAdsError.NoAdsError(str2, str, "Could not find ad for placement '" + str2 + "'."));
            return;
        }
        this._currentAdReference = iScarAd;
        Utils.runOnUiThread(new Runnable() {
            public void run() {
                ScarAdapterBase.this._currentAdReference.show(activity);
            }
        });
    }
}
