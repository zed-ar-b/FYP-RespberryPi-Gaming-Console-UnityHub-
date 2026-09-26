package com.unity3d.scar.adapter.v2000.scarads;

import android.content.Context;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.query.AdInfo;
import com.unity3d.scar.adapter.common.GMAAdsError;
import com.unity3d.scar.adapter.common.IAdsErrorHandler;
import com.unity3d.scar.adapter.common.scarads.IScarAd;
import com.unity3d.scar.adapter.common.scarads.IScarLoadListener;
import com.unity3d.scar.adapter.common.scarads.ScarAdMetadata;
import com.unity3d.scar.adapter.v2000.signals.QueryInfoMetadata;

public abstract class ScarAdBase<T> implements IScarAd {
    protected T _adObj;
    protected IAdsErrorHandler _adsErrorHandler;
    protected Context _context;
    protected QueryInfoMetadata _queryInfoMetadata;
    protected ScarAdListener _scarAdListener;
    protected ScarAdMetadata _scarAdMetadata;

    /* access modifiers changed from: protected */
    public abstract void loadAdInternal(AdRequest adRequest, IScarLoadListener iScarLoadListener);

    public ScarAdBase(Context context, ScarAdMetadata scarAdMetadata, QueryInfoMetadata queryInfoMetadata, IAdsErrorHandler iAdsErrorHandler) {
        this._context = context;
        this._scarAdMetadata = scarAdMetadata;
        this._queryInfoMetadata = queryInfoMetadata;
        this._adsErrorHandler = iAdsErrorHandler;
    }

    public void setGmaAd(T t) {
        this._adObj = t;
    }

    public void loadAd(IScarLoadListener iScarLoadListener) {
        if (this._queryInfoMetadata != null) {
            AdRequest build = new AdRequest.Builder().setAdInfo(new AdInfo(this._queryInfoMetadata.getQueryInfo(), this._scarAdMetadata.getAdString())).build();
            this._scarAdListener.setLoadListener(iScarLoadListener);
            loadAdInternal(build, iScarLoadListener);
            return;
        }
        this._adsErrorHandler.handleError(GMAAdsError.QueryNotFoundError(this._scarAdMetadata));
    }
}
