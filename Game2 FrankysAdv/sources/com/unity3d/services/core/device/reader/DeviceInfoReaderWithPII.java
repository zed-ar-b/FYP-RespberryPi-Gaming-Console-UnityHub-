package com.unity3d.services.core.device.reader;

import com.unity3d.services.core.device.reader.pii.PiiDataProvider;
import com.unity3d.services.core.device.reader.pii.PiiDataSelector;
import com.unity3d.services.core.device.reader.pii.PiiDecisionData;
import java.util.HashMap;
import java.util.Map;

public class DeviceInfoReaderWithPII implements IDeviceInfoReader {
    private IDeviceInfoReader _deviceInfoReader;
    private PiiDataProvider _piiDataProvider;
    private PiiDataSelector _piiDataSelector;

    public DeviceInfoReaderWithPII(IDeviceInfoReader iDeviceInfoReader, PiiDataSelector piiDataSelector, PiiDataProvider piiDataProvider) {
        this._deviceInfoReader = iDeviceInfoReader;
        this._piiDataSelector = piiDataSelector;
        this._piiDataProvider = piiDataProvider;
    }

    public Map<String, Object> getDeviceInfoData() {
        Map<String, Object> deviceInfoData = this._deviceInfoReader.getDeviceInfoData();
        PiiDecisionData whatToDoWithPII = this._piiDataSelector.whatToDoWithPII();
        int i = C03041.f345x6a9b5bad[whatToDoWithPII.getResultType().ordinal()];
        if (i == 1) {
            deviceInfoData.putAll(getPiiAttributesFromStorage(whatToDoWithPII));
        } else if (i == 2) {
            deviceInfoData.putAll(getPiiAttributesFromDevice(whatToDoWithPII));
        }
        return deviceInfoData;
    }

    /* renamed from: com.unity3d.services.core.device.reader.DeviceInfoReaderWithPII$1 */
    static /* synthetic */ class C03041 {

        /* renamed from: $SwitchMap$com$unity3d$services$core$device$reader$pii$DataSelectorResult */
        static final /* synthetic */ int[] f345x6a9b5bad;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.unity3d.services.core.device.reader.pii.DataSelectorResult[] r0 = com.unity3d.services.core.device.reader.pii.DataSelectorResult.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f345x6a9b5bad = r0
                com.unity3d.services.core.device.reader.pii.DataSelectorResult r1 = com.unity3d.services.core.device.reader.pii.DataSelectorResult.INCLUDE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f345x6a9b5bad     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.services.core.device.reader.pii.DataSelectorResult r1 = com.unity3d.services.core.device.reader.pii.DataSelectorResult.UPDATE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f345x6a9b5bad     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.unity3d.services.core.device.reader.pii.DataSelectorResult r1 = com.unity3d.services.core.device.reader.pii.DataSelectorResult.EXCLUDE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.device.reader.DeviceInfoReaderWithPII.C03041.<clinit>():void");
        }
    }

    private Map<String, Object> getPiiAttributesFromStorage(PiiDecisionData piiDecisionData) {
        return piiDecisionData.getAttributes();
    }

    private Map<String, Object> getPiiAttributesFromDevice(PiiDecisionData piiDecisionData) {
        HashMap hashMap = new HashMap();
        String advertisingTrackingId = this._piiDataProvider.getAdvertisingTrackingId();
        if (advertisingTrackingId != null) {
            hashMap.put(JsonStorageKeyNames.ADVERTISING_TRACKING_ID_NORMALIZED_KEY, advertisingTrackingId);
        }
        if (piiDecisionData.getUserNonBehavioralFlag() != null) {
            hashMap.put(JsonStorageKeyNames.USER_NON_BEHAVIORAL_KEY, piiDecisionData.getUserNonBehavioralFlag());
        }
        return hashMap;
    }
}
