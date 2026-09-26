package com.unity3d.services.core.configuration;

public interface IPrivacyConfigurationListener {
    void onError(String str);

    void onSuccess(PrivacyConfig privacyConfig);
}
