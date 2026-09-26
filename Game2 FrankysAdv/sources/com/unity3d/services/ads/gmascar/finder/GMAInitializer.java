package com.unity3d.services.ads.gmascar.finder;

import com.unity3d.scar.adapter.common.GMAEvent;
import com.unity3d.services.ads.gmascar.bridges.AdapterStatusBridge;
import com.unity3d.services.ads.gmascar.bridges.InitializationStatusBridge;
import com.unity3d.services.ads.gmascar.bridges.InitializeListenerBridge;
import com.unity3d.services.ads.gmascar.bridges.MobileAdsBridge;
import com.unity3d.services.ads.gmascar.utils.GMAEventSender;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;

public class GMAInitializer {
    private AdapterStatusBridge _adapterStatusBridge;
    private GMAEventSender _gmaEventSender = new GMAEventSender();
    private InitializeListenerBridge _initializationListenerBridge;
    private InitializationStatusBridge _initializationStatusBridge;
    private MobileAdsBridge _mobileAdsBridge;

    public GMAInitializer(MobileAdsBridge mobileAdsBridge, InitializeListenerBridge initializeListenerBridge, InitializationStatusBridge initializationStatusBridge, AdapterStatusBridge adapterStatusBridge) {
        this._mobileAdsBridge = mobileAdsBridge;
        this._initializationListenerBridge = initializeListenerBridge;
        this._initializationStatusBridge = initializationStatusBridge;
        this._adapterStatusBridge = adapterStatusBridge;
    }

    public void initializeGMA() {
        if (isInitialized()) {
            this._gmaEventSender.send(GMAEvent.ALREADY_INITIALIZED, new Object[0]);
        } else {
            this._mobileAdsBridge.initialize(ClientProperties.getApplicationContext(), this._initializationListenerBridge.createInitializeListenerProxy());
        }
    }

    public boolean initSuccessful(Object obj) {
        Object obj2 = this._initializationStatusBridge.getAdapterStatusMap(obj).get(this._mobileAdsBridge.getClassName());
        if (obj2 != null) {
            if (this._adapterStatusBridge.isGMAInitialized(obj2)) {
                WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.GMA, GMAEvent.INIT_SUCCESS, new Object[0]);
                return true;
            }
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.GMA, GMAEvent.INIT_ERROR, new Object[0]);
        }
        return false;
    }

    public boolean isInitialized() {
        try {
            return initSuccessful(this._mobileAdsBridge.getInitializationStatus());
        } catch (Exception e) {
            DeviceLog.debug("ERROR: Could not get initialization status of GMA SDK - %s", e.getLocalizedMessage());
        } catch (Throwable unused) {
        }
        return false;
    }

    public InitializeListenerBridge getInitializeListenerBridge() {
        return this._initializationListenerBridge;
    }
}
