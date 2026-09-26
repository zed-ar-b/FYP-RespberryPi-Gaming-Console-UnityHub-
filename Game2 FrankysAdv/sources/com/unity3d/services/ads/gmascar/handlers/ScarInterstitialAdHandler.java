package com.unity3d.services.ads.gmascar.handlers;

import com.unity3d.scar.adapter.common.GMAEvent;
import com.unity3d.scar.adapter.common.IScarInterstitialAdListenerWrapper;
import com.unity3d.scar.adapter.common.scarads.ScarAdMetadata;
import com.unity3d.services.core.misc.EventSubject;

public class ScarInterstitialAdHandler extends ScarAdHandlerBase implements IScarInterstitialAdListenerWrapper {
    public ScarInterstitialAdHandler(ScarAdMetadata scarAdMetadata, EventSubject<GMAEvent> eventSubject) {
        super(scarAdMetadata, eventSubject);
    }

    public void onAdFailedToShow(int i, String str) {
        this._gmaEventSender.send(GMAEvent.INTERSTITIAL_SHOW_ERROR, this._scarAdMetadata.getPlacementId(), this._scarAdMetadata.getQueryId(), str, Integer.valueOf(i));
    }

    public void onAdClicked() {
        this._gmaEventSender.send(GMAEvent.AD_CLICKED, new Object[0]);
    }

    public void onAdClosed() {
        if (!this._eventSubject.eventQueueIsEmpty()) {
            super.onAdSkipped();
        }
        super.onAdClosed();
    }

    public void onAdLeftApplication() {
        this._gmaEventSender.send(GMAEvent.AD_LEFT_APPLICATION, new Object[0]);
    }

    public void onAdImpression() {
        this._gmaEventSender.send(GMAEvent.INTERSTITIAL_IMPRESSION_RECORDED, new Object[0]);
    }
}
