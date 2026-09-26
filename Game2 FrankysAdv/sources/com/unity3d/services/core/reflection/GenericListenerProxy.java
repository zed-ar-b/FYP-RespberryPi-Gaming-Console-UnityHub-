package com.unity3d.services.core.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class GenericListenerProxy implements InvocationHandler {
    public abstract Class<?> getProxyClass() throws ClassNotFoundException;

    public Object getProxyInstance() throws ClassNotFoundException {
        return Proxy.newProxyInstance(getProxyClass().getClassLoader(), new Class[]{getProxyClass()}, this);
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        return method.invoke(this, objArr);
    }
}
