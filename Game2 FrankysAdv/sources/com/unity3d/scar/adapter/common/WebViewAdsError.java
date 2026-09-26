package com.unity3d.scar.adapter.common;

public class WebViewAdsError implements IUnityAdsError {
    protected String _description;
    protected Object[] _errorArguments;
    private Enum _errorCategory;

    public int getCode() {
        return -1;
    }

    public String getDomain() {
        return null;
    }

    public WebViewAdsError(Enum<?> enumR, String str, Object... objArr) {
        this._errorCategory = enumR;
        this._description = str;
        this._errorArguments = objArr;
    }

    public String getDescription() {
        return this._description;
    }

    public Enum<?> getErrorCategory() {
        return this._errorCategory;
    }

    public Object[] getErrorArguments() {
        return this._errorArguments;
    }
}
