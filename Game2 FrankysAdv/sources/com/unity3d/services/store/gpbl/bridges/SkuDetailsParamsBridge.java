package com.unity3d.services.store.gpbl.bridges;

import com.unity3d.services.core.reflection.GenericBridge;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkuDetailsParamsBridge extends GenericBridge {
    private static final String newBuilderMethodName = "newBuilder";
    private static final Map<String, Class<?>[]> staticMethods = new HashMap<String, Class<?>[]>() {
        {
            put(SkuDetailsParamsBridge.newBuilderMethodName, new Class[0]);
        }
    };
    private final Object _skuDetailsParamsInternalInstance;

    /* access modifiers changed from: protected */
    public String getClassName() {
        return "com.android.billingclient.api.SkuDetailsParams";
    }

    public SkuDetailsParamsBridge(Object obj) {
        super(new HashMap<String, Class<?>[]>() {
            {
                put(SkuDetailsParamsBridge.newBuilderMethodName, new Class[0]);
            }
        });
        this._skuDetailsParamsInternalInstance = obj;
    }

    public Object getInternalInstance() {
        return this._skuDetailsParamsInternalInstance;
    }

    public static BuilderBridge newBuilder() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        return new BuilderBridge(callNonVoidStaticMethod(newBuilderMethodName, new Object[0]));
    }

    public static Object callNonVoidStaticMethod(String str, Object... objArr) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return getClassForBridge().getMethod(str, (Class[]) staticMethods.get(str)).invoke((Object) null, objArr);
    }

    public static Class<?> getClassForBridge() throws ClassNotFoundException {
        return Class.forName("com.android.billingclient.api.SkuDetailsParams");
    }

    public static class BuilderBridge extends GenericBridge {
        private static final String buildMethodName = "build";
        private static final String setSkusListMethodName = "setSkusList";
        private static final String setTypeMethodName = "setType";
        private Object _skuDetailsParamsBuilderInternalInstance;

        /* access modifiers changed from: protected */
        public String getClassName() {
            return "com.android.billingclient.api.SkuDetailsParams$Builder";
        }

        public BuilderBridge(Object obj) {
            super(new HashMap<String, Class<?>[]>() {
                {
                    put(BuilderBridge.buildMethodName, new Class[0]);
                    put(BuilderBridge.setSkusListMethodName, new Class[]{List.class});
                    put(BuilderBridge.setTypeMethodName, new Class[]{String.class});
                }
            });
            this._skuDetailsParamsBuilderInternalInstance = obj;
        }

        public BuilderBridge setSkuList(List<String> list) {
            this._skuDetailsParamsBuilderInternalInstance = callNonVoidMethod(setSkusListMethodName, this._skuDetailsParamsBuilderInternalInstance, list);
            return this;
        }

        public BuilderBridge setType(String str) {
            this._skuDetailsParamsBuilderInternalInstance = callNonVoidMethod(setTypeMethodName, this._skuDetailsParamsBuilderInternalInstance, str);
            return this;
        }

        public SkuDetailsParamsBridge build() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
            return new SkuDetailsParamsBridge(callNonVoidMethod(buildMethodName, this._skuDetailsParamsBuilderInternalInstance, new Object[0]));
        }
    }
}
