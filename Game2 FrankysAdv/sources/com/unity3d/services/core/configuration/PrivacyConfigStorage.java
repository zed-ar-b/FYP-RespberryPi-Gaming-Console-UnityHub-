package com.unity3d.services.core.configuration;

import com.unity3d.services.core.misc.IObserver;
import com.unity3d.services.core.misc.Observable;

public class PrivacyConfigStorage extends Observable<PrivacyConfig> {
    private static PrivacyConfigStorage _instance;
    private PrivacyConfig _privacyConfig = new PrivacyConfig();

    private PrivacyConfigStorage() {
    }

    public static PrivacyConfigStorage getInstance() {
        if (_instance == null) {
            _instance = new PrivacyConfigStorage();
        }
        return _instance;
    }

    public synchronized PrivacyConfig getPrivacyConfig() {
        return this._privacyConfig;
    }

    public synchronized void registerObserver(IObserver<PrivacyConfig> iObserver) {
        super.registerObserver(iObserver);
        if (this._privacyConfig.getPrivacyStatus() != PrivacyConfigStatus.UNKNOWN) {
            iObserver.updated(this._privacyConfig);
        }
    }

    public synchronized void setPrivacyConfig(PrivacyConfig privacyConfig) {
        this._privacyConfig = privacyConfig;
        notifyObservers(privacyConfig);
    }
}
