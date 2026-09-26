package com.unity3d.services.core.webview.bridge.invocation;

import android.os.ConditionVariable;
import com.unity3d.services.core.webview.bridge.CallbackStatus;
import com.unity3d.services.core.webview.bridge.IWebViewBridgeInvoker;
import java.lang.reflect.Method;

public class WebViewBridgeInvocationRunnable implements Runnable {
    private static CallbackStatus _callbackStatus;
    private static ConditionVariable _responseTimeout;
    private String _className;
    private IWebViewBridgeInvocationCallback _invocationCallback;
    private Object[] _invocationParameters;
    private String _methodName;
    private int _timeoutLengthInMilliSeconds;
    private Method _webViewBridgeCallbackMethod;
    private IWebViewBridgeInvoker _webViewBridgeInvoker;

    public WebViewBridgeInvocationRunnable(IWebViewBridgeInvocationCallback iWebViewBridgeInvocationCallback, IWebViewBridgeInvoker iWebViewBridgeInvoker, String str, String str2, int i, Object... objArr) {
        try {
            this._webViewBridgeCallbackMethod = WebViewBridgeInvocationRunnable.class.getMethod("onInvocationComplete", new Class[]{CallbackStatus.class});
            this._invocationCallback = iWebViewBridgeInvocationCallback;
            this._webViewBridgeInvoker = iWebViewBridgeInvoker;
            this._className = str;
            this._methodName = str2;
            this._timeoutLengthInMilliSeconds = i;
            this._invocationParameters = objArr;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("WebViewBridgeInvocation callback method cannot be found", e);
        }
    }

    public void run() {
        _callbackStatus = null;
        _responseTimeout = new ConditionVariable();
        boolean invokeMethod = this._webViewBridgeInvoker.invokeMethod(this._className, this._methodName, this._webViewBridgeCallbackMethod, this._invocationParameters);
        IWebViewBridgeInvocationCallback iWebViewBridgeInvocationCallback = this._invocationCallback;
        if (iWebViewBridgeInvocationCallback != null) {
            if (!invokeMethod) {
                iWebViewBridgeInvocationCallback.onFailure("WebViewBridgeInvocationRunnable:run: invokeMethod failure", (CallbackStatus) null);
            } else if (!_responseTimeout.block((long) this._timeoutLengthInMilliSeconds)) {
                this._invocationCallback.onTimeout();
            } else if (_callbackStatus == CallbackStatus.OK) {
                this._invocationCallback.onSuccess();
            } else {
                this._invocationCallback.onFailure("WebViewBridgeInvocationRunnable:run CallbackStatus.Error", _callbackStatus);
            }
        }
    }

    public static synchronized void onInvocationComplete(CallbackStatus callbackStatus) {
        synchronized (WebViewBridgeInvocationRunnable.class) {
            _callbackStatus = callbackStatus;
            if (_responseTimeout != null) {
                _responseTimeout.open();
            }
        }
    }
}
