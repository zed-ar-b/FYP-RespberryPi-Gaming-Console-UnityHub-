package com.unity3d.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

public class HFPStatus {

    /* renamed from: a */
    private Context f17a;

    /* renamed from: b */
    private BroadcastReceiver f18b = null;

    /* renamed from: c */
    private Intent f19c = null;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public boolean f20d = false;
    /* access modifiers changed from: private */

    /* renamed from: e */
    public AudioManager f21e = null;

    /* renamed from: f */
    private boolean f22f = false;
    /* access modifiers changed from: private */

    /* renamed from: g */
    public int f23g = C0009a.f25a;

    /* renamed from: com.unity3d.player.HFPStatus$a */
    enum C0009a {
        ;

        static {
            f27c = new int[]{1, 2};
        }
    }

    public HFPStatus(Context context) {
        this.f17a = context;
        this.f21e = (AudioManager) context.getSystemService("audio");
        initHFPStatusJni();
    }

    /* renamed from: b */
    private void m9b() {
        BroadcastReceiver broadcastReceiver = this.f18b;
        if (broadcastReceiver != null) {
            this.f17a.unregisterReceiver(broadcastReceiver);
            this.f18b = null;
            this.f19c = null;
        }
        this.f23g = C0009a.f25a;
    }

    /* access modifiers changed from: private */
    /* renamed from: c */
    public void m12c() {
        if (this.f22f) {
            this.f22f = false;
            this.f21e.stopBluetoothSco();
        }
    }

    private final native void deinitHFPStatusJni();

    private final native void initHFPStatusJni();

    /* renamed from: a */
    public final void mo75a() {
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
        return this.f23g == C0009a.f26b;
    }

    /* access modifiers changed from: protected */
    public void requestHFPStat() {
        clearHFPStat();
        C00081 r0 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra("android.media.extra.SCO_AUDIO_STATE", -1) == 1) {
                    int unused = HFPStatus.this.f23g = C0009a.f26b;
                    HFPStatus.this.m12c();
                    if (HFPStatus.this.f20d) {
                        HFPStatus.this.f21e.setMode(3);
                    }
                }
            }
        };
        this.f18b = r0;
        this.f19c = this.f17a.registerReceiver(r0, new IntentFilter("android.media.ACTION_SCO_AUDIO_STATE_UPDATED"));
        try {
            this.f22f = true;
            this.f21e.startBluetoothSco();
        } catch (NullPointerException unused) {
            C0070f.Log(5, "startBluetoothSco() failed. no bluetooth device connected.");
        }
    }

    /* access modifiers changed from: protected */
    public void setHFPRecordingStat(boolean z) {
        this.f20d = z;
        if (!z) {
            this.f21e.setMode(0);
        }
    }
}
