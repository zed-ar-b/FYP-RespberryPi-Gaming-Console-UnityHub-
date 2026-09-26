package com.unity3d.services.ads.adunit;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.unity3d.services.ads.api.AdUnit;
import com.unity3d.services.ads.configuration.IAdsModuleConfiguration;
import com.unity3d.services.core.api.Intent;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.configuration.IModuleConfiguration;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.misc.ViewUtilities;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;

public class AdUnitActivity extends Activity {
    public static final String EXTRA_ACTIVITY_ID = "activityId";
    public static final String EXTRA_DISPLAY_CUTOUT_MODE = "displayCutoutMode";
    public static final String EXTRA_KEEP_SCREEN_ON = "keepScreenOn";
    public static final String EXTRA_KEY_EVENT_LIST = "keyEvents";
    public static final String EXTRA_ORIENTATION = "orientation";
    public static final String EXTRA_SYSTEM_UI_VISIBILITY = "systemUiVisibility";
    public static final String EXTRA_VIEWS = "views";
    private int _activityId;
    private int _displayCutoutMode;
    boolean _keepScreenOn;
    private ArrayList<Integer> _keyEventList;
    protected AdUnitRelativeLayout _layout;
    private int _orientation = -1;
    private int _systemUiVisibility;
    private Map<String, IAdUnitViewHandler> _viewHandlers;
    private String[] _views;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        AdUnitEvent adUnitEvent;
        super.onCreate(bundle);
        if (WebViewApp.getCurrentApp() == null) {
            DeviceLog.error("Unity Ads web app is null, closing Unity Ads activity from onCreate");
            finish();
            return;
        }
        AdUnit.setAdUnitActivity(this);
        Intent.setActiveActivity(this);
        createLayout();
        ViewUtilities.removeViewFromParent(this._layout);
        AdUnitRelativeLayout adUnitRelativeLayout = this._layout;
        addContentView(adUnitRelativeLayout, adUnitRelativeLayout.getLayoutParams());
        if (bundle == null) {
            this._views = getIntent().getStringArrayExtra(EXTRA_VIEWS);
            this._keyEventList = getIntent().getIntegerArrayListExtra(EXTRA_KEY_EVENT_LIST);
            if (getIntent().hasExtra(EXTRA_ORIENTATION)) {
                this._orientation = getIntent().getIntExtra(EXTRA_ORIENTATION, -1);
            }
            if (getIntent().hasExtra(EXTRA_SYSTEM_UI_VISIBILITY)) {
                this._systemUiVisibility = getIntent().getIntExtra(EXTRA_SYSTEM_UI_VISIBILITY, 0);
            }
            if (getIntent().hasExtra(EXTRA_ACTIVITY_ID)) {
                this._activityId = getIntent().getIntExtra(EXTRA_ACTIVITY_ID, -1);
            }
            if (getIntent().hasExtra(EXTRA_DISPLAY_CUTOUT_MODE)) {
                this._displayCutoutMode = getIntent().getIntExtra(EXTRA_DISPLAY_CUTOUT_MODE, 0);
            }
            adUnitEvent = AdUnitEvent.ON_CREATE;
        } else {
            this._views = bundle.getStringArray(EXTRA_VIEWS);
            this._orientation = bundle.getInt(EXTRA_ORIENTATION, -1);
            this._systemUiVisibility = bundle.getInt(EXTRA_SYSTEM_UI_VISIBILITY, 0);
            this._keyEventList = bundle.getIntegerArrayList(EXTRA_KEY_EVENT_LIST);
            this._keepScreenOn = bundle.getBoolean(EXTRA_KEEP_SCREEN_ON);
            this._activityId = bundle.getInt(EXTRA_ACTIVITY_ID, -1);
            this._displayCutoutMode = bundle.getInt(EXTRA_DISPLAY_CUTOUT_MODE, 0);
            setKeepScreenOn(this._keepScreenOn);
            adUnitEvent = AdUnitEvent.ON_RESTORE;
        }
        setOrientation(this._orientation);
        setSystemUiVisibility(this._systemUiVisibility);
        setLayoutInDisplayCutoutMode(this._displayCutoutMode);
        String[] strArr = this._views;
        if (strArr != null) {
            for (String viewHandler : strArr) {
                IAdUnitViewHandler viewHandler2 = getViewHandler(viewHandler);
                if (viewHandler2 != null) {
                    viewHandler2.onCreate(this, bundle);
                }
            }
        }
        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.ADUNIT, adUnitEvent, Integer.valueOf(this._activityId));
    }

    public AdUnitRelativeLayout getLayout() {
        return this._layout;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (WebViewApp.getCurrentApp() != null) {
            Map<String, IAdUnitViewHandler> map = this._viewHandlers;
            if (map != null) {
                for (Map.Entry next : map.entrySet()) {
                    if (next.getValue() != null) {
                        ((IAdUnitViewHandler) next.getValue()).onStart(this);
                    }
                }
            }
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.ADUNIT, AdUnitEvent.ON_START, Integer.valueOf(this._activityId));
        } else if (!isFinishing()) {
            DeviceLog.error("Unity Ads web app is null, closing Unity Ads activity from onStart");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        if (WebViewApp.getCurrentApp() != null) {
            Map<String, IAdUnitViewHandler> map = this._viewHandlers;
            if (map != null) {
                for (Map.Entry next : map.entrySet()) {
                    if (next.getValue() != null) {
                        ((IAdUnitViewHandler) next.getValue()).onStop(this);
                    }
                }
            }
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.ADUNIT, AdUnitEvent.ON_STOP, Integer.valueOf(this._activityId));
        } else if (!isFinishing()) {
            DeviceLog.error("Unity Ads web app is null, closing Unity Ads activity from onStop");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (WebViewApp.getCurrentApp() != null) {
            setViews(this._views);
            Map<String, IAdUnitViewHandler> map = this._viewHandlers;
            if (map != null) {
                for (Map.Entry next : map.entrySet()) {
                    if (next.getValue() != null) {
                        ((IAdUnitViewHandler) next.getValue()).onResume(this);
                    }
                }
            }
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.ADUNIT, AdUnitEvent.ON_RESUME, Integer.valueOf(this._activityId));
        } else if (!isFinishing()) {
            DeviceLog.error("Unity Ads web app is null, closing Unity Ads activity from onResume");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (WebViewApp.getCurrentApp() != null) {
            if (WebViewApp.getCurrentApp().getWebView() == null) {
                DeviceLog.warning("Unity Ads web view is null, from onPause");
            } else if (isFinishing()) {
                ViewUtilities.removeViewFromParent(WebViewApp.getCurrentApp().getWebView());
            }
            Map<String, IAdUnitViewHandler> map = this._viewHandlers;
            if (map != null) {
                for (Map.Entry next : map.entrySet()) {
                    if (next.getValue() != null) {
                        ((IAdUnitViewHandler) next.getValue()).onPause(this);
                    }
                }
            }
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.ADUNIT, AdUnitEvent.ON_PAUSE, Boolean.valueOf(isFinishing()), Integer.valueOf(this._activityId));
        } else if (!isFinishing()) {
            DeviceLog.error("Unity Ads web app is null, closing Unity Ads activity from onPause");
            finish();
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(EXTRA_ORIENTATION, this._orientation);
        bundle.putInt(EXTRA_SYSTEM_UI_VISIBILITY, this._systemUiVisibility);
        bundle.putIntegerArrayList(EXTRA_KEY_EVENT_LIST, this._keyEventList);
        bundle.putBoolean(EXTRA_KEEP_SCREEN_ON, this._keepScreenOn);
        bundle.putStringArray(EXTRA_VIEWS, this._views);
        bundle.putInt(EXTRA_ACTIVITY_ID, this._activityId);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (WebViewApp.getCurrentApp() != null) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.ADUNIT, AdUnitEvent.ON_DESTROY, Boolean.valueOf(isFinishing()), Integer.valueOf(this._activityId));
            Map<String, IAdUnitViewHandler> map = this._viewHandlers;
            if (map != null) {
                for (Map.Entry next : map.entrySet()) {
                    if (next.getValue() != null) {
                        ((IAdUnitViewHandler) next.getValue()).onDestroy(this);
                    }
                }
            }
            if (AdUnit.getCurrentAdUnitActivityId() == this._activityId) {
                AdUnit.setAdUnitActivity((AdUnitActivity) null);
            }
            Intent.removeActiveActivity(this);
        } else if (!isFinishing()) {
            DeviceLog.error("Unity Ads web app is null, closing Unity Ads activity from onDestroy");
            finish();
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        ArrayList<Integer> arrayList = this._keyEventList;
        if (arrayList == null || !arrayList.contains(Integer.valueOf(i))) {
            return false;
        }
        WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.ADUNIT, AdUnitEvent.KEY_DOWN, Integer.valueOf(i), Long.valueOf(keyEvent.getEventTime()), Long.valueOf(keyEvent.getDownTime()), Integer.valueOf(keyEvent.getRepeatCount()), Integer.valueOf(this._activityId));
        return true;
    }

    public void onWindowFocusChanged(boolean z) {
        if (z) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.ADUNIT, AdUnitEvent.ON_FOCUS_GAINED, Integer.valueOf(this._activityId));
        } else {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.ADUNIT, AdUnitEvent.ON_FOCUS_LOST, Integer.valueOf(this._activityId));
        }
        super.onWindowFocusChanged(z);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        try {
            JSONArray jSONArray = new JSONArray();
            JSONArray jSONArray2 = new JSONArray();
            for (String put : strArr) {
                jSONArray.put(put);
            }
            for (int put2 : iArr) {
                jSONArray2.put(put2);
            }
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.PERMISSIONS, PermissionsEvent.PERMISSIONS_RESULT, Integer.valueOf(i), jSONArray, jSONArray2);
        } catch (Exception e) {
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.PERMISSIONS, PermissionsEvent.PERMISSIONS_ERROR, e.getMessage());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:9:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setViewFrame(java.lang.String r3, int r4, int r5, int r6, int r7) {
        /*
            r2 = this;
            com.unity3d.services.ads.adunit.IAdUnitViewHandler r0 = r2.getViewHandler(r3)
            java.lang.String r1 = "adunit"
            boolean r3 = r3.equals(r1)
            r1 = 0
            if (r3 == 0) goto L_0x001b
            android.widget.FrameLayout$LayoutParams r3 = new android.widget.FrameLayout$LayoutParams
            r3.<init>(r6, r7)
            r3.setMargins(r4, r5, r1, r1)
            com.unity3d.services.ads.adunit.AdUnitRelativeLayout r0 = r2._layout
            r0.setLayoutParams(r3)
            goto L_0x0022
        L_0x001b:
            if (r0 == 0) goto L_0x0022
            android.view.View r3 = r0.getView()
            goto L_0x0023
        L_0x0022:
            r3 = 0
        L_0x0023:
            if (r3 == 0) goto L_0x0030
            android.widget.RelativeLayout$LayoutParams r0 = new android.widget.RelativeLayout$LayoutParams
            r0.<init>(r6, r7)
            r0.setMargins(r4, r5, r1, r1)
            r3.setLayoutParams(r0)
        L_0x0030:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.ads.adunit.AdUnitActivity.setViewFrame(java.lang.String, int, int, int, int):void");
    }

    public Map<String, Integer> getViewFrame(String str) {
        IAdUnitViewHandler viewHandler = getViewHandler(str);
        if (str.equals("adunit")) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this._layout.getLayoutParams();
            HashMap hashMap = new HashMap();
            hashMap.put("x", Integer.valueOf(layoutParams.leftMargin));
            hashMap.put("y", Integer.valueOf(layoutParams.topMargin));
            hashMap.put("width", Integer.valueOf(this._layout.getWidth()));
            hashMap.put("height", Integer.valueOf(this._layout.getHeight()));
            return hashMap;
        }
        View view = viewHandler != null ? viewHandler.getView() : null;
        if (view == null) {
            return null;
        }
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) view.getLayoutParams();
        HashMap hashMap2 = new HashMap();
        hashMap2.put("x", Integer.valueOf(layoutParams2.leftMargin));
        hashMap2.put("y", Integer.valueOf(layoutParams2.topMargin));
        hashMap2.put("width", Integer.valueOf(view.getWidth()));
        hashMap2.put("height", Integer.valueOf(view.getHeight()));
        return hashMap2;
    }

    public void setViews(String[] strArr) {
        if (strArr == null) {
            strArr = new String[0];
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(strArr));
        if (this._views == null) {
            this._views = new String[0];
        }
        ArrayList arrayList2 = new ArrayList(Arrays.asList(this._views));
        arrayList2.removeAll(arrayList);
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            getViewHandler((String) it.next()).destroy();
        }
        this._views = strArr;
        for (String str : strArr) {
            if (str != null) {
                IAdUnitViewHandler viewHandler = getViewHandler(str);
                viewHandler.create(this);
                if (!handleViewPlacement(viewHandler.getView())) {
                    return;
                }
            }
        }
    }

    private boolean handleViewPlacement(View view) {
        if (view == null) {
            finish();
            DeviceLog.error("Could not place view because it is null, finishing activity");
            return false;
        } else if (view.getParent() == null || !view.getParent().equals(this._layout)) {
            ViewUtilities.removeViewFromParent(view);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
            layoutParams.addRule(13);
            layoutParams.setMargins(0, 0, 0, 0);
            view.setPadding(0, 0, 0, 0);
            this._layout.addView(view, layoutParams);
            return true;
        } else {
            this._layout.bringChildToFront(view);
            return true;
        }
    }

    public String[] getViews() {
        return this._views;
    }

    public void setOrientation(int i) {
        this._orientation = i;
        setRequestedOrientation(i);
    }

    public boolean setKeepScreenOn(boolean z) {
        this._keepScreenOn = z;
        if (getWindow() == null) {
            return false;
        }
        if (z) {
            getWindow().addFlags(128);
            return true;
        }
        getWindow().clearFlags(128);
        return true;
    }

    public boolean setSystemUiVisibility(int i) {
        this._systemUiVisibility = i;
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                getWindow().getDecorView().setSystemUiVisibility(i);
                return true;
            } catch (Exception e) {
                DeviceLog.exception("Error while setting SystemUIVisibility", e);
            }
        }
        return false;
    }

    public void setKeyEventList(ArrayList<Integer> arrayList) {
        this._keyEventList = arrayList;
    }

    public IAdUnitViewHandler getViewHandler(String str) {
        Map<String, IAdUnitViewHandler> map = this._viewHandlers;
        if (map != null && map.containsKey(str)) {
            return this._viewHandlers.get(str);
        }
        IAdUnitViewHandler createViewHandler = createViewHandler(str);
        if (createViewHandler != null) {
            if (this._viewHandlers == null) {
                this._viewHandlers = new HashMap();
            }
            this._viewHandlers.put(str, createViewHandler);
        }
        return createViewHandler;
    }

    public void setLayoutInDisplayCutoutMode(int i) {
        this._displayCutoutMode = i;
        if (Build.VERSION.SDK_INT >= 28 && getWindow() != null) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            try {
                attributes.getClass().getField("layoutInDisplayCutoutMode").setInt(attributes, i);
            } catch (IllegalAccessException e) {
                DeviceLog.debug("Error setting layoutInDisplayCutoutMode", e);
            } catch (NoSuchFieldException e2) {
                DeviceLog.debug("Error getting layoutInDisplayCutoutMode", e2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void createLayout() {
        if (this._layout == null) {
            AdUnitRelativeLayout adUnitRelativeLayout = new AdUnitRelativeLayout(this);
            this._layout = adUnitRelativeLayout;
            adUnitRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            ViewUtilities.setBackground(this._layout, new ColorDrawable(-16777216));
        }
    }

    private IAdUnitViewHandler createViewHandler(String str) {
        Map<String, Class> adUnitViewHandlers;
        if (WebViewApp.getCurrentApp() != null) {
            Configuration configuration = WebViewApp.getCurrentApp().getConfiguration();
            String[] moduleConfigurationList = configuration.getModuleConfigurationList();
            int length = moduleConfigurationList.length;
            int i = 0;
            while (i < length) {
                IModuleConfiguration moduleConfiguration = configuration.getModuleConfiguration(moduleConfigurationList[i]);
                if (!(moduleConfiguration instanceof IAdsModuleConfiguration) || (adUnitViewHandlers = ((IAdsModuleConfiguration) moduleConfiguration).getAdUnitViewHandlers()) == null || !adUnitViewHandlers.containsKey(str)) {
                    i++;
                } else {
                    try {
                        return (IAdUnitViewHandler) adUnitViewHandlers.get(str).newInstance();
                    } catch (Exception unused) {
                        DeviceLog.error("Error creating view: " + str);
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
