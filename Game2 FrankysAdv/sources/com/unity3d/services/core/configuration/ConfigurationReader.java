package com.unity3d.services.core.configuration;

import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.properties.SdkProperties;
import com.unity3d.services.core.webview.WebViewApp;
import java.io.File;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationReader {
    private Configuration _localConfiguration;

    public Configuration getCurrentConfiguration() {
        if (getRemoteConfiguration() != null) {
            return getRemoteConfiguration();
        }
        Configuration localConfiguration = getLocalConfiguration();
        return localConfiguration != null ? localConfiguration : new Configuration();
    }

    private Configuration getRemoteConfiguration() {
        if (WebViewApp.getCurrentApp() == null) {
            return null;
        }
        return WebViewApp.getCurrentApp().getConfiguration();
    }

    private Configuration getLocalConfiguration() {
        Configuration configuration = this._localConfiguration;
        if (configuration != null) {
            return configuration;
        }
        File file = new File(SdkProperties.getLocalConfigurationFilepath());
        if (file.exists()) {
            try {
                this._localConfiguration = new Configuration(new JSONObject(new String(Utilities.readFileBytes(file))));
            } catch (IOException | JSONException unused) {
                DeviceLog.debug("Unable to read configuration from storage");
                this._localConfiguration = null;
            }
        }
        return this._localConfiguration;
    }
}
