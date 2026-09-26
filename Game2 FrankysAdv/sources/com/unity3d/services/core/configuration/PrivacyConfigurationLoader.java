package com.unity3d.services.core.configuration;

import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.request.WebRequest;
import org.json.JSONObject;

public class PrivacyConfigurationLoader implements IConfigurationLoader {
    private final IConfigurationLoader _configurationLoader;
    private final ConfigurationRequestFactory _configurationRequestFactory;
    /* access modifiers changed from: private */
    public final PrivacyConfigStorage _privacyConfigStorage;

    public PrivacyConfigurationLoader(IConfigurationLoader iConfigurationLoader, ConfigurationRequestFactory configurationRequestFactory, PrivacyConfigStorage privacyConfigStorage) {
        this._configurationLoader = iConfigurationLoader;
        this._configurationRequestFactory = configurationRequestFactory;
        this._privacyConfigStorage = privacyConfigStorage;
    }

    public void loadConfiguration(IConfigurationLoaderListener iConfigurationLoaderListener) throws Exception {
        if (this._privacyConfigStorage.getPrivacyConfig().getPrivacyStatus() == PrivacyConfigStatus.UNKNOWN) {
            load(new IPrivacyConfigurationListener() {
                public void onSuccess(PrivacyConfig privacyConfig) {
                    PrivacyConfigurationLoader.this._privacyConfigStorage.setPrivacyConfig(privacyConfig);
                }

                public void onError(String str) {
                    DeviceLog.warning("Couldn't fetch privacy configuration: " + str);
                    PrivacyConfigurationLoader.this._privacyConfigStorage.setPrivacyConfig(new PrivacyConfig());
                }
            });
        }
        this._configurationLoader.loadConfiguration(iConfigurationLoaderListener);
    }

    public Configuration getLocalConfiguration() {
        return this._configurationLoader.getLocalConfiguration();
    }

    private void load(IPrivacyConfigurationListener iPrivacyConfigurationListener) throws Exception {
        try {
            WebRequest webRequest = this._configurationRequestFactory.getWebRequest();
            InitializeEventsMetricSender.getInstance().didPrivacyConfigRequestStart();
            String makeRequest = webRequest.makeRequest();
            try {
                if (webRequest.getResponseCode() / 100 == 2) {
                    InitializeEventsMetricSender.getInstance().didPrivacyConfigRequestEnd(true);
                    iPrivacyConfigurationListener.onSuccess(new PrivacyConfig(new JSONObject(makeRequest)));
                    return;
                }
                InitializeEventsMetricSender.getInstance().didPrivacyConfigRequestEnd(false);
                iPrivacyConfigurationListener.onError("Privacy request failed with code: " + webRequest.getResponseCode());
            } catch (Exception unused) {
                InitializeEventsMetricSender.getInstance().didPrivacyConfigRequestEnd(false);
                iPrivacyConfigurationListener.onError("Could not create web request");
            }
        } catch (Exception e) {
            iPrivacyConfigurationListener.onError("Could not create web request: " + e);
        }
    }
}
