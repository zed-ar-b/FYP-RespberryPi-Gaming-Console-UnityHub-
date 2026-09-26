package com.unity3d.services.core.properties;

import com.unity3d.services.core.properties.SdkProperties;

public class InitializationStatusReader {
    private static final String STATE_INITIALIZED_FAILED = "initialized_failed";
    private static final String STATE_INITIALIZED_SUCCESSFULLY = "initialized_successfully";
    private static final String STATE_INITIALIZING = "initializing";
    private static final String STATE_NOT_INITIALIZED = "not_initialized";

    /* renamed from: com.unity3d.services.core.properties.InitializationStatusReader$1 */
    static /* synthetic */ class C03091 {

        /* renamed from: $SwitchMap$com$unity3d$services$core$properties$SdkProperties$InitializationState */
        static final /* synthetic */ int[] f348xe3e1bbe9;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.unity3d.services.core.properties.SdkProperties$InitializationState[] r0 = com.unity3d.services.core.properties.SdkProperties.InitializationState.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f348xe3e1bbe9 = r0
                com.unity3d.services.core.properties.SdkProperties$InitializationState r1 = com.unity3d.services.core.properties.SdkProperties.InitializationState.NOT_INITIALIZED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f348xe3e1bbe9     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.services.core.properties.SdkProperties$InitializationState r1 = com.unity3d.services.core.properties.SdkProperties.InitializationState.INITIALIZING     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f348xe3e1bbe9     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.unity3d.services.core.properties.SdkProperties$InitializationState r1 = com.unity3d.services.core.properties.SdkProperties.InitializationState.INITIALIZED_SUCCESSFULLY     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f348xe3e1bbe9     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.unity3d.services.core.properties.SdkProperties$InitializationState r1 = com.unity3d.services.core.properties.SdkProperties.InitializationState.INITIALIZED_FAILED     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.properties.InitializationStatusReader.C03091.<clinit>():void");
        }
    }

    public String getInitializationStateString(SdkProperties.InitializationState initializationState) {
        int i = C03091.f348xe3e1bbe9[initializationState.ordinal()];
        if (i == 1) {
            return STATE_NOT_INITIALIZED;
        }
        if (i == 2) {
            return STATE_INITIALIZING;
        }
        if (i == 3) {
            return STATE_INITIALIZED_SUCCESSFULLY;
        }
        if (i != 4) {
            return null;
        }
        return STATE_INITIALIZED_FAILED;
    }
}
