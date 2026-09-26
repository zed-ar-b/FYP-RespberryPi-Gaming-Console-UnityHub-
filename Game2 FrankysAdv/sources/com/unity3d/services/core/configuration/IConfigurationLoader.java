package com.unity3d.services.core.configuration;

public interface IConfigurationLoader {
    Configuration getLocalConfiguration();

    void loadConfiguration(IConfigurationLoaderListener iConfigurationLoaderListener) throws Exception;
}
