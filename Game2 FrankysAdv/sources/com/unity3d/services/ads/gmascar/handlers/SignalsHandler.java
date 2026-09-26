package com.unity3d.services.ads.gmascar.handlers;

import com.unity3d.scar.adapter.common.GMAEvent;
import com.unity3d.scar.adapter.common.signals.ISignalCollectionListener;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;

public class SignalsHandler implements ISignalCollectionListener {
    public void onSignalsCollected(String str) {
        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.GMA, GMAEvent.SIGNALS, str);
    }

    public void onSignalsCollectionFailed(String str) {
        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.GMA, GMAEvent.SIGNALS_ERROR, str);
    }
}
