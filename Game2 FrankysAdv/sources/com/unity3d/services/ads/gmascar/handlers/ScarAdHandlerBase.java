package com.unity3d.services.ads.gmascar.handlers;

import com.unity3d.scar.adapter.common.GMAEvent;
import com.unity3d.scar.adapter.common.IScarAdListenerWrapper;
import com.unity3d.scar.adapter.common.scarads.ScarAdMetadata;
import com.unity3d.services.ads.gmascar.utils.GMAEventSender;
import com.unity3d.services.core.misc.EventSubject;
import com.unity3d.services.core.misc.IEventListener;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;

public abstract class ScarAdHandlerBase implements IScarAdListenerWrapper {
    protected final EventSubject<GMAEvent> _eventSubject;
    protected final GMAEventSender _gmaEventSender = new GMAEventSender();
    protected final ScarAdMetadata _scarAdMetadata;

    public ScarAdHandlerBase(ScarAdMetadata scarAdMetadata, EventSubject<GMAEvent> eventSubject) {
        this._scarAdMetadata = scarAdMetadata;
        this._eventSubject = eventSubject;
    }

    public void onAdLoaded() {
        this._gmaEventSender.send(GMAEvent.AD_LOADED, this._scarAdMetadata.getPlacementId(), this._scarAdMetadata.getQueryId());
    }

    public void onAdFailedToLoad(int i, String str) {
        this._gmaEventSender.send(GMAEvent.LOAD_ERROR, this._scarAdMetadata.getPlacementId(), this._scarAdMetadata.getQueryId(), str, Integer.valueOf(i));
    }

    public void onAdOpened() {
        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.GMA, GMAEvent.AD_STARTED, new Object[0]);
        this._eventSubject.subscribe(new IEventListener<GMAEvent>() {
            public void onNextEvent(GMAEvent gMAEvent) {
                ScarAdHandlerBase.this._gmaEventSender.send(gMAEvent, new Object[0]);
            }
        });
    }

    public void onAdSkipped() {
        this._gmaEventSender.send(GMAEvent.AD_SKIPPED, new Object[0]);
    }

    public void onAdClosed() {
        this._gmaEventSender.send(GMAEvent.AD_CLOSED, new Object[0]);
        this._eventSubject.unsubscribe();
    }
}
