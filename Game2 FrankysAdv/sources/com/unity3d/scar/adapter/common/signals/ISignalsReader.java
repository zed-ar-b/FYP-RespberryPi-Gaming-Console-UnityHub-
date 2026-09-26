package com.unity3d.scar.adapter.common.signals;

import android.content.Context;

public interface ISignalsReader {
    void getSCARSignals(Context context, String[] strArr, String[] strArr2, ISignalCollectionListener iSignalCollectionListener);
}
