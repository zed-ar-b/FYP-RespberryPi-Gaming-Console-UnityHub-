package com.unity3d.services.core.device.reader;

import com.unity3d.services.core.configuration.PrivacyConfigStorage;
import com.unity3d.services.core.device.reader.pii.PiiDataProvider;
import com.unity3d.services.core.device.reader.pii.PiiTrackingStatusReader;
import java.util.HashMap;
import java.util.Map;

public class DeviceInfoReaderWithPrivacy implements IDeviceInfoReader {
    private final IDeviceInfoReader _deviceInfoReader;
    private final PiiDataProvider _piiDataProvider;
    private final PiiTrackingStatusReader _piiTrackingStatusReader;
    private final PrivacyConfigStorage _privacyConfigStorage;

    public DeviceInfoReaderWithPrivacy(IDeviceInfoReader iDeviceInfoReader, PrivacyConfigStorage privacyConfigStorage, PiiDataProvider piiDataProvider, PiiTrackingStatusReader piiTrackingStatusReader) {
        this._deviceInfoReader = iDeviceInfoReader;
        this._privacyConfigStorage = privacyConfigStorage;
        this._piiDataProvider = piiDataProvider;
        this._piiTrackingStatusReader = piiTrackingStatusReader;
    }

    public Map<String, Object> getDeviceInfoData() {
        Map<String, Object> deviceInfoData = this._deviceInfoReader.getDeviceInfoData();
        PrivacyConfigStorage privacyConfigStorage = this._privacyConfigStorage;
        if (!(privacyConfigStorage == null || privacyConfigStorage.getPrivacyConfig() == null || !this._privacyConfigStorage.getPrivacyConfig().allowedToSendPii())) {
            deviceInfoData.putAll(getPiiAttributesFromDevice());
        }
        return deviceInfoData;
    }

    private Map<String, Object> getPiiAttributesFromDevice() {
        HashMap hashMap = new HashMap();
        String advertisingTrackingId = this._piiDataProvider.getAdvertisingTrackingId();
        if (advertisingTrackingId != null) {
            hashMap.put(JsonStorageKeyNames.ADVERTISING_TRACKING_ID_NORMALIZED_KEY, advertisingTrackingId);
        }
        hashMap.put(JsonStorageKeyNames.USER_NON_BEHAVIORAL_KEY, Boolean.valueOf(this._piiTrackingStatusReader.getUserNonBehavioralFlag()));
        return hashMap;
    }
}
