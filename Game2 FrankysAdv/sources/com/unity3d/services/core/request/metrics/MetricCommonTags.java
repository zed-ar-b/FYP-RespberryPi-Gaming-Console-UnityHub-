package com.unity3d.services.core.request.metrics;

import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.configuration.PrivacyConfig;
import com.unity3d.services.core.configuration.PrivacyConfigStorage;
import com.unity3d.services.core.device.Device;
import com.unity3d.services.core.device.Storage;
import com.unity3d.services.core.device.StorageManager;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.IObserver;
import com.unity3d.services.core.properties.SdkProperties;
import java.util.HashMap;
import java.util.Map;

public class MetricCommonTags {
    private static final String METRIC_COMMON_TAG_CONFIG_SOURCE = "src";
    private static final String METRIC_COMMON_TAG_COUNTRY_ISO = "iso";
    private static final String METRIC_COMMON_TAG_MEDIATION_ADAPTER = "m_ad_ver";
    private static final String METRIC_COMMON_TAG_MEDIATION_NAME = "m_name";
    private static final String METRIC_COMMON_TAG_MEDIATION_VERSION = "m_ver";
    private static final String METRIC_COMMON_TAG_PLATFORM = "plt";
    public static final String METRIC_COMMON_TAG_PLATFORM_ANDROID = "android";
    private static final String METRIC_COMMON_TAG_PRIVACY_MODE = "prvc";
    private static final String METRIC_COMMON_TAG_SDK_VERSION = "sdk";
    private static final String METRIC_COMMON_TAG_SYSTEM_VERSION = "system";
    private static final String METRIC_COMMON_TAG_TEST_MODE = "tm";
    private String _configSrc;
    private final String _countryISO = Device.getNetworkCountryISO();
    private Map<String, String> _experiments;
    private String _mediationAdapter;
    private String _mediationName;
    private String _mediationVersion;
    private final String _platform = METRIC_COMMON_TAG_PLATFORM_ANDROID;
    /* access modifiers changed from: private */
    public String _privacyMode = PrivacyConfigStorage.getInstance().getPrivacyConfig().getPrivacyStatus().toLowerCase();
    private final IObserver<PrivacyConfig> _privacyObserver = new IObserver<PrivacyConfig>() {
        public void updated(PrivacyConfig privacyConfig) {
            String unused = MetricCommonTags.this._privacyMode = privacyConfig.getPrivacyStatus().toString().toLowerCase();
        }
    };
    private final String _sdkVersion = SdkProperties.getVersionName();
    private final String _systemVersion = Device.getOsVersion();
    private final boolean _testMode = SdkProperties.isTestMode();

    public MetricCommonTags() {
        PrivacyConfigStorage.getInstance().registerObserver(this._privacyObserver);
    }

    public void updateWithConfig(Configuration configuration) {
        this._configSrc = configuration.getSrc();
        if (configuration.getExperiments() != null) {
            this._experiments = configuration.getExperiments().getExperimentTags();
        }
    }

    public Map<String, String> asMap() {
        if (mediationIsEmpty()) {
            refreshMediationData();
        }
        HashMap hashMap = new HashMap();
        String str = this._countryISO;
        if (str != null) {
            hashMap.put(METRIC_COMMON_TAG_COUNTRY_ISO, str);
        }
        String str2 = this._platform;
        if (str2 != null) {
            hashMap.put(METRIC_COMMON_TAG_PLATFORM, str2);
        }
        if (this._countryISO != null) {
            hashMap.put(METRIC_COMMON_TAG_SDK_VERSION, this._sdkVersion);
        }
        String str3 = this._systemVersion;
        if (str3 != null) {
            hashMap.put(METRIC_COMMON_TAG_SYSTEM_VERSION, str3);
        }
        String str4 = this._privacyMode;
        if (str4 != null) {
            hashMap.put(METRIC_COMMON_TAG_PRIVACY_MODE, str4);
        }
        String str5 = this._configSrc;
        if (str5 != null) {
            hashMap.put(METRIC_COMMON_TAG_CONFIG_SOURCE, str5);
        }
        String str6 = this._mediationName;
        if (str6 != null) {
            hashMap.put(METRIC_COMMON_TAG_MEDIATION_NAME, str6);
        }
        String str7 = this._mediationVersion;
        if (str7 != null) {
            hashMap.put(METRIC_COMMON_TAG_MEDIATION_VERSION, str7);
        }
        String str8 = this._mediationAdapter;
        if (str8 != null) {
            hashMap.put(METRIC_COMMON_TAG_MEDIATION_ADAPTER, str8);
        }
        Map<String, String> map = this._experiments;
        if (map != null) {
            hashMap.putAll(map);
        }
        hashMap.put(METRIC_COMMON_TAG_TEST_MODE, String.valueOf(this._testMode));
        return hashMap;
    }

    public void shutdown() {
        PrivacyConfigStorage.getInstance().unregisterObserver(this._privacyObserver);
    }

    private void refreshMediationData() {
        try {
            Storage storage = StorageManager.getStorage(StorageManager.StorageType.PUBLIC);
            if (storage != null && storage.initStorage()) {
                this._mediationName = (String) storage.get("mediation.name.value");
                this._mediationVersion = (String) storage.get("mediation.version.value");
                this._mediationAdapter = (String) storage.get("mediation.adapter_version.value");
            }
        } catch (Exception e) {
            DeviceLog.debug("Failed to refreshMediationData: %s", e.getLocalizedMessage());
        }
    }

    private boolean mediationIsEmpty() {
        String str = this._mediationName;
        return str == null || str.isEmpty();
    }
}
