package com.unity3d.services.ads.adunit;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.unity3d.services.core.misc.ViewUtilities;

public class AdUnitTransparentActivity extends AdUnitActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ViewUtilities.setBackground(this._layout, new ColorDrawable(0));
    }

    /* access modifiers changed from: protected */
    public void createLayout() {
        super.createLayout();
        ViewUtilities.setBackground(this._layout, new ColorDrawable(0));
    }
}
