package com.unity3d.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

public class HFPStatus {

    /* renamed from: a */
    private Context f16a;

    /* renamed from: b */
    private BroadcastReceiver f17b = null;

    /* renamed from: c */
    private Intent f18c = null;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public boolean f19d = false;
    /* access modifiers changed from: private */

    /* renamed from: e */
    public AudioManager f20e = null;

    /* renamed from: f */
    private boolean f21f = false;
    /* access modifiers changed from: private */

    /* renamed from: g */
    public int f22g = C0078a.f24a;

    /* renamed from: com.unity3d.player.HFPStatus$a */
    enum C0078a {
        ;

        static {
            f26c = new int[]{1, 2};
        }
    }

    public HFPStatus(Context context) {
        this.f16a = context;
        this.f20e = (AudioManager) context.getSystemService("audio");
        initHFPStatusJni();
    }

    /* renamed from: b */
    private void m9b() {
        BroadcastReceiver broadcastReceiver = this.f17b;
        if (broadcastReceiver != null) {
            this.f16a.unregisterReceiver(broadcastReceiver);
            this.f17b = null;
            this.f18c = null;
        }
        this.f22g = C0078a.f24a;
    }

    /* access modifiers changed from: private */
    /* renamed from: c */
    public void m12c() {
        if (this.f21f) {
            this.f21f = false;
            this.f20e.stopBluetoothSco();
        }
    }

    private final native void deinitHFPStatusJni();

    private final native void initHFPStatusJni();

    /* renamed from: a */
    public final void mo201a() {
        clearHFPStat();
        deinitHFPStatusJni();
    }

    /* access modifiers changed from: protected */
    public void clearHFPStat() {
        m9b();
        m12c();
    }

    /* access modifiers changed from: protected */
    public boolean getHFPStat() {
        return this.f22g == C0078a.f25b;
    }

    /* access modifiers changed from: protected */
    public void requestHFPStat() {
        clearHFPStat();
        C00771 r0 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1) == 1) {
                    int unused = HFPStatus.this.f22g = C0078a.f25b;
                    HFPStatus.this.m12c();
                    if (HFPStatus.this.f19d) {
                        HFPStatus.this.f20e.setMode(3);
                    }
                }
            }
        };
        this.f17b = r0;
        this.f18c = this.f16a.registerReceiver(r0, new IntentFilter("android.media.ACTION_SCO_AUDIO_STATE_UPDATED"));
        try {
            this.f21f = true;
            this.f20e.startBluetoothSco();
        } catch (NullPointerException unused) {
            C0139f.Log(5, "startBluetoothSco() failed. no bluetooth device connected.");
        }
    }

    /* access modifiers changed from: protected */
    public void setHFPRecordingStat(boolean z) {
        this.f19d = z;
        if (!z) {
            this.f20e.setMode(0);
        }
    }
}
