package com.unity3d.services.core.device.reader.pii;

import com.unity3d.services.core.device.reader.JsonStorageKeyNames;
import com.unity3d.services.core.misc.IJsonStorageReader;

public class PiiTrackingStatusReader {
    private final IJsonStorageReader _jsonStorageReader;

    public PiiTrackingStatusReader(IJsonStorageReader iJsonStorageReader) {
        this._jsonStorageReader = iJsonStorageReader;
    }

    public PiiPrivacyMode getPrivacyMode() {
        if (getUserPrivacyMode() == PiiPrivacyMode.NULL && getSpmPrivacyMode() == PiiPrivacyMode.NULL) {
            return PiiPrivacyMode.NULL;
        }
        if (getUserPrivacyMode() == PiiPrivacyMode.APP || getSpmPrivacyMode() == PiiPrivacyMode.APP) {
            return PiiPrivacyMode.APP;
        }
        if (getUserPrivacyMode() == PiiPrivacyMode.MIXED || getSpmPrivacyMode() == PiiPrivacyMode.MIXED) {
            return PiiPrivacyMode.MIXED;
        }
        if (getUserPrivacyMode() == PiiPrivacyMode.NONE || getSpmPrivacyMode() == PiiPrivacyMode.NONE) {
            return PiiPrivacyMode.NONE;
        }
        return PiiPrivacyMode.UNDEFINED;
    }

    public boolean getUserNonBehavioralFlag() {
        IJsonStorageReader iJsonStorageReader = this._jsonStorageReader;
        if (iJsonStorageReader != null) {
            Object obj = iJsonStorageReader.get(JsonStorageKeyNames.USER_NON_BEHAVIORAL_VALUE_KEY);
            if (obj == null) {
                obj = this._jsonStorageReader.get(JsonStorageKeyNames.USER_NON_BEHAVIORAL_VALUE_ALT_KEY);
            }
            if (obj instanceof String) {
                return Boolean.parseBoolean((String) obj);
            }
            if (obj instanceof Boolean) {
                return ((Boolean) obj).booleanValue();
            }
        }
        return false;
    }

    private PiiPrivacyMode getUserPrivacyMode() {
        return getPrivacyMode(JsonStorageKeyNames.PRIVACY_MODE_KEY);
    }

    private PiiPrivacyMode getSpmPrivacyMode() {
        return getPrivacyMode(JsonStorageKeyNames.PRIVACY_SPM_KEY);
    }

    private PiiPrivacyMode getPrivacyMode(String str) {
        String str2;
        IJsonStorageReader iJsonStorageReader = this._jsonStorageReader;
        if (iJsonStorageReader != null) {
            Object obj = iJsonStorageReader.get(str);
            if (obj instanceof String) {
                str2 = (String) obj;
                return PiiPrivacyMode.getPiiPrivacyMode(str2);
            }
        }
        str2 = null;
        return PiiPrivacyMode.getPiiPrivacyMode(str2);
    }
}
