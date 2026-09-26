package com.unity3d.scar.adapter.common.signals;

public interface ISignalCollectionListener {
    void onSignalsCollected(String str);

    void onSignalsCollectionFailed(String str);
}
