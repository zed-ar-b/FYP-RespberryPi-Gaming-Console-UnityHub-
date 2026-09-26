package com.unity3d.services.core.configuration;

public interface IConfigurationLoaderListener {
    void onError(String str);

    void onSuccess(Configuration configuration);
}
