package com.unity3d.scar.adapter.v1920.scarads;

import android.content.Context;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.query.AdInfo;
import com.unity3d.scar.adapter.common.GMAAdsError;
import com.unity3d.scar.adapter.common.IAdsErrorHandler;
import com.unity3d.scar.adapter.common.scarads.IScarAd;
import com.unity3d.scar.adapter.common.scarads.IScarLoadListener;
import com.unity3d.scar.adapter.common.scarads.ScarAdMetadata;
import com.unity3d.scar.adapter.v1920.signals.QueryInfoMetadata;

public abstract class ScarAdBase implements IScarAd {
    protected IAdsErrorHandler _adsErrorHandler;
    protected Context _context;
    protected QueryInfoMetadata _queryInfoMetadata;
    protected ScarAdMetadata _scarAdMetadata;

    /* access modifiers changed from: protected */
    public abstract void loadAdInternal(IScarLoadListener iScarLoadListener, AdRequest adRequest);

    public ScarAdBase(Context context, ScarAdMetadata scarAdMetadata, QueryInfoMetadata queryInfoMetadata, IAdsErrorHandler iAdsErrorHandler) {
        this._context = context;
        this._scarAdMetadata = scarAdMetadata;
        this._queryInfoMetadata = queryInfoMetadata;
        this._adsErrorHandler = iAdsErrorHandler;
    }

    public void loadAd(IScarLoadListener iScarLoadListener) {
        if (this._queryInfoMetadata != null) {
            loadAdInternal(iScarLoadListener, new AdRequest.Builder().setAdInfo(new AdInfo(this._queryInfoMetadata.getQueryInfo(), this._scarAdMetadata.getAdString())).build());
            return;
        }
        this._adsErrorHandler.handleError(GMAAdsError.QueryNotFoundError(this._scarAdMetadata));
    }
}
