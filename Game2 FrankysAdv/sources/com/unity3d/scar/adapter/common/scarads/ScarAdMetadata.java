package com.unity3d.scar.adapter.common.scarads;

public class ScarAdMetadata {
    private String _adString;
    private String _adUnitId;
    private String _placementId;
    private String _queryId;
    private Integer _videoLengthMs;

    public ScarAdMetadata(String str, String str2) {
        this(str, str2, (String) null, (String) null, (Integer) null);
    }

    public ScarAdMetadata(String str, String str2, String str3, String str4, Integer num) {
        this._placementId = str;
        this._queryId = str2;
        this._adUnitId = str3;
        this._adString = str4;
        this._videoLengthMs = num;
    }

    public String getPlacementId() {
        return this._placementId;
    }

    public String getQueryId() {
        return this._queryId;
    }

    public String getAdUnitId() {
        return this._adUnitId;
    }

    public String getAdString() {
        return this._adString;
    }

    public Integer getVideoLengthMs() {
        return this._videoLengthMs;
    }
}
