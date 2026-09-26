package com.unity3d.scar.adapter.v2000.signals;

import com.google.android.gms.ads.query.QueryInfo;

public class QueryInfoMetadata {
    private String _error;
    private String _placementId;
    private QueryInfo _queryInfo;

    public QueryInfoMetadata(String str) {
        this._placementId = str;
    }

    public String getPlacementId() {
        return this._placementId;
    }

    public QueryInfo getQueryInfo() {
        return this._queryInfo;
    }

    public String getQueryStr() {
        QueryInfo queryInfo = this._queryInfo;
        if (queryInfo != null) {
            return queryInfo.getQuery();
        }
        return null;
    }

    public String getError() {
        return this._error;
    }

    public void setQueryInfo(QueryInfo queryInfo) {
        this._queryInfo = queryInfo;
    }

    public void setError(String str) {
        this._error = str;
    }
}
