package com.unity3d.services.ads.token;

import android.os.Handler;
import android.os.Looper;
import com.unity3d.ads.IUnityAdsTokenListener;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.configuration.ConfigurationReader;
import com.unity3d.services.core.configuration.PrivacyConfigStorage;
import com.unity3d.services.core.device.TokenType;
import com.unity3d.services.core.device.reader.DeviceInfoReaderBuilder;
import com.unity3d.services.core.device.reader.GameSessionIdReader;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.properties.InitializationStatusReader;
import com.unity3d.services.core.properties.SdkProperties;
import com.unity3d.services.core.request.metrics.AdOperationMetric;
import com.unity3d.services.core.request.metrics.ISDKMetrics;
import com.unity3d.services.core.request.metrics.SDKMetrics;
import com.unity3d.services.core.request.metrics.TSIMetric;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTokenStorage {
    private static AsyncTokenStorage _instance;
    private Configuration _configuration = new Configuration();
    private boolean _configurationWasSet = false;
    /* access modifiers changed from: private */
    public final Handler _handler;
    private final InitializationStatusReader _initStatusReader = new InitializationStatusReader();
    private INativeTokenGenerator _nativeTokenGenerator;
    private final ISDKMetrics _sdkMetrics;
    private boolean _tokenAvailable = false;
    private final List<TokenListenerState> _tokenListeners = new LinkedList();

    public static AsyncTokenStorage getInstance() {
        if (_instance == null) {
            _instance = new AsyncTokenStorage((INativeTokenGenerator) null, new Handler(Looper.getMainLooper()), SDKMetrics.getInstance());
        }
        return _instance;
    }

    class TokenListenerState {
        public boolean invoked;
        public IUnityAdsTokenListener listener;
        public Runnable runnable;
        public TokenType tokenType;

        TokenListenerState() {
        }
    }

    public AsyncTokenStorage(INativeTokenGenerator iNativeTokenGenerator, Handler handler, ISDKMetrics iSDKMetrics) {
        this._handler = handler;
        this._nativeTokenGenerator = iNativeTokenGenerator;
        this._sdkMetrics = iSDKMetrics;
    }

    public synchronized void setConfiguration(Configuration configuration) {
        this._configuration = configuration;
        boolean isValidConfig = isValidConfig(configuration);
        this._configurationWasSet = isValidConfig;
        if (isValidConfig) {
            if (this._nativeTokenGenerator == null) {
                DeviceInfoReaderBuilder deviceInfoReaderBuilder = new DeviceInfoReaderBuilder(new ConfigurationReader(), PrivacyConfigStorage.getInstance(), GameSessionIdReader.getInstance());
                ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
                this._nativeTokenGenerator = new NativeTokenGenerator(newSingleThreadExecutor, deviceInfoReaderBuilder);
                if (configuration.getExperiments().shouldNativeTokenAwaitPrivacy()) {
                    this._nativeTokenGenerator = new NativeTokenGeneratorWithPrivacyAwait(newSingleThreadExecutor, this._nativeTokenGenerator, configuration.getPrivacyRequestWaitTimeout());
                }
            }
            Iterator it = new ArrayList(this._tokenListeners).iterator();
            while (it.hasNext()) {
                handleTokenInvocation((TokenListenerState) it.next());
            }
        }
    }

    public synchronized void onTokenAvailable() {
        this._tokenAvailable = true;
        if (this._configurationWasSet) {
            notifyListenersTokenReady();
        }
    }

    public synchronized void getToken(IUnityAdsTokenListener iUnityAdsTokenListener) {
        if (SdkProperties.getCurrentInitializationState() == SdkProperties.InitializationState.INITIALIZED_FAILED) {
            iUnityAdsTokenListener.onUnityAdsTokenReady((String) null);
            sendTokenMetrics((String) null, TokenType.TOKEN_REMOTE);
        } else if (SdkProperties.getCurrentInitializationState() == SdkProperties.InitializationState.NOT_INITIALIZED) {
            iUnityAdsTokenListener.onUnityAdsTokenReady((String) null);
            sendTokenMetrics((String) null, TokenType.TOKEN_REMOTE);
        } else {
            TokenListenerState addTimeoutHandler = addTimeoutHandler(iUnityAdsTokenListener);
            if (this._configurationWasSet) {
                handleTokenInvocation(addTimeoutHandler);
            }
        }
    }

    private synchronized TokenListenerState addTimeoutHandler(IUnityAdsTokenListener iUnityAdsTokenListener) {
        final TokenListenerState tokenListenerState;
        tokenListenerState = new TokenListenerState();
        tokenListenerState.listener = iUnityAdsTokenListener;
        tokenListenerState.tokenType = TokenType.TOKEN_REMOTE;
        tokenListenerState.runnable = new Runnable() {
            public void run() {
                AsyncTokenStorage.this.notifyTokenReady(tokenListenerState, (String) null);
            }
        };
        this._tokenListeners.add(tokenListenerState);
        this._handler.postDelayed(tokenListenerState.runnable, (long) this._configuration.getTokenTimeout());
        return tokenListenerState;
    }

    private synchronized void notifyListenersTokenReady() {
        while (true) {
            if (this._tokenListeners.isEmpty()) {
                break;
            }
            String token = TokenStorage.getToken();
            if (token == null) {
                break;
            }
            notifyTokenReady(this._tokenListeners.get(0), token);
        }
    }

    private void handleTokenInvocation(final TokenListenerState tokenListenerState) {
        if (!tokenListenerState.invoked) {
            tokenListenerState.invoked = true;
            if (this._tokenAvailable || !this._configuration.getExperiments().isNativeTokenEnabled()) {
                tokenListenerState.tokenType = TokenType.TOKEN_REMOTE;
                String token = TokenStorage.getToken();
                if (token != null && !token.isEmpty()) {
                    notifyTokenReady(tokenListenerState, token);
                    return;
                }
                return;
            }
            tokenListenerState.tokenType = TokenType.TOKEN_NATIVE;
            this._nativeTokenGenerator.generateToken(new INativeTokenGeneratorListener() {
                public void onReady(final String str) {
                    AsyncTokenStorage.this._handler.post(new Runnable() {
                        public void run() {
                            AsyncTokenStorage.this.notifyTokenReady(tokenListenerState, str);
                        }
                    });
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public synchronized void notifyTokenReady(TokenListenerState tokenListenerState, String str) {
        if (this._tokenListeners.remove(tokenListenerState)) {
            tokenListenerState.listener.onUnityAdsTokenReady(str);
            try {
                this._handler.removeCallbacks(tokenListenerState.runnable);
            } catch (Exception e) {
                DeviceLog.exception("Failed to remove callback from a handler", e);
            }
        }
        sendTokenMetrics(str, tokenListenerState.tokenType);
    }

    /* renamed from: com.unity3d.services.ads.token.AsyncTokenStorage$3 */
    static /* synthetic */ class C02463 {
        static final /* synthetic */ int[] $SwitchMap$com$unity3d$services$core$device$TokenType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.unity3d.services.core.device.TokenType[] r0 = com.unity3d.services.core.device.TokenType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$unity3d$services$core$device$TokenType = r0
                com.unity3d.services.core.device.TokenType r1 = com.unity3d.services.core.device.TokenType.TOKEN_NATIVE     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$unity3d$services$core$device$TokenType     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.services.core.device.TokenType r1 = com.unity3d.services.core.device.TokenType.TOKEN_REMOTE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.token.AsyncTokenStorage.C02463.<clinit>():void");
        }
    }

    private void sendTokenMetrics(String str, TokenType tokenType) {
        int i = C02463.$SwitchMap$com$unity3d$services$core$device$TokenType[tokenType.ordinal()];
        if (i == 1) {
            sendNativeTokenMetrics(str);
        } else if (i != 2) {
            DeviceLog.error("Unknown token type passed to sendTokenMetrics");
        } else {
            sendRemoteTokenMetrics(str);
        }
    }

    private void sendNativeTokenMetrics(String str) {
        ISDKMetrics iSDKMetrics = this._sdkMetrics;
        if (iSDKMetrics != null) {
            if (str == null) {
                iSDKMetrics.sendMetric(TSIMetric.newNativeGeneratedTokenNull(getMetricTags()));
            } else {
                iSDKMetrics.sendMetric(TSIMetric.newNativeGeneratedTokenAvailable(getMetricTags()));
            }
        }
    }

    private void sendRemoteTokenMetrics(String str) {
        if (this._sdkMetrics != null) {
            if (str == null || str.isEmpty()) {
                this._sdkMetrics.sendMetric(TSIMetric.newAsyncTokenNull(getMetricTags()));
            } else {
                this._sdkMetrics.sendMetric(TSIMetric.newAsyncTokenAvailable(getMetricTags()));
            }
        }
    }

    private Map<String, String> getMetricTags() {
        HashMap hashMap = new HashMap();
        hashMap.put(AdOperationMetric.INIT_STATE, this._initStatusReader.getInitializationStateString(SdkProperties.getCurrentInitializationState()));
        return hashMap;
    }

    private boolean isValidConfig(Configuration configuration) {
        return (configuration == null || configuration.getExperiments() == null) ? false : true;
    }
}
