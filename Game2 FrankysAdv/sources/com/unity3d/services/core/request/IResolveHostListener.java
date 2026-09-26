package com.unity3d.services.core.request;

public interface IResolveHostListener {
    void onFailed(String str, ResolveHostError resolveHostError, String str2);

    void onResolve(String str, String str2);
}
