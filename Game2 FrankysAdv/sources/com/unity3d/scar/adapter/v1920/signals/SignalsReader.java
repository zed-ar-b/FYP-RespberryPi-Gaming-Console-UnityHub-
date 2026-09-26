package com.unity3d.scar.adapter.v1920.signals;

import android.content.Context;
import com.google.android.gms.ads.AdFormat;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.query.QueryInfo;
import com.unity3d.scar.adapter.common.DispatchGroup;
import com.unity3d.scar.adapter.common.signals.ISignalCollectionListener;
import com.unity3d.scar.adapter.common.signals.ISignalsReader;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class SignalsReader implements ISignalsReader {
    /* access modifiers changed from: private */
    public static SignalsStorage _signalsStorage;

    public SignalsReader(SignalsStorage signalsStorage) {
        _signalsStorage = signalsStorage;
    }

    public void getSCARSignals(Context context, String[] strArr, String[] strArr2, ISignalCollectionListener iSignalCollectionListener) {
        DispatchGroup dispatchGroup = new DispatchGroup();
        for (String sCARSignal : strArr) {
            dispatchGroup.enter();
            getSCARSignal(context, sCARSignal, AdFormat.INTERSTITIAL, dispatchGroup);
        }
        for (String sCARSignal2 : strArr2) {
            dispatchGroup.enter();
            getSCARSignal(context, sCARSignal2, AdFormat.REWARDED, dispatchGroup);
        }
        dispatchGroup.notify(new GMAScarDispatchCompleted(iSignalCollectionListener));
    }

    private void getSCARSignal(Context context, String str, AdFormat adFormat, DispatchGroup dispatchGroup) {
        AdRequest build = new AdRequest.Builder().build();
        QueryInfoMetadata queryInfoMetadata = new QueryInfoMetadata(str);
        QueryInfoCallback queryInfoCallback = new QueryInfoCallback(queryInfoMetadata, dispatchGroup);
        _signalsStorage.put(str, queryInfoMetadata);
        QueryInfo.generate(context, adFormat, build, queryInfoCallback);
    }

    private class GMAScarDispatchCompleted implements Runnable {
        private ISignalCollectionListener _signalListener;

        public GMAScarDispatchCompleted(ISignalCollectionListener iSignalCollectionListener) {
            this._signalListener = iSignalCollectionListener;
        }

        public void run() {
            HashMap hashMap = new HashMap();
            String str = null;
            for (Map.Entry<String, QueryInfoMetadata> value : SignalsReader._signalsStorage.getPlacementQueryInfoMap().entrySet()) {
                QueryInfoMetadata queryInfoMetadata = (QueryInfoMetadata) value.getValue();
                hashMap.put(queryInfoMetadata.getPlacementId(), queryInfoMetadata.getQueryStr());
                if (queryInfoMetadata.getError() != null) {
                    str = queryInfoMetadata.getError();
                }
            }
            if (hashMap.size() > 0) {
                this._signalListener.onSignalsCollected(new JSONObject(hashMap).toString());
            } else if (str == null) {
                this._signalListener.onSignalsCollected("");
            } else {
                this._signalListener.onSignalsCollectionFailed(str);
            }
        }
    }
}
