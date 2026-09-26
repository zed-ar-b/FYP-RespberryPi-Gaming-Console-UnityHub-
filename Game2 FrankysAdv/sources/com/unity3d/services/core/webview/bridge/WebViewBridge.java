package com.unity3d.services.core.webview.bridge;

import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.webview.WebViewApp;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONException;

public class WebViewBridge {
    private static HashMap<String, HashMap<String, HashMap<Integer, Method>>> _classTable;

    public static void setClassTable(Class[] clsArr) {
        HashMap hashMap;
        if (clsArr != null) {
            _classTable = new HashMap<>();
            for (Class cls : clsArr) {
                if (cls != null && (cls.getPackage().getName().startsWith("com.unity3d.services") || cls.getPackage().getName().startsWith("com.unity3d.ads.test"))) {
                    HashMap hashMap2 = new HashMap();
                    for (Method method : cls.getMethods()) {
                        if (method.getAnnotation(WebViewExposed.class) != null) {
                            String name = method.getName();
                            if (hashMap2.containsKey(name)) {
                                hashMap = (HashMap) hashMap2.get(name);
                            } else {
                                hashMap = new HashMap();
                            }
                            hashMap.put(Integer.valueOf(Arrays.deepHashCode(method.getParameterTypes())), method);
                            hashMap2.put(name, hashMap);
                        }
                    }
                    _classTable.put(cls.getName(), hashMap2);
                }
            }
        }
    }

    private static Method findMethod(String str, String str2, Object[] objArr) throws JSONException, NoSuchMethodException {
        if (_classTable.containsKey(str)) {
            HashMap hashMap = _classTable.get(str);
            if (hashMap.containsKey(str2)) {
                return (Method) ((HashMap) hashMap.get(str2)).get(Integer.valueOf(Arrays.deepHashCode(getTypes(objArr))));
            }
            throw new NoSuchMethodException();
        }
        throw new NoSuchMethodException();
    }

    private static Class<?>[] getTypes(Object[] objArr) throws JSONException {
        Class[] clsArr = objArr == null ? new Class[1] : new Class[(objArr.length + 1)];
        if (objArr != null) {
            for (int i = 0; i < objArr.length; i++) {
                clsArr[i] = objArr[i].getClass();
            }
        }
        clsArr[clsArr.length - 1] = WebViewCallback.class;
        return clsArr;
    }

    private static Object[] getValues(Object[] objArr, WebViewCallback webViewCallback) throws JSONException {
        Object[] objArr2;
        if (objArr != null) {
            objArr2 = new Object[(objArr.length + (webViewCallback != null ? 1 : 0))];
        } else if (webViewCallback == null) {
            return null;
        } else {
            objArr2 = new Object[1];
        }
        if (objArr != null) {
            System.arraycopy(objArr, 0, objArr2, 0, objArr.length);
        }
        if (webViewCallback != null) {
            objArr2[objArr2.length - 1] = webViewCallback;
        }
        return objArr2;
    }

    public static void handleInvocation(String str, String str2, Object[] objArr, WebViewCallback webViewCallback) throws Exception {
        try {
            try {
                findMethod(str, str2, objArr).invoke((Object) null, getValues(objArr, webViewCallback));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | JSONException e) {
                webViewCallback.error(WebViewBridgeError.INVOCATION_FAILED, str, str2, objArr, e.getMessage());
                throw e;
            }
        } catch (NoSuchMethodException | JSONException e2) {
            webViewCallback.error(WebViewBridgeError.METHOD_NOT_FOUND, str, str2, objArr);
            throw e2;
        }
    }

    public static void handleCallback(String str, String str2, Object[] objArr) throws Exception {
        try {
            WebViewApp.getCurrentApp().getCallback(str).invoke(str2, getValues(objArr, (WebViewCallback) null));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | JSONException e) {
            DeviceLog.error("Error while invoking method");
            throw e;
        }
    }
}
