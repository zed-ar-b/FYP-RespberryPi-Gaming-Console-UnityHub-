package com.unity3d.services.core.configuration;

public interface IModuleConfiguration {
    Class[] getWebAppApiClassList();

    boolean initCompleteState(Configuration configuration);

    boolean initErrorState(Configuration configuration, ErrorState errorState, String str);

    boolean initModuleState(Configuration configuration);

    boolean resetState(Configuration configuration);
}
