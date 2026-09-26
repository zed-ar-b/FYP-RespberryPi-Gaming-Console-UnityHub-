package com.unity3d.player;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;

final class ReflectionHelper {
    protected static boolean LOG = false;
    protected static final boolean LOGV = false;

    /* renamed from: a */
    private static C0081a[] f38a = new C0081a[4096];
    /* access modifiers changed from: private */

    /* renamed from: b */
    public static long f39b = 0;

    /* renamed from: c */
    private static long f40c = 0;

    /* renamed from: d */
    private static boolean f41d = false;

    /* renamed from: com.unity3d.player.ReflectionHelper$a */
    private static class C0081a {

        /* renamed from: a */
        public volatile Member f50a;

        /* renamed from: b */
        private final Class f51b;

        /* renamed from: c */
        private final String f52c;

        /* renamed from: d */
        private final String f53d;

        /* renamed from: e */
        private final int f54e;

        C0081a(Class cls, String str, String str2) {
            this.f51b = cls;
            this.f52c = str;
            this.f53d = str2;
            this.f54e = ((((cls.hashCode() + 527) * 31) + this.f52c.hashCode()) * 31) + this.f53d.hashCode();
        }

        public final boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof C0081a) {
                C0081a aVar = (C0081a) obj;
                return this.f54e == aVar.f54e && this.f53d.equals(aVar.f53d) && this.f52c.equals(aVar.f52c) && this.f51b.equals(aVar.f51b);
            }
        }

        public final int hashCode() {
            return this.f54e;
        }
    }

    /* renamed from: com.unity3d.player.ReflectionHelper$b */
    private static class C0082b implements Runnable {

        /* renamed from: a */
        final long f55a;

        /* renamed from: b */
        final long f56b;

        public C0082b(long j, long j2) {
            this.f55a = j;
            this.f56b = j2;
        }

        public final void run() {
            if (ReflectionHelper.beginProxyCall(this.f55a)) {
                try {
                    ReflectionHelper.nativeProxyFinalize(this.f56b);
                } finally {
                    ReflectionHelper.endProxyCall();
                }
            }
        }
    }

    /* renamed from: com.unity3d.player.ReflectionHelper$c */
    protected interface C0083c extends InvocationHandler {
        /* renamed from: a */
        void mo208a(long j, boolean z);
    }

    ReflectionHelper() {
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:7|8|(1:10)|11|12|(1:14)(1:19)) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        return 0.0f;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x001e */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0024 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static float m21a(java.lang.Class r1, java.lang.Class r2) {
        /*
            boolean r0 = r1.equals(r2)
            if (r0 == 0) goto L_0x0009
            r1 = 1065353216(0x3f800000, float:1.0)
            return r1
        L_0x0009:
            boolean r0 = r1.isPrimitive()
            if (r0 != 0) goto L_0x0028
            boolean r0 = r2.isPrimitive()
            if (r0 != 0) goto L_0x0028
            java.lang.Class r0 = r1.asSubclass(r2)     // Catch:{ ClassCastException -> 0x001e }
            if (r0 == 0) goto L_0x001e
            r1 = 1056964608(0x3f000000, float:0.5)
            return r1
        L_0x001e:
            java.lang.Class r1 = r2.asSubclass(r1)     // Catch:{ ClassCastException -> 0x0028 }
            if (r1 == 0) goto L_0x0028
            r1 = 1036831949(0x3dcccccd, float:0.1)
            return r1
        L_0x0028:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.ReflectionHelper.m21a(java.lang.Class, java.lang.Class):float");
    }

    /* renamed from: a */
    private static float m22a(Class cls, Class[] clsArr, Class[] clsArr2) {
        if (clsArr2.length == 0) {
            return 0.1f;
        }
        int i = 0;
        if ((clsArr == null ? 0 : clsArr.length) + 1 != clsArr2.length) {
            return 0.0f;
        }
        float f = 1.0f;
        if (clsArr != null) {
            int length = clsArr.length;
            int i2 = 0;
            float f2 = 1.0f;
            while (i < length) {
                f2 *= m21a(clsArr[i], clsArr2[i2]);
                i++;
                i2++;
            }
            f = f2;
        }
        return f * m21a(cls, clsArr2[clsArr2.length - 1]);
    }

    /* renamed from: a */
    private static Class m24a(String str, int[] iArr) {
        while (iArr[0] < str.length()) {
            int i = iArr[0];
            iArr[0] = i + 1;
            char charAt = str.charAt(i);
            if (charAt != '(' && charAt != ')') {
                if (charAt == 'L') {
                    int indexOf = str.indexOf(59, iArr[0]);
                    if (indexOf == -1) {
                        return null;
                    }
                    String substring = str.substring(iArr[0], indexOf);
                    iArr[0] = indexOf + 1;
                    try {
                        return Class.forName(substring.replace('/', '.'));
                    } catch (ClassNotFoundException unused) {
                        return null;
                    }
                } else if (charAt == 'Z') {
                    return Boolean.TYPE;
                } else {
                    if (charAt == 'I') {
                        return Integer.TYPE;
                    }
                    if (charAt == 'F') {
                        return Float.TYPE;
                    }
                    if (charAt == 'V') {
                        return Void.TYPE;
                    }
                    if (charAt == 'B') {
                        return Byte.TYPE;
                    }
                    if (charAt == 'C') {
                        return Character.TYPE;
                    }
                    if (charAt == 'S') {
                        return Short.TYPE;
                    }
                    if (charAt == 'J') {
                        return Long.TYPE;
                    }
                    if (charAt == 'D') {
                        return Double.TYPE;
                    }
                    if (charAt == '[') {
                        return Array.newInstance(m24a(str, iArr), 0).getClass();
                    }
                    C0137f.Log(5, "! parseType; " + charAt + " is not known!");
                    return null;
                }
            }
        }
        return null;
    }

    /* renamed from: a */
    private static synchronized void m27a(C0081a aVar, Member member) {
        synchronized (ReflectionHelper.class) {
            aVar.f50a = member;
            f38a[aVar.hashCode() & (f38a.length - 1)] = aVar;
        }
    }

    /* renamed from: a */
    private static synchronized boolean m28a(C0081a aVar) {
        synchronized (ReflectionHelper.class) {
            C0081a aVar2 = f38a[aVar.hashCode() & (f38a.length - 1)];
            if (!aVar.equals(aVar2)) {
                return false;
            }
            aVar.f50a = aVar2.f50a;
            return true;
        }
    }

    /* renamed from: a */
    private static Class[] m29a(String str) {
        Class a;
        int i = 0;
        int[] iArr = {0};
        ArrayList arrayList = new ArrayList();
        while (iArr[0] < str.length() && (a = m24a(str, iArr)) != null) {
            arrayList.add(a);
        }
        Class[] clsArr = new Class[arrayList.size()];
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            clsArr[i] = (Class) it.next();
            i++;
        }
        return clsArr;
    }

    protected static synchronized boolean beginProxyCall(long j) {
        boolean z;
        synchronized (ReflectionHelper.class) {
            if (j == f39b) {
                f40c++;
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    protected static synchronized void endProxyCall() {
        Class<ReflectionHelper> cls = ReflectionHelper.class;
        synchronized (cls) {
            long j = f40c - 1;
            f40c = j;
            if (0 == j && f41d) {
                cls.notifyAll();
            }
        }
    }

    protected static synchronized void endUnityLaunch() {
        Class<ReflectionHelper> cls = ReflectionHelper.class;
        synchronized (cls) {
            try {
                f39b++;
                f41d = true;
                while (f40c > 0) {
                    cls.wait();
                }
            } catch (InterruptedException unused) {
                C0137f.Log(6, "Interrupted while waiting for all proxies to exit.");
            }
            f41d = false;
        }
    }

    protected static Constructor getConstructorID(Class cls, String str) {
        Constructor constructor;
        C0081a aVar = new C0081a(cls, "", str);
        if (m28a(aVar)) {
            constructor = (Constructor) aVar.f50a;
        } else {
            Class[] a = m29a(str);
            float f = 0.0f;
            Constructor constructor2 = null;
            for (Constructor constructor3 : cls.getConstructors()) {
                float a2 = m22a(Void.TYPE, constructor3.getParameterTypes(), a);
                if (a2 > f) {
                    constructor2 = constructor3;
                    if (a2 == 1.0f) {
                        break;
                    }
                    f = a2;
                }
            }
            m27a(aVar, (Member) constructor2);
            constructor = constructor2;
        }
        if (constructor != null) {
            return constructor;
        }
        throw new NoSuchMethodError("<init>" + str + " in class " + cls.getName());
    }

    protected static Field getFieldID(Class cls, String str, String str2, boolean z) {
        Field field;
        String str3 = str;
        String str4 = str2;
        boolean z2 = z;
        Class cls2 = cls;
        C0081a aVar = new C0081a(cls2, str3, str4);
        if (m28a(aVar)) {
            field = (Field) aVar.f50a;
        } else {
            Class[] a = m29a(str2);
            float f = 0.0f;
            Field field2 = null;
            while (cls2 != null) {
                Field[] declaredFields = cls2.getDeclaredFields();
                int length = declaredFields.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Field field3 = declaredFields[i];
                    if (z2 == Modifier.isStatic(field3.getModifiers()) && field3.getName().compareTo(str3) == 0) {
                        float a2 = m22a((Class) field3.getType(), (Class[]) null, a);
                        if (a2 > f) {
                            field2 = field3;
                            if (a2 == 1.0f) {
                                f = a2;
                                break;
                            }
                            f = a2;
                        } else {
                            continue;
                        }
                    }
                    i++;
                }
                if (f == 1.0f || cls2.isPrimitive() || cls2.isInterface() || cls2.equals(Object.class) || cls2.equals(Void.TYPE)) {
                    break;
                }
                cls2 = cls2.getSuperclass();
            }
            m27a(aVar, (Member) field2);
            field = field2;
        }
        if (field != null) {
            return field;
        }
        Object[] objArr = new Object[4];
        objArr[0] = z2 ? "static" : "non-static";
        objArr[1] = str3;
        objArr[2] = str4;
        objArr[3] = cls2.getName();
        throw new NoSuchFieldError(String.format("no %s field with name='%s' signature='%s' in class L%s;", objArr));
    }

    protected static String getFieldSignature(Field field) {
        Class<?> type = field.getType();
        if (type.isPrimitive()) {
            String name = type.getName();
            return "boolean".equals(name) ? "Z" : "byte".equals(name) ? "B" : "char".equals(name) ? "C" : "double".equals(name) ? "D" : "float".equals(name) ? "F" : "int".equals(name) ? "I" : "long".equals(name) ? "J" : "short".equals(name) ? "S" : name;
        } else if (type.isArray()) {
            return type.getName().replace('.', '/');
        } else {
            return "L" + type.getName().replace('.', '/') + ";";
        }
    }

    protected static Method getMethodID(Class cls, String str, String str2, boolean z) {
        Method method;
        C0081a aVar = new C0081a(cls, str, str2);
        if (m28a(aVar)) {
            method = (Method) aVar.f50a;
        } else {
            Class[] a = m29a(str2);
            float f = 0.0f;
            Method method2 = null;
            while (cls != null) {
                Method[] declaredMethods = cls.getDeclaredMethods();
                int length = declaredMethods.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Method method3 = declaredMethods[i];
                    if (z == Modifier.isStatic(method3.getModifiers()) && method3.getName().compareTo(str) == 0) {
                        float a2 = m22a((Class) method3.getReturnType(), method3.getParameterTypes(), a);
                        if (a2 > f) {
                            method2 = method3;
                            if (a2 == 1.0f) {
                                f = a2;
                                break;
                            }
                            f = a2;
                        } else {
                            continue;
                        }
                    }
                    i++;
                }
                if (f == 1.0f || cls.isPrimitive() || cls.isInterface() || cls.equals(Object.class) || cls.equals(Void.TYPE)) {
                    break;
                }
                cls = cls.getSuperclass();
            }
            m27a(aVar, (Member) method2);
            method = method2;
        }
        if (method != null) {
            return method;
        }
        Object[] objArr = new Object[4];
        objArr[0] = z ? "static" : "non-static";
        objArr[1] = str;
        objArr[2] = str2;
        objArr[3] = cls.getName();
        throw new NoSuchMethodError(String.format("no %s method with name='%s' signature='%s' in class L%s;", objArr));
    }

    /* access modifiers changed from: private */
    public static native void nativeProxyFinalize(long j);

    /* access modifiers changed from: private */
    public static native Object nativeProxyInvoke(long j, String str, Object[] objArr);

    /* access modifiers changed from: private */
    public static native void nativeProxyLogJNIInvokeException(long j);

    protected static Object newProxyInstance(UnityPlayer unityPlayer, long j, Class cls) {
        return newProxyInstance(unityPlayer, j, new Class[]{cls});
    }

    protected static Object newProxyInstance(final UnityPlayer unityPlayer, final long j, final Class[] clsArr) {
        return Proxy.newProxyInstance(ReflectionHelper.class.getClassLoader(), clsArr, new C0083c() {

            /* renamed from: d */
            private Runnable f45d = new C0082b(ReflectionHelper.f39b, j);

            /* renamed from: e */
            private UnityPlayer f46e = unityPlayer;

            /* renamed from: f */
            private long f47f = ReflectionHelper.f39b;

            /* renamed from: g */
            private long f48g;

            /* renamed from: h */
            private boolean f49h;

            /* renamed from: a */
            private Object m31a(Object obj, Method method, Object[] objArr) {
                if (objArr == null) {
                    try {
                        objArr = new Object[0];
                    } catch (NoClassDefFoundError unused) {
                        C0137f.Log(6, String.format("Java interface default methods are only supported since Android Oreo", new Object[0]));
                        ReflectionHelper.nativeProxyLogJNIInvokeException(this.f48g);
                        return null;
                    }
                }
                Class<?> declaringClass = method.getDeclaringClass();
                Constructor<MethodHandles.Lookup> declaredConstructor = MethodHandles.Lookup.class.getDeclaredConstructor(new Class[]{Class.class, Integer.TYPE});
                declaredConstructor.setAccessible(true);
                return declaredConstructor.newInstance(new Object[]{declaringClass, 2}).in(declaringClass).unreflectSpecial(method, declaringClass).bindTo(obj).invokeWithArguments(objArr);
            }

            /* renamed from: a */
            public final void mo208a(long j, boolean z) {
                this.f48g = j;
                this.f49h = z;
            }

            /* access modifiers changed from: protected */
            public final void finalize() {
                this.f46e.queueGLThreadEvent(this.f45d);
                super.finalize();
            }

            public final Object invoke(Object obj, Method method, Object[] objArr) {
                long j;
                if (!ReflectionHelper.beginProxyCall(this.f47f)) {
                    C0137f.Log(6, "Scripting proxy object was destroyed, because Unity player was unloaded.");
                    return null;
                }
                try {
                    this.f48g = 0;
                    this.f49h = false;
                    Object a = ReflectionHelper.nativeProxyInvoke(j, method.getName(), objArr);
                    if (!this.f49h) {
                        if (this.f48g != 0) {
                            j = this.f48g;
                        }
                        ReflectionHelper.endProxyCall();
                        return a;
                    } else if ((method.getModifiers() & 1024) == 0) {
                        return m31a(obj, method, objArr);
                    } else {
                        j = this.f48g;
                    }
                    ReflectionHelper.nativeProxyLogJNIInvokeException(j);
                    ReflectionHelper.endProxyCall();
                    return a;
                } finally {
                    ReflectionHelper.endProxyCall();
                }
            }
        });
    }

    protected static void setNativeExceptionOnProxy(Object obj, long j, boolean z) {
        ((C0083c) Proxy.getInvocationHandler(obj)).mo208a(j, z);
    }
}
