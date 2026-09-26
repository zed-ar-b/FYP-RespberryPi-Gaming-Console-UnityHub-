package com.unity3d.services.ads.operation.show;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.ads.operation.AdModule;
import com.unity3d.services.core.configuration.ConfigurationReader;
import com.unity3d.services.core.device.Device;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.request.metrics.AdOperationError;
import com.unity3d.services.core.request.metrics.AdOperationMetric;
import com.unity3d.services.core.request.metrics.ISDKMetrics;
import com.unity3d.services.core.request.metrics.SDKMetrics;
import com.unity3d.services.core.webview.bridge.CallbackStatus;
import com.unity3d.services.core.webview.bridge.IWebViewBridgeInvoker;
import com.unity3d.services.core.webview.bridge.invocation.IWebViewBridgeInvocationCallback;
import com.unity3d.services.core.webview.bridge.invocation.WebViewBridgeInvocation;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowModule extends AdModule<IShowOperation, ShowOperationState> implements IShowModule {
    public static String errorMsgPlacementIdNull = "[UnityAds] Placement ID cannot be null";
    private static IShowModule instance;

    public static IShowModule getInstance() {
        if (instance == null) {
            instance = new ShowModuleDecoratorTimeout(new ShowModule(SDKMetrics.getInstance()), new ConfigurationReader());
        }
        return instance;
    }

    public ShowModule(ISDKMetrics iSDKMetrics) {
        super(iSDKMetrics);
    }

    public void executeAdOperation(IWebViewBridgeInvoker iWebViewBridgeInvoker, final ShowOperationState showOperationState) {
        if (TextUtils.isEmpty(showOperationState.placementId)) {
            sendOnUnityAdsFailedToShow(showOperationState, errorMsgPlacementIdNull, UnityAds.UnityAdsShowError.INVALID_ARGUMENT, true);
            return;
        }
        ShowOperation showOperation = new ShowOperation(showOperationState, new WebViewBridgeInvocation(this._executorService, iWebViewBridgeInvoker, new IWebViewBridgeInvocationCallback() {
            public void onSuccess() {
            }

            public void onFailure(String str, CallbackStatus callbackStatus) {
                ShowModule.this.getMetricSender().sendMetricWithInitState(AdOperationMetric.newAdShowFailure(AdOperationError.callback_error, Long.valueOf(showOperationState.duration())));
                ShowModule.this.sendOnUnityAdsFailedToShow(showOperationState, str, UnityAds.UnityAdsShowError.INTERNAL_ERROR, false);
                ShowModule.this.remove(showOperationState.f339id);
            }

            public void onTimeout() {
                ShowModule.this.getMetricSender().sendMetricWithInitState(AdOperationMetric.newAdShowFailure(AdOperationError.callback_timeout, Long.valueOf(showOperationState.duration())));
                ShowModule.this.sendOnUnityAdsFailedToShow(showOperationState, "[UnityAds] Show Invocation Timeout", UnityAds.UnityAdsShowError.INTERNAL_ERROR, false);
                ShowModule.this.remove(showOperationState.f339id);
            }
        }));
        ClientProperties.setActivity(showOperationState.activity);
        Activity activity = showOperationState.activity;
        Activity activity2 = showOperationState.activity;
        Display defaultDisplay = ((WindowManager) activity.getSystemService("window")).getDefaultDisplay();
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        JSONObject jSONObject3 = new JSONObject();
        try {
            jSONObject3.put("requestedOrientation", showOperationState.activity.getRequestedOrientation());
            jSONObject3.put("rotation", defaultDisplay.getRotation());
            if (Build.VERSION.SDK_INT >= 13) {
                Point point = new Point();
                defaultDisplay.getSize(point);
                jSONObject3.put("width", point.x);
                jSONObject3.put("height", point.y);
            } else {
                jSONObject3.put("width", defaultDisplay.getWidth());
                jSONObject3.put("height", defaultDisplay.getHeight());
            }
            jSONObject2.put("display", jSONObject3);
            jSONObject2.put("headerBiddingOptions", showOperationState.showOptions.getData());
            jSONObject.put("options", jSONObject2);
            jSONObject.put("listenerId", showOperation.getId());
            jSONObject.put("placementId", showOperationState.placementId);
            jSONObject.put("time", Device.getElapsedRealtime());
            set(showOperation);
            showOperation.invoke(showOperationState.configuration.getWebViewBridgeTimeout(), jSONObject);
        } catch (JSONException unused) {
            sendOnUnityAdsFailedToShow(showOperationState, "[UnityAds] Error creating show options", UnityAds.UnityAdsShowError.INTERNAL_ERROR, true);
        } catch (NullPointerException unused2) {
            sendOnUnityAdsFailedToShow(showOperationState, "[UnityAds] Error creating show options", UnityAds.UnityAdsShowError.INTERNAL_ERROR, true);
        }
    }

    public void onUnityAdsShowFailure(String str, UnityAds.UnityAdsShowError unityAdsShowError, String str2) {
        IShowOperation iShowOperation = (IShowOperation) get(str);
        if (iShowOperation != null && iShowOperation.getShowOperationState() != null) {
            ShowOperationState showOperationState = iShowOperation.getShowOperationState();
            getMetricSender().sendMetricWithInitState(AdOperationMetric.newAdShowFailure(unityAdsShowError, Long.valueOf(showOperationState.duration())));
            iShowOperation.onUnityAdsShowFailure(showOperationState.placementId, unityAdsShowError, str2);
            remove(str);
        }
    }

    public void onUnityAdsShowConsent(String str) {
        IShowOperation iShowOperation = (IShowOperation) get(str);
        if (iShowOperation == null || iShowOperation.getShowOperationState() == null) {
        }
    }

    public void onUnityAdsShowStart(String str) {
        IShowOperation iShowOperation = (IShowOperation) get(str);
        if (iShowOperation != null && iShowOperation.getShowOperationState() != null) {
            iShowOperation.onUnityAdsShowStart(iShowOperation.getShowOperationState().placementId);
        }
    }

    public void onUnityAdsShowClick(String str) {
        IShowOperation iShowOperation = (IShowOperation) get(str);
        if (iShowOperation != null && iShowOperation.getShowOperationState() != null) {
            iShowOperation.onUnityAdsShowClick(iShowOperation.getShowOperationState().placementId);
        }
    }

    public void onUnityAdsShowComplete(String str, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
        IShowOperation iShowOperation = (IShowOperation) get(str);
        if (iShowOperation != null && iShowOperation.getShowOperationState() != null) {
            ShowOperationState showOperationState = iShowOperation.getShowOperationState();
            getMetricSender().sendMetricWithInitState(AdOperationMetric.newAdShowSuccess(Long.valueOf(showOperationState.duration())));
            iShowOperation.onUnityAdsShowComplete(showOperationState.placementId, unityAdsShowCompletionState);
            remove(str);
        }
    }

    /* access modifiers changed from: private */
    public void sendOnUnityAdsFailedToShow(final ShowOperationState showOperationState, final String str, final UnityAds.UnityAdsShowError unityAdsShowError, boolean z) {
        if (showOperationState != null && showOperationState.listener != null) {
            if (z) {
                getMetricSender().sendMetricWithInitState(AdOperationMetric.newAdShowFailure(unityAdsShowError, Long.valueOf(showOperationState.duration())));
            }
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    showOperationState.onUnityAdsShowFailure(unityAdsShowError, str);
                }
            });
        }
    }
}
