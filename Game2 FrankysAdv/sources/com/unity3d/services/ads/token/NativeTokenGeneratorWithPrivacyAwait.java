package com.unity3d.services.ads.token;

import android.os.ConditionVariable;
import com.unity3d.services.core.configuration.PrivacyConfig;
import com.unity3d.services.core.configuration.PrivacyConfigStorage;
import com.unity3d.services.core.misc.IObserver;
import java.util.concurrent.ExecutorService;

public class NativeTokenGeneratorWithPrivacyAwait implements INativeTokenGenerator {
    private final ExecutorService _executorService;
    /* access modifiers changed from: private */
    public final INativeTokenGenerator _nativeTokenGenerator;
    /* access modifiers changed from: private */
    public final ConditionVariable _privacyAwait = new ConditionVariable();
    /* access modifiers changed from: private */
    public final int _privacyAwaitTimeout;

    public NativeTokenGeneratorWithPrivacyAwait(ExecutorService executorService, INativeTokenGenerator iNativeTokenGenerator, int i) {
        this._executorService = executorService;
        this._nativeTokenGenerator = iNativeTokenGenerator;
        this._privacyAwaitTimeout = i;
    }

    public void generateToken(final INativeTokenGeneratorListener iNativeTokenGeneratorListener) {
        final C02481 r0 = new IObserver<PrivacyConfig>() {
            public void updated(PrivacyConfig privacyConfig) {
                NativeTokenGeneratorWithPrivacyAwait.this._privacyAwait.open();
            }
        };
        PrivacyConfigStorage.getInstance().registerObserver(r0);
        this._executorService.execute(new Runnable() {
            public void run() {
                NativeTokenGeneratorWithPrivacyAwait.this._privacyAwait.block((long) NativeTokenGeneratorWithPrivacyAwait.this._privacyAwaitTimeout);
                PrivacyConfigStorage.getInstance().unregisterObserver(r0);
                NativeTokenGeneratorWithPrivacyAwait.this._nativeTokenGenerator.generateToken(iNativeTokenGeneratorListener);
            }
        });
    }
}
