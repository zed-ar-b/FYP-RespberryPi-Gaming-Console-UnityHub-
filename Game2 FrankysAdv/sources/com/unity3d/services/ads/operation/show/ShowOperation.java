package com.unity3d.services.ads.operation.show;

import com.unity3d.ads.UnityAds;
import com.unity3d.services.ads.operation.AdOperation;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.webview.bridge.invocation.IWebViewBridgeInvocation;

public class ShowOperation extends AdOperation implements IShowOperation {
    /* access modifiers changed from: private */
    public ShowOperationState showOperationState;

    public ShowOperation(ShowOperationState showOperationState2, IWebViewBridgeInvocation iWebViewBridgeInvocation) {
        super(iWebViewBridgeInvocation, "show");
        this.showOperationState = showOperationState2;
    }

    public ShowOperationState getShowOperationState() {
        return this.showOperationState;
    }

    public void onUnityAdsShowFailure(String str, final UnityAds.UnityAdsShowError unityAdsShowError, final String str2) {
        ShowOperationState showOperationState2 = this.showOperationState;
        if (showOperationState2 != null && showOperationState2.listener != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    if (ShowOperation.this.showOperationState != null) {
                        ShowOperation.this.showOperationState.onUnityAdsShowFailure(unityAdsShowError, str2);
                    }
                }
            });
        }
    }

    public void onUnityAdsShowStart(final String str) {
        if (this.showOperationState != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    if (ShowOperation.this.showOperationState != null) {
                        ShowOperation.this.showOperationState.onUnityAdsShowStart(str);
                    }
                }
            });
        }
    }

    public void onUnityAdsShowClick(String str) {
        if (this.showOperationState != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    if (ShowOperation.this.showOperationState != null) {
                        ShowOperation.this.showOperationState.onUnityAdsShowClick();
                    }
                }
            });
        }
    }

    public void onUnityAdsShowComplete(String str, final UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
        if (this.showOperationState != null) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    if (ShowOperation.this.showOperationState != null) {
                        ShowOperation.this.showOperationState.onUnityAdsShowComplete(unityAdsShowCompletionState);
                    }
                }
            });
        }
    }

    public String getId() {
        return this.showOperationState.f339id;
    }
}
