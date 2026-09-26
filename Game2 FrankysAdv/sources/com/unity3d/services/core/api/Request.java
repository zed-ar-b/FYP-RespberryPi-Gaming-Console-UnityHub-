package com.unity3d.services.core.api;

import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.request.IWebRequestListener;
import com.unity3d.services.core.request.WebRequest;
import com.unity3d.services.core.request.WebRequestError;
import com.unity3d.services.core.request.WebRequestEvent;
import com.unity3d.services.core.request.WebRequestThread;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

public class Request {
    @WebViewExposed
    public static void get(final String str, String str2, JSONArray jSONArray, Integer num, Integer num2, WebViewCallback webViewCallback) {
        if (jSONArray != null && jSONArray.length() == 0) {
            jSONArray = null;
        }
        try {
            WebRequestThread.request(str2, WebRequest.RequestType.GET, getHeadersMap(jSONArray), (String) null, num, num2, new IWebRequestListener() {
                public void onComplete(String str, String str2, int i, Map<String, List<String>> map) {
                    try {
                        JSONArray responseHeadersMap = Request.getResponseHeadersMap(map);
                        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.REQUEST, WebRequestEvent.COMPLETE, str, str, str2, Integer.valueOf(i), responseHeadersMap);
                    } catch (Exception e) {
                        DeviceLog.exception("Error parsing response headers", e);
                        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.REQUEST, WebRequestEvent.FAILED, str, str, "Error parsing response headers");
                    }
                }

                public void onFailed(String str, String str2) {
                    WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.REQUEST, WebRequestEvent.FAILED, str, str, str2);
                }
            });
            webViewCallback.invoke(str);
        } catch (Exception e) {
            DeviceLog.exception("Error mapping headers for the request", e);
            webViewCallback.error(WebRequestError.MAPPING_HEADERS_FAILED, str);
        }
    }

    @WebViewExposed
    public static void post(final String str, String str2, String str3, JSONArray jSONArray, Integer num, Integer num2, WebViewCallback webViewCallback) {
        String str4 = (str3 == null || str3.length() != 0) ? str3 : null;
        if (jSONArray != null && jSONArray.length() == 0) {
            jSONArray = null;
        }
        try {
            WebRequestThread.request(str2, WebRequest.RequestType.POST, getHeadersMap(jSONArray), str4, num, num2, new IWebRequestListener() {
                public void onComplete(String str, String str2, int i, Map<String, List<String>> map) {
                    try {
                        JSONArray responseHeadersMap = Request.getResponseHeadersMap(map);
                        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.REQUEST, WebRequestEvent.COMPLETE, str, str, str2, Integer.valueOf(i), responseHeadersMap);
                    } catch (Exception e) {
                        DeviceLog.exception("Error parsing response headers", e);
                        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.REQUEST, WebRequestEvent.FAILED, str, str, "Error parsing response headers");
                    }
                }

                public void onFailed(String str, String str2) {
                    WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.REQUEST, WebRequestEvent.FAILED, str, str, str2);
                }
            });
            webViewCallback.invoke(str);
        } catch (Exception e) {
            DeviceLog.exception("Error mapping headers for the request", e);
            webViewCallback.error(WebRequestError.MAPPING_HEADERS_FAILED, str);
        }
    }

    @WebViewExposed
    public static void head(final String str, String str2, JSONArray jSONArray, Integer num, Integer num2, WebViewCallback webViewCallback) {
        if (jSONArray != null && jSONArray.length() == 0) {
            jSONArray = null;
        }
        try {
            WebRequestThread.request(str2, WebRequest.RequestType.HEAD, getHeadersMap(jSONArray), num, num2, new IWebRequestListener() {
                public void onComplete(String str, String str2, int i, Map<String, List<String>> map) {
                    try {
                        JSONArray responseHeadersMap = Request.getResponseHeadersMap(map);
                        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.REQUEST, WebRequestEvent.COMPLETE, str, str, str2, Integer.valueOf(i), responseHeadersMap);
                    } catch (Exception e) {
                        DeviceLog.exception("Error parsing response headers", e);
                        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.REQUEST, WebRequestEvent.FAILED, str, str, "Error parsing response headers");
                    }
                }

                public void onFailed(String str, String str2) {
                    WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.REQUEST, WebRequestEvent.FAILED, str, str, str2);
                }
            });
            webViewCallback.invoke(str);
        } catch (Exception e) {
            DeviceLog.exception("Error mapping headers for the request", e);
            webViewCallback.error(WebRequestError.MAPPING_HEADERS_FAILED, str);
        }
    }

    @WebViewExposed
    public static void setConcurrentRequestCount(Integer num, WebViewCallback webViewCallback) {
        WebRequestThread.setConcurrentRequestCount(num.intValue());
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void setMaximumPoolSize(Integer num, WebViewCallback webViewCallback) {
        WebRequestThread.setMaximumPoolSize(num.intValue());
        webViewCallback.invoke(new Object[0]);
    }

    @WebViewExposed
    public static void setKeepAliveTime(Integer num, WebViewCallback webViewCallback) {
        WebRequestThread.setKeepAliveTime(num.longValue());
        webViewCallback.invoke(new Object[0]);
    }

    public static JSONArray getResponseHeadersMap(Map<String, List<String>> map) {
        JSONArray jSONArray = new JSONArray();
        if (map != null && map.size() > 0) {
            for (String next : map.keySet()) {
                JSONArray jSONArray2 = null;
                for (String put : map.get(next)) {
                    JSONArray jSONArray3 = new JSONArray();
                    jSONArray3.put(next);
                    jSONArray3.put(put);
                    jSONArray2 = jSONArray3;
                }
                jSONArray.put(jSONArray2);
            }
        }
        return jSONArray;
    }

    public static HashMap<String, List<String>> getHeadersMap(JSONArray jSONArray) throws JSONException {
        if (jSONArray == null) {
            return null;
        }
        HashMap<String, List<String>> hashMap = new HashMap<>();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONArray jSONArray2 = (JSONArray) jSONArray.get(i);
            List list = hashMap.get(jSONArray2.getString(0));
            if (list == null) {
                list = new ArrayList();
            }
            list.add(jSONArray2.getString(1));
            hashMap.put(jSONArray2.getString(0), list);
        }
        return hashMap;
    }
}
