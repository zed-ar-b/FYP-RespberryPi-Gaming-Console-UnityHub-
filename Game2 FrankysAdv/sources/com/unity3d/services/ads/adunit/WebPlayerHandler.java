package com.unity3d.services.ads.adunit;

import android.os.Bundle;
import android.view.View;
import com.unity3d.services.ads.webplayer.WebPlayerSettingsCache;
import com.unity3d.services.ads.webplayer.WebPlayerView;
import com.unity3d.services.ads.webplayer.WebPlayerViewCache;
import com.unity3d.services.core.misc.ViewUtilities;

public class WebPlayerHandler implements IAdUnitViewHandler {
    private static String webPlayerViewId = "webplayer";
    private WebPlayerView _webPlayerView;

    public void onPause(AdUnitActivity adUnitActivity) {
    }

    public void onResume(AdUnitActivity adUnitActivity) {
    }

    public void onStart(AdUnitActivity adUnitActivity) {
    }

    public void onStop(AdUnitActivity adUnitActivity) {
    }

    public boolean create(AdUnitActivity adUnitActivity) {
        if (this._webPlayerView != null) {
            return true;
        }
        WebPlayerSettingsCache instance = WebPlayerSettingsCache.getInstance();
        String str = webPlayerViewId;
        WebPlayerView webPlayerView = new WebPlayerView(adUnitActivity, str, instance.getWebSettings(str), instance.getWebPlayerSettings(webPlayerViewId));
        this._webPlayerView = webPlayerView;
        webPlayerView.setEventSettings(instance.getWebPlayerEventSettings(webPlayerViewId));
        WebPlayerViewCache.getInstance().addWebPlayer(webPlayerViewId, this._webPlayerView);
        return true;
    }

    public boolean destroy() {
        WebPlayerView webPlayerView = this._webPlayerView;
        if (webPlayerView != null) {
            ViewUtilities.removeViewFromParent(webPlayerView);
            this._webPlayerView.destroy();
        }
        WebPlayerViewCache.getInstance().removeWebPlayer(webPlayerViewId);
        this._webPlayerView = null;
        return true;
    }

    public View getView() {
        return this._webPlayerView;
    }

    public void onCreate(AdUnitActivity adUnitActivity, Bundle bundle) {
        create(adUnitActivity);
    }

    public void onDestroy(AdUnitActivity adUnitActivity) {
        if (adUnitActivity.isFinishing()) {
            destroy();
        }
    }
}
