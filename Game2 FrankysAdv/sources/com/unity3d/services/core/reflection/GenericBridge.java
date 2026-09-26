package com.unity3d.services.core.reflection;

import com.unity3d.scar.adapter.common.GMAEvent;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.webview.WebViewApp;
import com.unity3d.services.core.webview.WebViewEventCategory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericBridge {
    private final String _className = getClassName();
    private final Map<String, Class<?>[]> _functionAndParameters;
    private final Map<String, Method> _methodMap;
    private boolean _methodMapBuilt = false;

    /* access modifiers changed from: protected */
    public abstract String getClassName();

    public GenericBridge(Map<String, Class<?>[]> map) {
        this._functionAndParameters = map;
        this._methodMap = new HashMap();
        buildMethodMap();
    }

    public Map<String, Class<?>[]> getFunctionMap() {
        return this._functionAndParameters;
    }

    public Class<?> classForName() {
        try {
            return Class.forName(this._className);
        } catch (ClassNotFoundException e) {
            DeviceLog.debug("ERROR: Could not find Class %s %s", this._className, e.getLocalizedMessage());
            return null;
        }
    }

    public boolean exists() {
        if (classForName() == null) {
            DeviceLog.debug("ERROR: Could not find class %s", this._className);
            return false;
        }
        if (!this._methodMapBuilt) {
            buildMethodMap();
        }
        if (this._methodMap.size() == getFunctionMap().size()) {
            return true;
        }
        return false;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void buildMethodMap() {
        /*
            r9 = this;
            java.util.Map r0 = r9.getFunctionMap()
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
            r1 = 1
            r2 = 1
        L_0x000e:
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L_0x0052
            java.lang.Object r3 = r0.next()
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3
            java.lang.Object r4 = r3.getValue()
            java.lang.Class[] r4 = (java.lang.Class[]) r4
            r5 = 0
            java.lang.Class r6 = r9.classForName()     // Catch:{ Exception -> 0x003b }
            java.lang.Object r7 = r3.getKey()     // Catch:{ Exception -> 0x003b }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Exception -> 0x003b }
            java.lang.reflect.Method r6 = r9.getReflectiveMethod(r6, r7, r4)     // Catch:{ Exception -> 0x003b }
            if (r6 == 0) goto L_0x000e
            java.util.Map<java.lang.String, java.lang.reflect.Method> r7 = r9._methodMap     // Catch:{ Exception -> 0x003b }
            java.lang.Object r8 = r3.getKey()     // Catch:{ Exception -> 0x003b }
            r7.put(r8, r6)     // Catch:{ Exception -> 0x003b }
            goto L_0x000e
        L_0x003b:
            r2 = 3
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.String r6 = r9._className
            r2[r5] = r6
            java.lang.Object r3 = r3.getKey()
            r2[r1] = r3
            r3 = 2
            r2[r3] = r4
            java.lang.String r3 = "ERROR: Could not find %s class with method %s and parameters : %s"
            com.unity3d.services.core.log.DeviceLog.debug(r3, r2)
            r2 = 0
            goto L_0x000e
        L_0x0052:
            r9._methodMapBuilt = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.reflection.GenericBridge.buildMethodMap():void");
    }

    private Method getMethod(String str) {
        return this._methodMap.get(str);
    }

    private Method getReflectiveMethod(Class<?> cls, String str, Class<?>... clsArr) {
        try {
            return cls.getDeclaredMethod(str, clsArr);
        } catch (Exception e) {
            DeviceLog.debug("ERROR: Could not find method %s in %s", str, cls.getName() + " " + e.getLocalizedMessage());
            WebViewApp.getCurrentApp().sendEvent(WebViewEventCategory.GMA, GMAEvent.METHOD_ERROR, new Object[0]);
            return null;
        }
    }

    public void callVoidMethod(String str, Object obj, Object... objArr) {
        Method method = getMethod(str);
        if (method == null) {
            DeviceLog.debug("ERROR: Could not find method %s", str);
            return;
        }
        try {
            method.invoke(obj, objArr);
        } catch (Exception e) {
            DeviceLog.debug("ERROR: Could not invoke method %s : %s", str, e.getLocalizedMessage());
        }
    }

    public <T> T callNonVoidMethod(String str, Object obj, Object... objArr) {
        Method method = getMethod(str);
        if (method == null) {
            DeviceLog.debug("ERROR: Could not find method %s", str);
            return null;
        }
        try {
            return method.invoke(obj, objArr);
        } catch (Exception e) {
            DeviceLog.debug("ERROR: Could not invoke method %s : %s", str, e.getLocalizedMessage());
            return null;
        }
    }
}
