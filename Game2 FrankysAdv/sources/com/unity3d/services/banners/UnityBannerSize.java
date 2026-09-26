package com.unity3d.services.banners;

import android.content.Context;
import android.content.res.Resources;
import com.unity3d.services.core.misc.ViewUtilities;

public class UnityBannerSize {
    private int height;
    private int width;

    public UnityBannerSize(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public static UnityBannerSize getDynamicSize(Context context) {
        return new UnityBannerSize(BannerSize.BANNER_SIZE_DYNAMIC.getWidth(context), BannerSize.BANNER_SIZE_DYNAMIC.getHeight(context));
    }

    private enum BannerSize {
        BANNER_SIZE_STANDARD,
        BANNER_SIZE_LEADERBOARD,
        BANNER_SIZE_IAB_STANDARD,
        BANNER_SIZE_DYNAMIC;
        
        private static final int IAB_STANDARD_HEIGHT = 60;
        private static final int IAB_STANDARD_WIDTH = 468;
        private static final int LEADERBOARD_HEIGHT = 90;
        private static final int LEADERBOARD_WIDTH = 728;
        private static final int STANDARD_HEIGHT = 50;
        private static final int STANDARD_WIDTH = 320;

        private BannerSize getNonDynamicSize(Context context) {
            if (this != BANNER_SIZE_DYNAMIC) {
                return this;
            }
            int round = Math.round(ViewUtilities.dpFromPx(context, (float) Resources.getSystem().getDisplayMetrics().widthPixels));
            if (round >= LEADERBOARD_WIDTH) {
                return BANNER_SIZE_LEADERBOARD;
            }
            if (round >= IAB_STANDARD_WIDTH) {
                return BANNER_SIZE_IAB_STANDARD;
            }
            return BANNER_SIZE_STANDARD;
        }

        /* access modifiers changed from: private */
        public int getWidth(Context context) {
            int i = C02691.f341xd7cd7171[getNonDynamicSize(context).ordinal()];
            if (i != 2) {
                return i != 3 ? STANDARD_WIDTH : IAB_STANDARD_WIDTH;
            }
            return LEADERBOARD_WIDTH;
        }

        /* access modifiers changed from: private */
        public int getHeight(Context context) {
            int i = C02691.f341xd7cd7171[getNonDynamicSize(context).ordinal()];
            if (i != 2) {
                return i != 3 ? STANDARD_HEIGHT : IAB_STANDARD_HEIGHT;
            }
            return LEADERBOARD_HEIGHT;
        }
    }

    /* renamed from: com.unity3d.services.banners.UnityBannerSize$1 */
    static /* synthetic */ class C02691 {

        /* renamed from: $SwitchMap$com$unity3d$services$banners$UnityBannerSize$BannerSize */
        static final /* synthetic */ int[] f341xd7cd7171;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.unity3d.services.banners.UnityBannerSize$BannerSize[] r0 = com.unity3d.services.banners.UnityBannerSize.BannerSize.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f341xd7cd7171 = r0
                com.unity3d.services.banners.UnityBannerSize$BannerSize r1 = com.unity3d.services.banners.UnityBannerSize.BannerSize.BANNER_SIZE_STANDARD     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f341xd7cd7171     // Catch:{ NoSuchFieldError -> 0x001d }
                com.unity3d.services.banners.UnityBannerSize$BannerSize r1 = com.unity3d.services.banners.UnityBannerSize.BannerSize.BANNER_SIZE_LEADERBOARD     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f341xd7cd7171     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.unity3d.services.banners.UnityBannerSize$BannerSize r1 = com.unity3d.services.banners.UnityBannerSize.BannerSize.BANNER_SIZE_IAB_STANDARD     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.banners.UnityBannerSize.C02691.<clinit>():void");
        }
    }
}
