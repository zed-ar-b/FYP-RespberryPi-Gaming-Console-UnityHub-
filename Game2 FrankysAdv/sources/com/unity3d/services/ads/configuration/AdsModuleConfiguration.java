package com.unity3d.services.ads.configuration;

import android.os.ConditionVariable;
import com.unity3d.services.ads.UnityAdsImplementation;
import com.unity3d.services.ads.adunit.AdUnitOpen;
import com.unity3d.services.ads.adunit.VideoPlayerHandler;
import com.unity3d.services.ads.adunit.WebPlayerHandler;
import com.unity3d.services.ads.adunit.WebViewHandler;
import com.unity3d.services.ads.api.AdUnit;
import com.unity3d.services.ads.api.GMAScar;
import com.unity3d.services.ads.api.Load;
import com.unity3d.services.ads.api.Show;
import com.unity3d.services.ads.api.Token;
import com.unity3d.services.ads.api.VideoPlayer;
import com.unity3d.services.ads.api.WebPlayer;
import com.unity3d.services.ads.token.AsyncTokenStorage;
import com.unity3d.services.ads.token.TokenStorage;
import com.unity3d.services.core.configuration.Configuration;
import com.unity3d.services.core.configuration.ErrorState;
import com.unity3d.services.core.log.DeviceLog;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AdsModuleConfiguration implements IAdsModuleConfiguration {
    /* access modifiers changed from: private */
    public InetAddress _address;

    public Class[] getWebAppApiClassList() {
        return new Class[]{AdUnit.class, VideoPlayer.class, WebPlayer.class, Load.class, Show.class, Token.class, GMAScar.class};
    }

    public boolean resetState(Configuration configuration) {
        AdUnitOpen.setConfiguration(configuration);
        UnityAdsImplementation.setConfiguration(configuration);
        TokenStorage.deleteTokens();
        AsyncTokenStorage.getInstance().setConfiguration(configuration);
        return true;
    }

    public boolean initModuleState(Configuration configuration) {
        InetAddress inetAddress;
        DeviceLog.debug("Unity Ads init: checking for ad blockers");
        try {
            final String host = new URL(configuration.getConfigUrl()).getHost();
            final ConditionVariable conditionVariable = new ConditionVariable();
            new Thread() {
                public void run() {
                    try {
                        InetAddress unused = AdsModuleConfiguration.this._address = InetAddress.getByName(host);
                        conditionVariable.open();
                    } catch (Exception e) {
                        DeviceLog.exception("Couldn't get address. Host: " + host, e);
                        conditionVariable.open();
                    }
                }
            }.start();
            if (!conditionVariable.block(2000) || (inetAddress = this._address) == null || !inetAddress.isLoopbackAddress()) {
                AdUnitOpen.setConfiguration(configuration);
                UnityAdsImplementation.setConfiguration(configuration);
                AsyncTokenStorage.getInstance().setConfiguration(configuration);
                return true;
            }
            DeviceLog.error("Unity Ads init: halting init because Unity Ads config resolves to loopback address (due to ad blocker?)");
            return false;
        } catch (MalformedURLException unused) {
        }
    }

    public boolean initErrorState(Configuration configuration, ErrorState errorState, String str) {
        TokenStorage.setInitToken((String) null);
        TokenStorage.deleteTokens();
        return true;
    }

    public boolean initCompleteState(Configuration configuration) {
        AdUnitOpen.setConfiguration(configuration);
        UnityAdsImplementation.setConfiguration(configuration);
        AsyncTokenStorage.getInstance().setConfiguration(configuration);
        return true;
    }

    public Map<String, Class> getAdUnitViewHandlers() {
        HashMap hashMap = new HashMap();
        hashMap.put("videoplayer", VideoPlayerHandler.class);
        hashMap.put("webplayer", WebPlayerHandler.class);
        hashMap.put("webview", WebViewHandler.class);
        return hashMap;
    }
}
