package com.unity3d.ads.metadata;

import android.content.Context;

public class MediationMetaData extends MetaData {
    public static final String KEY_MISSED_IMPRESSION_ORDINAL = "missedImpressionOrdinal";
    public static final String KEY_NAME = "name";
    public static final String KEY_ORDINAL = "ordinal";
    public static final String KEY_VERSION = "version";

    public MediationMetaData(Context context) {
        super(context);
        setCategory("mediation");
    }

    public void setOrdinal(int i) {
        set(KEY_ORDINAL, (Object) Integer.valueOf(i));
    }

    public void setMissedImpressionOrdinal(int i) {
        set(KEY_MISSED_IMPRESSION_ORDINAL, (Object) Integer.valueOf(i));
    }

    public void setName(String str) {
        set(KEY_NAME, (Object) str);
    }

    public void setVersion(String str) {
        set(KEY_VERSION, (Object) str);
    }
}
