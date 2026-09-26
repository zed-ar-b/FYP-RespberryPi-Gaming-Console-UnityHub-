package com.unity3d.services.ads.api;

import android.content.Intent;
import android.os.Build;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.WindowInsets;
import com.unity3d.services.ads.adunit.AdUnitActivity;
import com.unity3d.services.ads.adunit.AdUnitError;
import com.unity3d.services.ads.adunit.AdUnitMotionEvent;
import com.unity3d.services.ads.adunit.AdUnitSoftwareActivity;
import com.unity3d.services.ads.adunit.AdUnitTransparentActivity;
import com.unity3d.services.ads.adunit.AdUnitTransparentSoftwareActivity;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.Utilities;
import com.unity3d.services.core.properties.ClientProperties;
import com.unity3d.services.core.webview.bridge.WebViewCallback;
import com.unity3d.services.core.webview.bridge.WebViewExposed;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdUnit {
    private static AdUnitActivity _adUnitActivity = null;
    private static int _currentActivityId = -1;

    private AdUnit() {
    }

    public static void setAdUnitActivity(AdUnitActivity adUnitActivity) {
        _adUnitActivity = adUnitActivity;
    }

    public static AdUnitActivity getAdUnitActivity() {
        return _adUnitActivity;
    }

    public static int getCurrentAdUnitActivityId() {
        return _currentActivityId;
    }

    public static void setCurrentAdUnitActivityId(int i) {
        _currentActivityId = i;
    }

    @WebViewExposed
    public static void open(Integer num, JSONArray jSONArray, Integer num2, WebViewCallback webViewCallback) {
        open(num, jSONArray, num2, (JSONArray) null, webViewCallback);
    }

    @WebViewExposed
    public static void open(Integer num, JSONArray jSONArray, Integer num2, JSONArray jSONArray2, WebViewCallback webViewCallback) {
        open(num, jSONArray, num2, jSONArray2, 0, true, webViewCallback);
    }

    @WebViewExposed
    public static void open(Integer num, JSONArray jSONArray, Integer num2, JSONArray jSONArray2, Integer num3, Boolean bool, WebViewCallback webViewCallback) {
        open(num, jSONArray, num2, jSONArray2, num3, bool, false, webViewCallback);
    }

    @WebViewExposed
    public static void open(Integer num, JSONArray jSONArray, Integer num2, JSONArray jSONArray2, Integer num3, Boolean bool, Boolean bool2, WebViewCallback webViewCallback) {
        open(num, jSONArray, num2, jSONArray2, num3, bool, bool2, 0, webViewCallback);
    }

    @WebViewExposed
    public static void open(Integer num, JSONArray jSONArray, Integer num2, JSONArray jSONArray2, Integer num3, Boolean bool, Boolean bool2, Integer num4, WebViewCallback webViewCallback) {
        Intent intent;
        if (!bool.booleanValue() && bool2.booleanValue()) {
            DeviceLog.debug("Unity Ads opening new transparent ad unit activity, hardware acceleration disabled");
            intent = new Intent(ClientProperties.getActivity(), AdUnitTransparentSoftwareActivity.class);
        } else if (bool.booleanValue() && !bool2.booleanValue()) {
            DeviceLog.debug("Unity Ads opening new hardware accelerated ad unit activity");
            intent = new Intent(ClientProperties.getActivity(), AdUnitActivity.class);
        } else if (!bool.booleanValue() || !bool2.booleanValue()) {
            DeviceLog.debug("Unity Ads opening new ad unit activity, hardware acceleration disabled");
            intent = new Intent(ClientProperties.getActivity(), AdUnitSoftwareActivity.class);
        } else {
            DeviceLog.debug("Unity Ads opening new hardware accelerated transparent ad unit activity");
            intent = new Intent(ClientProperties.getActivity(), AdUnitTransparentActivity.class);
        }
        intent.addFlags(268500992);
        if (num != null) {
            try {
                intent.putExtra(AdUnitActivity.EXTRA_ACTIVITY_ID, num.intValue());
                setCurrentAdUnitActivityId(num.intValue());
                try {
                    intent.putExtra(AdUnitActivity.EXTRA_VIEWS, getViewList(jSONArray));
                    if (jSONArray2 != null) {
                        try {
                            intent.putExtra(AdUnitActivity.EXTRA_KEY_EVENT_LIST, getKeyEventList(jSONArray2));
                        } catch (Exception e) {
                            DeviceLog.exception("Error parsing views from viewList", e);
                            webViewCallback.error(AdUnitError.CORRUPTED_KEYEVENTLIST, jSONArray2, e.getMessage());
                            return;
                        }
                    }
                    intent.putExtra(AdUnitActivity.EXTRA_SYSTEM_UI_VISIBILITY, num3);
                    intent.putExtra(AdUnitActivity.EXTRA_ORIENTATION, num2);
                    intent.putExtra(AdUnitActivity.EXTRA_DISPLAY_CUTOUT_MODE, num4);
                    ClientProperties.getActivity().startActivity(intent);
                    DeviceLog.debug("Opened AdUnitActivity with: " + jSONArray.toString());
                    webViewCallback.invoke(new Object[0]);
                } catch (Exception e2) {
                    DeviceLog.exception("Error parsing views from viewList", e2);
                    webViewCallback.error(AdUnitError.CORRUPTED_VIEWLIST, jSONArray, e2.getMessage());
                }
            } catch (Exception e3) {
                DeviceLog.exception("Could not set activityId for intent", e3);
                webViewCallback.error(AdUnitError.ACTIVITY_ID, Integer.valueOf(num.intValue()), e3.getMessage());
            }
        } else {
            DeviceLog.error("Activity ID is NULL");
            webViewCallback.error(AdUnitError.ACTIVITY_ID, "Activity ID NULL");
        }
    }

    @WebViewExposed
    public static void close(WebViewCallback webViewCallback) {
        if (getAdUnitActivity() != null) {
            getAdUnitActivity().finish();
            webViewCallback.invoke(new Object[0]);
            return;
        }
        webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void setViews(final JSONArray jSONArray, WebViewCallback webViewCallback) {
        boolean z;
        try {
            getViewList(jSONArray);
            z = false;
        } catch (JSONException unused) {
            webViewCallback.error(AdUnitError.CORRUPTED_VIEWLIST, jSONArray);
            z = true;
        }
        if (!z) {
            Utilities.runOnUiThread(new Runnable() {
                public void run() {
                    if (AdUnit.getAdUnitActivity() != null) {
                        try {
                            AdUnit.getAdUnitActivity().setViews(AdUnit.getViewList(jSONArray));
                        } catch (Exception e) {
                            DeviceLog.exception("Corrupted viewlist", e);
                        }
                    }
                }
            });
        }
        if (getAdUnitActivity() != null) {
            webViewCallback.invoke(jSONArray);
            return;
        }
        webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void getViews(WebViewCallback webViewCallback) {
        if (getAdUnitActivity() != null) {
            webViewCallback.invoke(new JSONArray(Arrays.asList(getAdUnitActivity().getViews())));
            return;
        }
        webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void setOrientation(final Integer num, WebViewCallback webViewCallback) {
        Utilities.runOnUiThread(new Runnable() {
            public void run() {
                if (AdUnit.getAdUnitActivity() != null) {
                    AdUnit.getAdUnitActivity().setOrientation(num.intValue());
                }
            }
        });
        if (getAdUnitActivity() != null) {
            webViewCallback.invoke(num);
            return;
        }
        webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void getOrientation(WebViewCallback webViewCallback) {
        if (getAdUnitActivity() != null) {
            webViewCallback.invoke(Integer.valueOf(getAdUnitActivity().getRequestedOrientation()));
            return;
        }
        webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void setKeepScreenOn(final Boolean bool, WebViewCallback webViewCallback) {
        Utilities.runOnUiThread(new Runnable() {
            public void run() {
                if (AdUnit.getAdUnitActivity() != null) {
                    AdUnit.getAdUnitActivity().setKeepScreenOn(bool.booleanValue());
                }
            }
        });
        if (getAdUnitActivity() != null) {
            webViewCallback.invoke(new Object[0]);
        } else {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        }
    }

    @WebViewExposed
    public static void setSystemUiVisibility(final Integer num, WebViewCallback webViewCallback) {
        Utilities.runOnUiThread(new Runnable() {
            public void run() {
                if (AdUnit.getAdUnitActivity() != null) {
                    AdUnit.getAdUnitActivity().setSystemUiVisibility(num.intValue());
                }
            }
        });
        if (getAdUnitActivity() != null) {
            webViewCallback.invoke(num);
            return;
        }
        webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
    }

    @WebViewExposed
    public static void setKeyEventList(JSONArray jSONArray, WebViewCallback webViewCallback) {
        if (getAdUnitActivity() != null) {
            try {
                getAdUnitActivity().setKeyEventList(getKeyEventList(jSONArray));
                webViewCallback.invoke(jSONArray);
            } catch (Exception e) {
                DeviceLog.exception("Error parsing views from viewList", e);
                webViewCallback.error(AdUnitError.CORRUPTED_KEYEVENTLIST, jSONArray, e.getMessage());
            }
        } else {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        }
    }

    @WebViewExposed
    public static void setViewFrame(String str, Integer num, Integer num2, Integer num3, Integer num4, WebViewCallback webViewCallback) {
        final String str2 = str;
        final Integer num5 = num;
        final Integer num6 = num2;
        final Integer num7 = num3;
        final Integer num8 = num4;
        Utilities.runOnUiThread(new Runnable() {
            public void run() {
                if (AdUnit.getAdUnitActivity() != null) {
                    AdUnit.getAdUnitActivity().setViewFrame(str2, num5.intValue(), num6.intValue(), num7.intValue(), num8.intValue());
                }
            }
        });
        if (getAdUnitActivity() != null) {
            webViewCallback.invoke(new Object[0]);
        } else {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        }
    }

    @WebViewExposed
    public static void getViewFrame(String str, WebViewCallback webViewCallback) {
        if (getAdUnitActivity() == null) {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        } else if (getAdUnitActivity().getViewFrame(str) != null) {
            Map<String, Integer> viewFrame = getAdUnitActivity().getViewFrame(str);
            webViewCallback.invoke(viewFrame.get("x"), viewFrame.get("y"), viewFrame.get("width"), viewFrame.get("height"));
        } else {
            webViewCallback.error(AdUnitError.UNKNOWN_VIEW, new Object[0]);
        }
    }

    @WebViewExposed
    public static void startMotionEventCapture(Integer num, WebViewCallback webViewCallback) {
        if (getAdUnitActivity() == null) {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        } else if (getAdUnitActivity().getLayout() != null) {
            getAdUnitActivity().getLayout().startCapture(num.intValue());
            webViewCallback.invoke(new Object[0]);
        } else {
            webViewCallback.error(AdUnitError.LAYOUT_NULL, new Object[0]);
        }
    }

    @WebViewExposed
    public static void endMotionEventCapture(WebViewCallback webViewCallback) {
        if (getAdUnitActivity() == null) {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        } else if (getAdUnitActivity().getLayout() != null) {
            getAdUnitActivity().getLayout().endCapture();
            webViewCallback.invoke(new Object[0]);
        } else {
            webViewCallback.error(AdUnitError.LAYOUT_NULL, new Object[0]);
        }
    }

    @WebViewExposed
    public static void clearMotionEventCapture(WebViewCallback webViewCallback) {
        if (getAdUnitActivity() == null) {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        } else if (getAdUnitActivity().getLayout() != null) {
            getAdUnitActivity().getLayout().clearCapture();
            webViewCallback.invoke(new Object[0]);
        } else {
            webViewCallback.error(AdUnitError.LAYOUT_NULL, new Object[0]);
        }
    }

    @WebViewExposed
    public static void getMotionEventCount(JSONArray jSONArray, WebViewCallback webViewCallback) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                arrayList.add(Integer.valueOf(jSONArray.getInt(i)));
            } catch (Exception e) {
                DeviceLog.exception("Error retrieving int from eventTypes", e);
            }
        }
        if (getAdUnitActivity() == null) {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        } else if (getAdUnitActivity().getLayout() == null) {
            webViewCallback.error(AdUnitError.LAYOUT_NULL, new Object[0]);
        } else if (getAdUnitActivity().getLayout().getCurrentEventCount() >= getAdUnitActivity().getLayout().getMaxEventCount()) {
            webViewCallback.error(AdUnitError.MAX_MOTION_EVENT_COUNT_REACHED, new Object[0]);
        } else {
            SparseIntArray eventCount = getAdUnitActivity().getLayout().getEventCount(arrayList);
            JSONObject jSONObject = new JSONObject();
            for (int i2 = 0; i2 < eventCount.size(); i2++) {
                int keyAt = eventCount.keyAt(i2);
                try {
                    jSONObject.put(Integer.toString(keyAt), eventCount.get(keyAt));
                } catch (Exception e2) {
                    DeviceLog.exception("Error building response JSON", e2);
                }
            }
            webViewCallback.invoke(jSONObject);
        }
    }

    @WebViewExposed
    public static void getMotionEventData(JSONObject jSONObject, WebViewCallback webViewCallback) {
        Iterator<String> keys = jSONObject.keys();
        SparseArray sparseArray = new SparseArray();
        while (true) {
            if (!keys.hasNext()) {
                break;
            }
            String next = keys.next();
            int parseInt = Integer.parseInt(next);
            if (sparseArray.get(parseInt) == null) {
                sparseArray.put(parseInt, new ArrayList());
            }
            JSONArray jSONArray = null;
            try {
                jSONArray = jSONObject.getJSONArray(next);
            } catch (Exception e) {
                DeviceLog.exception("Couldn't fetch keyIndices", e);
            }
            if (jSONArray != null) {
                for (int i = 0; i < jSONArray.length(); i++) {
                    try {
                        ((ArrayList) sparseArray.get(parseInt)).add(Integer.valueOf(jSONArray.getInt(i)));
                    } catch (Exception e2) {
                        DeviceLog.exception("Couldn't add value to requested infos", e2);
                    }
                }
            }
        }
        if (getAdUnitActivity() == null) {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        } else if (getAdUnitActivity().getLayout() == null) {
            webViewCallback.error(AdUnitError.LAYOUT_NULL, new Object[0]);
        } else if (getAdUnitActivity().getLayout().getCurrentEventCount() >= getAdUnitActivity().getLayout().getMaxEventCount()) {
            webViewCallback.error(AdUnitError.MAX_MOTION_EVENT_COUNT_REACHED, new Object[0]);
        } else {
            SparseArray<SparseArray<AdUnitMotionEvent>> events = getAdUnitActivity().getLayout().getEvents(sparseArray);
            JSONObject jSONObject2 = new JSONObject();
            for (int i2 = 0; i2 < events.size(); i2++) {
                int keyAt = events.keyAt(i2);
                SparseArray sparseArray2 = events.get(keyAt);
                JSONObject jSONObject3 = new JSONObject();
                for (int i3 = 0; i3 < sparseArray2.size(); i3++) {
                    JSONObject jSONObject4 = new JSONObject();
                    int keyAt2 = sparseArray2.keyAt(i3);
                    AdUnitMotionEvent adUnitMotionEvent = (AdUnitMotionEvent) sparseArray2.get(keyAt2);
                    try {
                        jSONObject4.put("action", adUnitMotionEvent.getAction());
                        jSONObject4.put("isObscured", adUnitMotionEvent.isObscured());
                        jSONObject4.put("toolType", adUnitMotionEvent.getToolType());
                        jSONObject4.put("source", adUnitMotionEvent.getSource());
                        jSONObject4.put("deviceId", adUnitMotionEvent.getDeviceId());
                        jSONObject4.put("x", (double) adUnitMotionEvent.getX());
                        jSONObject4.put("y", (double) adUnitMotionEvent.getY());
                        jSONObject4.put("eventTime", adUnitMotionEvent.getEventTime());
                        jSONObject4.put("pressure", (double) adUnitMotionEvent.getPressure());
                        jSONObject4.put("size", (double) adUnitMotionEvent.getSize());
                        jSONObject3.put(Integer.toString(keyAt2), jSONObject4);
                    } catch (Exception e3) {
                        DeviceLog.debug("Couldn't construct event info", e3);
                    }
                }
                try {
                    jSONObject2.put(Integer.toString(keyAt), jSONObject3);
                } catch (Exception e4) {
                    DeviceLog.debug("Couldn't construct info object", e4);
                }
            }
            webViewCallback.invoke(jSONObject2);
        }
    }

    @WebViewExposed
    public static void getCurrentMotionEventCount(WebViewCallback webViewCallback) {
        if (getAdUnitActivity() == null) {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        } else if (getAdUnitActivity().getLayout() != null) {
            webViewCallback.invoke(Integer.valueOf(getAdUnitActivity().getLayout().getCurrentEventCount()));
        } else {
            webViewCallback.error(AdUnitError.LAYOUT_NULL, new Object[0]);
        }
    }

    @WebViewExposed
    public static void getSafeAreaInsets(WebViewCallback webViewCallback) {
        if (getAdUnitActivity() == null || getAdUnitActivity().getLayout() == null) {
            webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
        } else if (Build.VERSION.SDK_INT >= 28) {
            WindowInsets rootWindowInsets = getAdUnitActivity().getLayout().getRootWindowInsets();
            if (rootWindowInsets != null) {
                JSONObject jSONObject = new JSONObject();
                try {
                    Object invoke = rootWindowInsets.getClass().getMethod("getDisplayCutout", new Class[0]).invoke(rootWindowInsets, new Object[0]);
                    if (invoke != null) {
                        Object invoke2 = invoke.getClass().getMethod("getSafeInsetTop", new Class[0]).invoke(invoke, new Object[0]);
                        Object invoke3 = invoke.getClass().getMethod("getSafeInsetRight", new Class[0]).invoke(invoke, new Object[0]);
                        Object invoke4 = invoke.getClass().getMethod("getSafeInsetBottom", new Class[0]).invoke(invoke, new Object[0]);
                        Object invoke5 = invoke.getClass().getMethod("getSafeInsetLeft", new Class[0]).invoke(invoke, new Object[0]);
                        jSONObject.put("top", invoke2);
                        jSONObject.put("right", invoke3);
                        jSONObject.put("bottom", invoke4);
                        jSONObject.put("left", invoke5);
                        webViewCallback.invoke(jSONObject);
                        return;
                    }
                    webViewCallback.error(AdUnitError.NO_DISPLAY_CUTOUT_AVAILABLE, new Object[0]);
                } catch (NoSuchMethodException e) {
                    webViewCallback.error(AdUnitError.DISPLAY_CUTOUT_METHOD_NOT_AVAILABLE, new Object[0]);
                    DeviceLog.debug("Method getDisplayCutout not found", e);
                } catch (IllegalAccessException | InvocationTargetException e2) {
                    webViewCallback.error(AdUnitError.DISPLAY_CUTOUT_INVOKE_FAILED, new Object[0]);
                    DeviceLog.debug("Error while calling displayCutout getter", e2);
                } catch (JSONException e3) {
                    webViewCallback.error(AdUnitError.DISPLAY_CUTOUT_JSON_ERROR, new Object[0]);
                    DeviceLog.debug("JSON error while constructing display cutout object", e3);
                }
            } else {
                webViewCallback.error(AdUnitError.NO_DISPLAY_CUTOUT_AVAILABLE, new Object[0]);
            }
        } else {
            webViewCallback.error(AdUnitError.API_LEVEL_ERROR, new Object[0]);
        }
    }

    @WebViewExposed
    public static void setLayoutInDisplayCutoutMode(final Integer num, WebViewCallback webViewCallback) {
        Utilities.runOnUiThread(new Runnable() {
            public void run() {
                if (AdUnit.getAdUnitActivity() != null) {
                    AdUnit.getAdUnitActivity().setLayoutInDisplayCutoutMode(num.intValue());
                }
            }
        });
        if (getAdUnitActivity() != null) {
            webViewCallback.invoke(num);
            return;
        }
        webViewCallback.error(AdUnitError.ADUNIT_NULL, new Object[0]);
    }

    /* access modifiers changed from: private */
    public static String[] getViewList(JSONArray jSONArray) throws JSONException {
        String[] strArr = new String[jSONArray.length()];
        for (int i = 0; i < jSONArray.length(); i++) {
            strArr[i] = jSONArray.getString(i);
        }
        return strArr;
    }

    private static ArrayList<Integer> getKeyEventList(JSONArray jSONArray) throws JSONException {
        ArrayList<Integer> arrayList = new ArrayList<>();
        int i = 0;
        while (true) {
            Integer valueOf = Integer.valueOf(i);
            if (valueOf.intValue() >= jSONArray.length()) {
                return arrayList;
            }
            arrayList.add(Integer.valueOf(jSONArray.getInt(valueOf.intValue())));
            i = valueOf.intValue() + 1;
        }
    }
}
