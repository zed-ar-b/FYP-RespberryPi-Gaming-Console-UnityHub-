package com.google.unity.ads;

import android.os.Bundle;
import com.google.android.gms.ads.mediation.MediationExtrasReceiver;
import java.util.HashMap;

public interface AdNetworkExtras {
    Bundle buildExtras(HashMap<String, String> hashMap);

    Class<? extends MediationExtrasReceiver> getAdapterClass();
}
