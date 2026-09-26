package com.unity3d.services.banners;

public class BannerErrorInfo {
    public BannerErrorCode errorCode;
    public String errorMessage;

    public BannerErrorInfo(String str, BannerErrorCode bannerErrorCode) {
        this.errorCode = bannerErrorCode;
        this.errorMessage = str;
    }
}
