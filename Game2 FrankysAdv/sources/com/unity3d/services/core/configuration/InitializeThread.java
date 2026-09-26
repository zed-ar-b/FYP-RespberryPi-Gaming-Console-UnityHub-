package com.unity3d.services.core.configuration;

import android.os.Build;
import android.os.ConditionVariable;
import android.text.TextUtils;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.ads.token.TokenStorage;
import com.unity3d.services.core.api.DownloadLatestWebViewStatus;
import com.unity3d.services.core.api.Lifecycle;
import com.unity3d.services.core.cache.CacheDirectory;
import com.unity3d.services.core.connectivity.ConnectivityMonitor;
import com.unity3d.services.core.connectivity.IConnectivityListener;
import com.unity3d.services.core.device.reader.DeviceInfoReaderBuilder;
import com.unity3d.services.core.device.reader.DeviceInfoReaderPrivacyBuilder;
import com.unity3d.services.core.device.reader.GameSessionIdReader;
import com.unity3d.services.core.lifecycle.CachedLifecycle;
import com.unity3d.services.core.lifecycle.LifecycleListener;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.properties.SdkProperties;
import com.unity3d.services.core.request.WebRequest;
import com.unity3d.services.core.request.metrics.Metric;
import com.unity3d.services.core.request.metrics.SDKMetrics;
import com.unity3d.services.core.request.metrics.TSIMetric;
import com.unity3d.services.core.webview.WebView;
import com.unity3d.services.core.webview.WebViewApp;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

public class InitializeThread extends Thread {
    private static InitializeThread _thread;
    private boolean _didRetry = false;
    private InitializeState _state;
    private String _stateName;
    private long _stateStartTimestamp;
    private boolean _stopThread = false;

    private int getStatePrefixLength() {
        return 15;
    }

    private InitializeThread(InitializeState initializeState) {
        this._state = initializeState;
    }

