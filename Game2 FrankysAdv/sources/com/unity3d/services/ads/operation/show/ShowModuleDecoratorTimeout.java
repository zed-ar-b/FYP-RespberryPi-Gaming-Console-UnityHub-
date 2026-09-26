package com.unity3d.services.ads.operation.show;

import com.unity3d.ads.UnityAds;
import com.unity3d.services.core.configuration.ConfigurationReader;
import com.unity3d.services.core.request.metrics.AdOperationError;
import com.unity3d.services.core.request.metrics.AdOperationMetric;
import com.unity3d.services.core.timer.BaseTimer;
import com.unity3d.services.core.timer.ITimerListener;
import com.unity3d.services.core.webview.bridge.IWebViewBridgeInvoker;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShowModuleDecoratorTimeout extends ShowModuleDecorator {
    private static final String errorMsgTimeout = "[UnityAds] Timeout while trying to show ";
    private final ExecutorService _executorService = Executors.newSingleThreadExecutor();
    private final boolean _useNewTimer;

    public ShowModuleDecoratorTimeout(IShowModule iShowModule, ConfigurationReader configurationReader) {
        super(iShowModule);
        this._useNewTimer = configurationReader.getCurrentConfiguration().getExperiments().isNewLifecycleTimer();
    }

    public void executeAdOperation(IWebViewBridgeInvoker iWebViewBridgeInvoker, ShowOperationState showOperationState) {
        getMetricSender().sendMetricWithInitState(AdOperationMetric.newAdShowStart());
        showOperationState.start();
        startShowTimeout(showOperationState);
        super.executeAdOperation(iWebViewBridgeInvoker, showOperationState);
    }

    private void startShowTimeout(final ShowOperationState showOperationState) {
        if (!this._useNewTimer) {
            this._executorService.submit(new Runnable() {
                public void run() {
                    if (!showOperationState.timeoutCV.block((long) showOperationState.configuration.getShowTimeout())) {
                        ShowModuleDecoratorTimeout showModuleDecoratorTimeout = ShowModuleDecoratorTimeout.this;
                        ShowOperationState showOperationState = showOperationState;
                        UnityAds.UnityAdsShowError unityAdsShowError = UnityAds.UnityAdsShowError.TIMEOUT;
                        showModuleDecoratorTimeout.onOperationTimeout(showOperationState, unityAdsShowError, ShowModuleDecoratorTimeout.errorMsgTimeout + showOperationState.placementId);
                    }
                }
            });
        } else if (showOperationState != null) {
            showOperationState.timeoutTimer = new BaseTimer(Integer.valueOf(showOperationState.configuration.getShowTimeout()), new ITimerListener() {
                public void onTimerFinished() {
                    ShowModuleDecoratorTimeout showModuleDecoratorTimeout = ShowModuleDecoratorTimeout.this;
                    ShowOperationState showOperationState = showOperationState;
                    UnityAds.UnityAdsShowError unityAdsShowError = UnityAds.UnityAdsShowError.TIMEOUT;
                    showModuleDecoratorTimeout.onOperationTimeout(showOperationState, unityAdsShowError, ShowModuleDecoratorTimeout.errorMsgTimeout + showOperationState.placementId);
                }
            });
            showOperationState.timeoutTimer.start(Executors.newSingleThreadScheduledExecutor());
        }
    }

    public void onUnityAdsShowConsent(String str) {
        releaseOperationTimeoutLock(str);
        super.onUnityAdsShowConsent(str);
    }

    public void onUnityAdsShowFailure(String str, UnityAds.UnityAdsShowError unityAdsShowError, String str2) {
        releaseOperationTimeoutLock(str);
        super.onUnityAdsShowFailure(str, unityAdsShowError, str2);
    }

    public void onUnityAdsShowStart(String str) {
        releaseOperationTimeoutLock(str);
        super.onUnityAdsShowStart(str);
    }

    private void releaseOperationTimeoutLock(String str) {
        ShowOperationState showOperationState;
        IShowOperation iShowOperation = get(str);
        if (iShowOperation != null && (showOperationState = iShowOperation.getShowOperationState()) != null) {
            if (this._useNewTimer) {
                BaseTimer baseTimer = showOperationState.timeoutTimer;
                if (baseTimer != null) {
                    baseTimer.kill();
                }
            } else if (iShowOperation.getShowOperationState().timeoutCV != null) {
                iShowOperation.getShowOperationState().timeoutCV.open();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onOperationTimeout(ShowOperationState showOperationState, UnityAds.UnityAdsShowError unityAdsShowError, String str) {
        if (showOperationState != null) {
            getMetricSender().sendMetricWithInitState(AdOperationMetric.newAdShowFailure(AdOperationError.timeout, Long.valueOf(showOperationState.duration())));
            remove(showOperationState.f339id);
            showOperationState.onUnityAdsShowFailure(unityAdsShowError, str);
        }
    }
}
