package com.unity3d.services.core.configuration;

import java.util.Map;
import org.json.JSONObject;

public interface IExperiments {
    JSONObject getCurrentSessionExperiments();

    Map<String, String> getExperimentTags();

    JSONObject getExperimentsAsJson();

    JSONObject getNextSessionExperiments();

    boolean isForwardExperimentsToWebViewEnabled();

    boolean isNativeTokenEnabled();

    boolean isNativeWebViewCacheEnabled();

    boolean isNewLifecycleTimer();

    boolean isPrivacyRequestEnabled();

    boolean isTwoStageInitializationEnabled();

    boolean isUpdatePiiFields();

    boolean isWebAssetAdCaching();

    boolean isWebGestureNotRequired();

    boolean shouldNativeTokenAwaitPrivacy();
}
