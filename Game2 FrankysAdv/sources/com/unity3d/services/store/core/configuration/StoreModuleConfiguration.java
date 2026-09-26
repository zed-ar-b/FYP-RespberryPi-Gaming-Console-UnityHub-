package com.unity3d.services.store.core.configuration;

import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.configuration.ErrorState;
import com.unity3d.services.core.configuration.IModuleConfiguration;
import com.unity3d.services.store.core.api.Store;

public class StoreModuleConfiguration implements IModuleConfiguration {
    private static final Class[] WEB_APP_API_CLASS_LIST = {Store.class};

    public boolean initCompleteState(Configuration configuration) {
        return true;
    }

    public boolean initErrorState(Configuration configuration, ErrorState errorState, String str) {
        return true;
    }

    public boolean initModuleState(Configuration configuration) {
        return true;
    }

    public boolean resetState(Configuration configuration) {
        return true;
    }

    public Class[] getWebAppApiClassList() {
        return WEB_APP_API_CLASS_LIST;
    }
}