    public void run() {
        while (this._state != null && !this._stopThread) {
            try {
                try {
                    handleStateStartMetrics(this._state);
                    InitializeState execute = this._state.execute();
                    this._state = execute;
                    handleStateEndMetrics(execute);
                } catch (Exception e) {
                    DeviceLog.exception("Unity Ads SDK encountered an error during initialization, cancel initialization", e);
                    Utilities.runOnUiThread(new Runnable() {
                        public void run() {
                            SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INTERNAL_ERROR, "Unity Ads SDK encountered an error during initialization, cancel initialization");
                        }
                    });
                    this._state = new InitializeStateForceReset();
                } catch (OutOfMemoryError e2) {
                    DeviceLog.exception("Unity Ads SDK failed to initialize due to application doesn't have enough memory to initialize Unity Ads SDK", new Exception(e2));
                    Utilities.runOnUiThread(new Runnable() {
                        public void run() {
                            SdkProperties.notifyInitializationFailed(UnityAds.UnityAdsInitializationError.INTERNAL_ERROR, "Unity Ads SDK failed to initialize due to application doesn't have enough memory to initialize Unity Ads SDK");
                        }
                    });
                    this._state = new InitializeStateForceReset();
                }
            } catch (OutOfMemoryError unused) {
            }
        }
        _thread = null;
    }

    public void quit() {
        this._stopThread = true;
    }

    public static synchronized void initialize(Configuration configuration) {
        synchronized (InitializeThread.class) {
            if (_thread == null) {
                InitializeEventsMetricSender.getInstance().didInitStart();
                CachedLifecycle.register();
                InitializeThread initializeThread = new InitializeThread(new InitializeStateLoadConfigFile(configuration));
                _thread = initializeThread;
                initializeThread.setName("UnityAdsInitializeThread");
                _thread.start();
            }
        }
    }

    public static synchronized void reset() {
        synchronized (InitializeThread.class) {
            if (_thread == null) {
                InitializeThread initializeThread = new InitializeThread(new InitializeStateForceReset());
                _thread = initializeThread;
                initializeThread.setName("UnityAdsResetThread");
                _thread.start();
            }
        }
    }

    public static synchronized DownloadLatestWebViewStatus downloadLatestWebView() {
        synchronized (InitializeThread.class) {
            if (_thread != null) {
                DownloadLatestWebViewStatus downloadLatestWebViewStatus = DownloadLatestWebViewStatus.INIT_QUEUE_NOT_EMPTY;
                return downloadLatestWebViewStatus;
            } else if (SdkProperties.getLatestConfiguration() == null) {
                DownloadLatestWebViewStatus downloadLatestWebViewStatus2 = DownloadLatestWebViewStatus.MISSING_LATEST_CONFIG;
                return downloadLatestWebViewStatus2;
            } else {
                InitializeThread initializeThread = new InitializeThread(new InitializeStateCheckForCachedWebViewUpdate(SdkProperties.getLatestConfiguration()));
                _thread = initializeThread;
                initializeThread.setName("UnityAdsDownloadThread");
                _thread.start();
                DownloadLatestWebViewStatus downloadLatestWebViewStatus3 = DownloadLatestWebViewStatus.BACKGROUND_DOWNLOAD_STARTED;
                return downloadLatestWebViewStatus3;
            }
        }
    }

    private void handleStateStartMetrics(InitializeState initializeState) {
        if (isRetryState(initializeState)) {
            this._didRetry = true;
        } else {
            if (!this._didRetry) {
                this._stateStartTimestamp = System.nanoTime();
            }
            this._didRetry = false;
        }
        this._stateName = getMetricNameForState(initializeState);
    }

    private void handleStateEndMetrics(InitializeState initializeState) {
        if (this._stateName != null && !isRetryState(initializeState) && !this._stateName.equals("native_retry_state")) {
            SDKMetrics.getInstance().sendMetric(new Metric(this._stateName, Long.valueOf(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this._stateStartTimestamp)), getMetricTagsForState()));
        }
    }

    private Map<String, String> getMetricTagsForState() {
        return InitializeEventsMetricSender.getInstance().getRetryTags();
    }

    private String getMetricNameForState(InitializeState initializeState) {
        if (initializeState == null) {
            return null;
        }
        String simpleName = initializeState.getClass().getSimpleName();
        if (simpleName.length() == 0) {
            return null;
        }
        String lowerCase = simpleName.substring(getStatePrefixLength()).toLowerCase();
        StringBuilder sb = new StringBuilder(7 + lowerCase.length() + 6);
        sb.append("native_");
        sb.append(lowerCase);
        sb.append("_state");
        return sb.toString();
    }

    private boolean isRetryState(InitializeState initializeState) {
        return initializeState instanceof InitializeStateRetry;
    }

    private static abstract class InitializeState {
        public abstract InitializeState execute();

        private InitializeState() {
        }
    }

    public static class InitializeStateLoadConfigFile extends InitializeState {
        private Configuration _configuration;

        public InitializeStateLoadConfigFile(Configuration configuration) {
            super();
            this._configuration = configuration;
        }

        public InitializeState execute() {
            DeviceLog.debug("Unity Ads init: Loading Config File Parameters");
            File file = new File(SdkProperties.getLocalConfigurationFilepath());
            if (!file.exists()) {
                return new InitializeStateReset(this._configuration);
            }
            try {
                Configuration configuration = new Configuration(new JSONObject(new String(Utilities.readFileBytes(file))));
                this._configuration = configuration;
                return new InitializeStateReset(configuration);
            } catch (Exception unused) {
                DeviceLog.debug("Unity Ads init: Using default configuration parameters");
                return new InitializeStateReset(this._configuration);
            } catch (Throwable unused2) {
                return new InitializeStateReset(this._configuration);
            }
        }
    }

    public static class InitializeStateReset extends InitializeState {
        private Configuration _configuration;
        private int _resetWebAppTimeout;

        public InitializeStateReset(Configuration configuration) {
            super();
            this._configuration = configuration;
            this._resetWebAppTimeout = configuration.getResetWebappTimeout();
        }

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeState execute() {
            boolean z;
            DeviceLog.debug("Unity Ads init: starting init");
            final ConditionVariable conditionVariable = new ConditionVariable();
            final WebViewApp currentApp = WebViewApp.getCurrentApp();
            if (currentApp != null) {
                currentApp.resetWebViewAppInitialization();
                if (currentApp.getWebView() != null) {
                    Utilities.runOnUiThread(new Runnable() {
                        public void run() {
                            currentApp.getWebView().destroy();
                            currentApp.setWebView((WebView) null);
                            conditionVariable.open();
                        }
                    });
                    z = conditionVariable.block((long) this._resetWebAppTimeout);
                } else {
                    z = true;
                }
                if (!z) {
                    return new InitializeStateError(ErrorState.ResetWebApp, new Exception("Reset failed on opening ConditionVariable"), this._configuration);
                }
            }
            if (Build.VERSION.SDK_INT > 13) {
                unregisterLifecycleCallbacks();
            }
            SdkProperties.setCacheDirectory((CacheDirectory) null);
            if (SdkProperties.getCacheDirectory() == null) {
                return new InitializeStateError(ErrorState.ResetWebApp, new Exception("Cache directory is NULL"), this._configuration);
            }
            SdkProperties.setInitialized(false);
            for (String moduleConfiguration : this._configuration.getModuleConfigurationList()) {
                IModuleConfiguration moduleConfiguration2 = this._configuration.getModuleConfiguration(moduleConfiguration);
                if (moduleConfiguration2 != null) {
                    moduleConfiguration2.resetState(this._configuration);
                }
            }
            return new InitializeStateInitModules(this._configuration);
        }

        private void unregisterLifecycleCallbacks() {
            if (Lifecycle.getLifecycleListener() != null) {
                if (ClientProperties.getApplication() != null) {
                    ClientProperties.getApplication().unregisterActivityLifecycleCallbacks(Lifecycle.getLifecycleListener());
                }
                Lifecycle.setLifecycleListener((LifecycleListener) null);
            }
        }
    }

    public static class InitializeStateForceReset extends InitializeStateReset {
        public InitializeStateForceReset() {
            super(new Configuration());
        }

        public InitializeState execute() {
            SdkProperties.setInitializeState(SdkProperties.InitializationState.NOT_INITIALIZED);
            super.execute();
            return null;
        }
    }

    public static class InitializeStateInitModules extends InitializeState {
        private Configuration _configuration;

        public InitializeStateInitModules(Configuration configuration) {
            super();
            this._configuration = configuration;
        }

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeState execute() {
            for (String moduleConfiguration : this._configuration.getModuleConfigurationList()) {
                IModuleConfiguration moduleConfiguration2 = this._configuration.getModuleConfiguration(moduleConfiguration);
                if (moduleConfiguration2 != null && !moduleConfiguration2.initModuleState(this._configuration)) {
                    return new InitializeStateError(ErrorState.InitModules, new Exception("Unity Ads config server resolves to loopback address (due to ad blocker?)"), this._configuration);
                }
            }
            return new InitializeStateConfig(this._configuration);
        }
    }

    public static class InitializeStateConfig extends InitializeState {
        /* access modifiers changed from: private */
        public Configuration _configuration;
        /* access modifiers changed from: private */
        public Configuration _localConfig;
        private int _maxRetries;
        /* access modifiers changed from: private */
        public InitializeState _nextState;
        private int _retries = 0;
        private long _retryDelay;
        private double _scalingFactor;

        public InitializeStateConfig(Configuration configuration) {
            super();
            this._configuration = new Configuration(SdkProperties.getConfigUrl(), configuration.getExperimentsReader());
            this._retryDelay = configuration.getRetryDelay();
            this._maxRetries = configuration.getMaxRetries();
            this._scalingFactor = configuration.getRetryScalingFactor();
            this._localConfig = configuration;
            this._nextState = null;
        }

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeState execute() {
            DeviceLog.info("Unity Ads init: load configuration from " + SdkProperties.getConfigUrl());
            if (this._configuration.getExperiments() == null || !this._configuration.getExperiments().isTwoStageInitializationEnabled()) {
                return executeLegacy(this._configuration);
            }
            return executeWithLoader();
        }

        public InitializeState executeLegacy(Configuration configuration) {
            try {
                configuration.makeRequest();
                if (configuration.getDelayWebViewUpdate()) {
                    return new InitializeStateLoadCacheConfigAndWebView(configuration, this._localConfig);
                }
                InitializeState initializeStateCreateWithRemote = configuration.getExperiments().isNativeWebViewCacheEnabled() ? new InitializeStateCreateWithRemote(configuration) : new InitializeStateLoadCache(configuration);
                this._nextState = initializeStateCreateWithRemote;
                return initializeStateCreateWithRemote;
            } catch (Exception e) {
                int i = this._retries;
                if (i >= this._maxRetries) {
                    return new InitializeStateNetworkError(ErrorState.NetworkConfigRequest, e, this, this._localConfig);
                }
                this._retryDelay = (long) (((double) this._retryDelay) * this._scalingFactor);
                this._retries = i + 1;
                InitializeEventsMetricSender.getInstance().onRetryConfig();
                return new InitializeStateRetry(this, this._retryDelay);
            }
        }

        public InitializeState executeWithLoader() {
            PrivacyConfigStorage instance = PrivacyConfigStorage.getInstance();
            IConfigurationLoader configurationLoader = new ConfigurationLoader(new ConfigurationRequestFactory(this._configuration, new DeviceInfoReaderBuilder(new ConfigurationReader(), instance, GameSessionIdReader.getInstance())));
            if (this._configuration.getExperiments().isPrivacyRequestEnabled()) {
                configurationLoader = new PrivacyConfigurationLoader(configurationLoader, new ConfigurationRequestFactory(this._configuration, new DeviceInfoReaderPrivacyBuilder(new ConfigurationReader(), instance, GameSessionIdReader.getInstance())), instance);
            }
            final Configuration configuration = new Configuration(SdkProperties.getConfigUrl());
            try {
                configurationLoader.loadConfiguration(new IConfigurationLoaderListener() {
                    public void onSuccess(Configuration configuration) {
                        Configuration unused = InitializeStateConfig.this._configuration = configuration;
                        InitializeStateConfig.this._configuration.saveToDisk();
                        if (InitializeStateConfig.this._configuration.getDelayWebViewUpdate()) {
                            InitializeStateConfig initializeStateConfig = InitializeStateConfig.this;
                            InitializeState unused2 = initializeStateConfig._nextState = new InitializeStateLoadCacheConfigAndWebView(initializeStateConfig._configuration, InitializeStateConfig.this._localConfig);
                        }
                        TokenStorage.setInitToken(InitializeStateConfig.this._configuration.getUnifiedAuctionToken());
                        boolean isNativeWebViewCacheEnabled = InitializeStateConfig.this._configuration.getExperiments().isNativeWebViewCacheEnabled();
                        InitializeStateConfig initializeStateConfig2 = InitializeStateConfig.this;
                        InitializeState unused3 = initializeStateConfig2._nextState = isNativeWebViewCacheEnabled ? new InitializeStateCreateWithRemote(initializeStateConfig2._configuration) : new InitializeStateLoadCache(initializeStateConfig2._configuration);
                    }

                    public void onError(String str) {
                        SDKMetrics.getInstance().sendMetric(TSIMetric.newEmergencySwitchOff());
                        InitializeStateConfig initializeStateConfig = InitializeStateConfig.this;
                        InitializeState unused = initializeStateConfig._nextState = initializeStateConfig.executeLegacy(configuration);
                    }
                });
                return this._nextState;
            } catch (Exception e) {
                int i = this._retries;
                if (i >= this._maxRetries) {
                    return new InitializeStateNetworkError(ErrorState.NetworkConfigRequest, e, this, this._configuration);
                }
                this._retryDelay = (long) (((double) this._retryDelay) * this._scalingFactor);
                this._retries = i + 1;
                InitializeEventsMetricSender.getInstance().onRetryConfig();
                return new InitializeStateRetry(this, this._retryDelay);
            }
        }
    }

    public static class InitializeStateLoadCache extends InitializeState {
        private Configuration _configuration;

        public InitializeStateLoadCache(Configuration configuration) {
            super();
            this._configuration = configuration;
        }

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeState execute() {
            DeviceLog.debug("Unity Ads init: check if webapp can be loaded from local cache");
            try {
                byte[] readFileBytes = Utilities.readFileBytes(new File(SdkProperties.getLocalWebViewFile()));
                String Sha256 = Utilities.Sha256(readFileBytes);
                if (Sha256 == null || !Sha256.equals(this._configuration.getWebViewHash())) {
                    return new InitializeStateLoadWeb(this._configuration);
                }
                try {
                    String str = new String(readFileBytes, "UTF-8");
                    DeviceLog.info("Unity Ads init: webapp loaded from local cache");
                    return new InitializeStateCreate(this._configuration, str);
                } catch (Exception e) {
                    return new InitializeStateError(ErrorState.LoadCache, e, this._configuration);
                }
            } catch (Exception e2) {
                DeviceLog.debug("Unity Ads init: webapp not found in local cache: " + e2.getMessage());
                return new InitializeStateLoadWeb(this._configuration);
            }
        }
    }

    public static class InitializeStateLoadWeb extends InitializeState {
        private Configuration _configuration;
        private int _maxRetries;
        private int _retries = 0;
        private long _retryDelay;
        private double _scalingFactor;

        public InitializeStateLoadWeb(Configuration configuration) {
            super();
            this._configuration = configuration;
            this._retryDelay = configuration.getRetryDelay();
            this._maxRetries = configuration.getMaxRetries();
            this._scalingFactor = configuration.getRetryScalingFactor();
        }

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeState execute() {
            DeviceLog.info("Unity Ads init: loading webapp from " + this._configuration.getWebViewUrl());
            try {
                try {
                    String makeRequest = new WebRequest(this._configuration.getWebViewUrl(), "GET", (Map<String, List<String>>) null).makeRequest();
                    String webViewHash = this._configuration.getWebViewHash();
                    if (webViewHash != null && !Utilities.Sha256(makeRequest).equals(webViewHash)) {
                        return new InitializeStateError(ErrorState.InvalidHash, new Exception("Invalid webViewHash"), this._configuration);
                    }
                    if (webViewHash != null) {
                        Utilities.writeFile(new File(SdkProperties.getLocalWebViewFile()), makeRequest);
                    }
                    return new InitializeStateCreate(this._configuration, makeRequest);
                } catch (Exception e) {
                    int i = this._retries;
                    if (i >= this._maxRetries) {
                        return new InitializeStateNetworkError(ErrorState.NetworkWebviewRequest, e, this, this._configuration);
                    }
                    this._retryDelay = (long) (((double) this._retryDelay) * this._scalingFactor);
                    this._retries = i + 1;
                    InitializeEventsMetricSender.getInstance().onRetryWebview();
                    return new InitializeStateRetry(this, this._retryDelay);
                }
            } catch (MalformedURLException e2) {
                DeviceLog.exception("Malformed URL", e2);
                return new InitializeStateError(ErrorState.MalformedWebviewRequest, e2, this._configuration);
            }
        }
    }

    public static class InitializeStateCreate extends InitializeState {
        private Configuration _configuration;
        private String _webViewData;

        public InitializeStateCreate(Configuration configuration, String str) {
            super();
            this._configuration = configuration;
            this._webViewData = str;
        }

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public String getWebData() {
            return this._webViewData;
        }

        public InitializeState execute() {
            DeviceLog.debug("Unity Ads init: creating webapp");
            Configuration configuration = this._configuration;
            configuration.setWebViewData(this._webViewData);
            try {
                ErrorState create = WebViewApp.create(configuration, false);
                if (create == null) {
                    return new InitializeStateComplete(this._configuration);
                }
                String webAppFailureMessage = WebViewApp.getCurrentApp().getWebAppFailureMessage() != null ? WebViewApp.getCurrentApp().getWebAppFailureMessage() : "Unity Ads WebApp creation failed";
                DeviceLog.error(webAppFailureMessage);
                return new InitializeStateError(create, new Exception(webAppFailureMessage), this._configuration);
            } catch (IllegalThreadStateException e) {
                DeviceLog.exception("Illegal Thread", e);
                return new InitializeStateError(ErrorState.CreateWebApp, e, this._configuration);
            }
        }
    }

    public static class InitializeStateCreateWithRemote extends InitializeState {
        private Configuration _configuration;

        public InitializeStateCreateWithRemote(Configuration configuration) {
            super();
            this._configuration = configuration;
        }

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeState execute() {
            DeviceLog.debug("Unity Ads init: creating webapp");
            try {
                ErrorState create = WebViewApp.create(this._configuration, true);
                if (create == null) {
                    return new InitializeStateComplete(this._configuration);
                }
                String webAppFailureMessage = WebViewApp.getCurrentApp().getWebAppFailureMessage() != null ? WebViewApp.getCurrentApp().getWebAppFailureMessage() : "Unity Ads WebApp creation failed";
                DeviceLog.error(webAppFailureMessage);
                return new InitializeStateError(create, new Exception(webAppFailureMessage), this._configuration);
            } catch (IllegalThreadStateException e) {
                DeviceLog.exception("Illegal Thread", e);
                return new InitializeStateError(ErrorState.CreateWebApp, e, this._configuration);
            }
        }
    }

    public static class InitializeStateComplete extends InitializeState {
        private Configuration _configuration;

        public InitializeStateComplete(Configuration configuration) {
            super();
            this._configuration = configuration;
        }

        public InitializeState execute() {
            for (String moduleConfiguration : this._configuration.getModuleConfigurationList()) {
                IModuleConfiguration moduleConfiguration2 = this._configuration.getModuleConfiguration(moduleConfiguration);
                if (moduleConfiguration2 != null) {
                    moduleConfiguration2.initCompleteState(this._configuration);
                }
            }
            return null;
        }
    }

    public static class InitializeStateError extends InitializeState {
        protected Configuration _configuration;
        ErrorState _errorState;
        Exception _exception;

        public InitializeStateError(ErrorState errorState, Exception exc, Configuration configuration) {
            super();
            this._errorState = errorState;
            this._exception = exc;
            this._configuration = configuration;
        }

        public InitializeState execute() {
            DeviceLog.error("Unity Ads init: halting init in " + this._errorState.getMetricName() + ": " + this._exception.getMessage());
            for (String moduleConfiguration : this._configuration.getModuleConfigurationList()) {
                IModuleConfiguration moduleConfiguration2 = this._configuration.getModuleConfiguration(moduleConfiguration);
                if (moduleConfiguration2 != null) {
                    moduleConfiguration2.initErrorState(this._configuration, this._errorState, this._exception.getMessage());
                }
            }
            return null;
        }
    }

    public static class InitializeStateNetworkError extends InitializeStateError implements IConnectivityListener {
        private static long _lastConnectedEventTimeMs;
        private static int _receivedConnectedEvents;
        private ConditionVariable _conditionVariable;
        private int _connectedEventThreshold;
        private InitializeState _erroredState;
        private int _maximumConnectedEvents;
        private long _networkErrorTimeout;
        private ErrorState _state;

        public InitializeStateNetworkError(ErrorState errorState, Exception exc, InitializeState initializeState, Configuration configuration) {
            super(errorState, exc, configuration);
            this._state = errorState;
            _receivedConnectedEvents = 0;
            _lastConnectedEventTimeMs = 0;
            this._erroredState = initializeState;
            this._networkErrorTimeout = configuration.getNetworkErrorTimeout();
            this._maximumConnectedEvents = configuration.getMaximumConnectedEvents();
            this._connectedEventThreshold = configuration.getConnectedEventThreshold();
        }

        public InitializeState execute() {
            DeviceLog.error("Unity Ads init: network error, waiting for connection events");
            this._conditionVariable = new ConditionVariable();
            ConnectivityMonitor.addListener(this);
            if (this._conditionVariable.block(this._networkErrorTimeout)) {
                ConnectivityMonitor.removeListener(this);
                return this._erroredState;
            }
            ConnectivityMonitor.removeListener(this);
            return new InitializeStateError(this._state, new Exception("No connected events within the timeout!"), this._configuration);
        }

        public void onConnected() {
            _receivedConnectedEvents++;
            DeviceLog.debug("Unity Ads init got connected event");
            if (shouldHandleConnectedEvent()) {
                this._conditionVariable.open();
            }
            if (_receivedConnectedEvents > this._maximumConnectedEvents) {
                ConnectivityMonitor.removeListener(this);
            }
            _lastConnectedEventTimeMs = System.currentTimeMillis();
        }

        public void onDisconnected() {
            DeviceLog.debug("Unity Ads init got disconnected event");
        }

        private boolean shouldHandleConnectedEvent() {
            return System.currentTimeMillis() - _lastConnectedEventTimeMs >= ((long) this._connectedEventThreshold) && _receivedConnectedEvents <= this._maximumConnectedEvents;
        }
    }

    public static class InitializeStateRetry extends InitializeState {
        long _delay;
        InitializeState _state;

        public InitializeStateRetry(InitializeState initializeState, long j) {
            super();
            this._state = initializeState;
            this._delay = j;
        }

        public InitializeState execute() {
            DeviceLog.debug("Unity Ads init: retrying in " + this._delay + " milliseconds");
            try {
                Thread.sleep(this._delay);
            } catch (Exception e) {
                DeviceLog.exception("Init retry interrupted", e);
            }
            return this._state;
        }
    }

    public static class InitializeStateLoadCacheConfigAndWebView extends InitializeState {
        private Configuration _configuration;
        private Configuration _localConfig;

        public InitializeStateLoadCacheConfigAndWebView(Configuration configuration, Configuration configuration2) {
            super();
            this._configuration = configuration;
            this._localConfig = configuration2;
        }

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeState execute() {
            try {
                return new InitializeStateCheckForUpdatedWebView(this._configuration, InitializeThread.loadCachedFileToByteArray(new File(SdkProperties.getLocalWebViewFile())), this._localConfig);
            } catch (Exception unused) {
                return new InitializeStateCleanCache(this._configuration, new InitializeStateLoadWeb(this._configuration));
            }
        }
    }

    /* access modifiers changed from: private */
    public static byte[] loadCachedFileToByteArray(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IOException("file not found");
        }
        try {
            return Utilities.readFileBytes(file);
        } catch (IOException unused) {
            throw new IOException("could not read from file");
        }
    }

    public static class InitializeStateCleanCache extends InitializeState {
        private Configuration _configuration;
        private InitializeState _nextState;

        public InitializeStateCleanCache(Configuration configuration, InitializeState initializeState) {
            super();
            this._configuration = configuration;
            this._nextState = initializeState;
        }

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeState execute() {
            try {
                File file = new File(SdkProperties.getLocalConfigurationFilepath());
                File file2 = new File(SdkProperties.getLocalWebViewFile());
                file.delete();
                file2.delete();
            } catch (Exception e) {
                DeviceLog.error("Failure trying to clean cache: " + e.getMessage());
            }
            return this._nextState;
        }
    }

    public static class InitializeStateCleanCacheIgnoreError extends InitializeStateCleanCache {
        public InitializeStateCleanCacheIgnoreError(Configuration configuration, InitializeState initializeState) {
            super(configuration, initializeState);
        }

        public InitializeState execute() {
            try {
                InitializeState execute = super.execute();
                if (!(execute instanceof InitializeStateError)) {
                    return execute;
                }
                return null;
            } catch (Exception unused) {
                return null;
            }
        }
    }

    public static class InitializeStateCheckForUpdatedWebView extends InitializeState {
        private Configuration _configuration;
        private Configuration _localWebViewConfiguration;
        private byte[] _localWebViewData;

        public InitializeStateCheckForUpdatedWebView(Configuration configuration, byte[] bArr, Configuration configuration2) {
            super();
            this._configuration = configuration;
            this._localWebViewData = bArr;
            this._localWebViewConfiguration = configuration2;
        }

        public InitializeState execute() {
            try {
                String Sha256 = Utilities.Sha256(this._localWebViewData);
                if (!Sha256.equals(this._configuration.getWebViewHash())) {
                    SdkProperties.setLatestConfiguration(this._configuration);
                }
                if (!TextUtils.isEmpty(Sha256)) {
                    if (this._localWebViewConfiguration != null) {
                        if (this._localWebViewConfiguration.getWebViewHash() != null && this._localWebViewConfiguration.getWebViewHash().equals(Sha256) && SdkProperties.getVersionName().equals(this._localWebViewConfiguration.getSdkVersion())) {
                            return new InitializeStateCreate(this._localWebViewConfiguration, new String(this._localWebViewData, "UTF-8"));
                        }
                    }
                    if (this._configuration != null && this._configuration.getWebViewHash().equals(Sha256)) {
                        return new InitializeStateCreate(this._configuration, new String(this._localWebViewData, "UTF-8"));
                    }
                }
            } catch (Exception unused) {
            }
            return new InitializeStateCleanCache(this._configuration, new InitializeStateLoadWeb(this._configuration));
        }
    }

    public static class InitializeStateDownloadWebView extends InitializeState {
        private Configuration _configuration;
        private int _retries = 0;
        private long _retryDelay;

        public InitializeStateDownloadWebView(Configuration configuration) {
            super();
            this._configuration = configuration;
            this._retryDelay = configuration.getRetryDelay();
        }

        public InitializeState execute() {
            DeviceLog.info("Unity Ads init: downloading webapp from " + this._configuration.getWebViewUrl());
            try {
                try {
                    String makeRequest = new WebRequest(this._configuration.getWebViewUrl(), "GET", (Map<String, List<String>>) null).makeRequest();
                    String webViewHash = this._configuration.getWebViewHash();
                    if (makeRequest == null || webViewHash == null || !Utilities.Sha256(makeRequest).equals(webViewHash)) {
                        return null;
                    }
                    return new InitializeStateUpdateCache(this._configuration, makeRequest);
                } catch (Exception unused) {
                    if (this._retries >= this._configuration.getMaxRetries()) {
                        return null;
                    }
                    long retryScalingFactor = (long) (((double) this._retryDelay) * this._configuration.getRetryScalingFactor());
                    this._retryDelay = retryScalingFactor;
                    this._retries++;
                    return new InitializeStateRetry(this, retryScalingFactor);
                }
            } catch (Exception e) {
                DeviceLog.exception("Malformed URL", e);
                return null;
            }
        }
    }

    public static class InitializeStateUpdateCache extends InitializeState {
        private Configuration _configuration;
        private String _webViewData;

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeStateUpdateCache(Configuration configuration, String str) {
            super();
            this._configuration = configuration;
            this._webViewData = str;
        }

        public InitializeState execute() {
            if (!(this._configuration == null || this._webViewData == null)) {
                try {
                    Utilities.writeFile(new File(SdkProperties.getLocalWebViewFile()), this._webViewData);
                    Utilities.writeFile(new File(SdkProperties.getLocalConfigurationFilepath()), this._configuration.getJSONString());
                } catch (Exception unused) {
                    return new InitializeStateCleanCacheIgnoreError(this._configuration, (InitializeState) null);
                }
            }
            return null;
        }
    }

    public static class InitializeStateCheckForCachedWebViewUpdate extends InitializeState {
        private Configuration _configuration;

        public Configuration getConfiguration() {
            return this._configuration;
        }

        public InitializeStateCheckForCachedWebViewUpdate(Configuration configuration) {
            super();
            this._configuration = configuration;
        }

        public InitializeState execute() {
            try {
                byte[] access$400 = InitializeThread.loadCachedFileToByteArray(new File(SdkProperties.getLocalWebViewFile()));
                if (Utilities.Sha256(access$400).equals(this._configuration.getWebViewHash())) {
                    return new InitializeStateUpdateCache(this._configuration, new String(access$400, "UTF-8"));
                }
            } catch (Exception unused) {
            }
            return new InitializeStateDownloadWebView(this._configuration);
        }
    }
}
