package org.fmod;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;

/* renamed from: org.fmod.a */
final class C0101a implements Runnable {

    /* renamed from: a */
    private final FMODAudioDevice f329a;

    /* renamed from: b */
    private final ByteBuffer f330b;

    /* renamed from: c */
    private final int f331c;

    /* renamed from: d */
    private final int f332d;

    /* renamed from: e */
    private final int f333e = 2;

    /* renamed from: f */
    private volatile Thread f334f;

    /* renamed from: g */
    private volatile boolean f335g;

    /* renamed from: h */
    private AudioRecord f336h;

    /* renamed from: i */
    private boolean f337i;

    C0101a(FMODAudioDevice fMODAudioDevice, int i, int i2) {
        this.f329a = fMODAudioDevice;
        this.f331c = i;
        this.f332d = i2;
        this.f330b = ByteBuffer.allocateDirect(AudioRecord.getMinBufferSize(i, i2, 2));
    }

    /* renamed from: d */
    private void m192d() {
        AudioRecord audioRecord = this.f336h;
        if (audioRecord != null) {
            if (audioRecord.getState() == 1) {
                this.f336h.stop();
            }
            this.f336h.release();
            this.f336h = null;
        }
        this.f330b.position(0);
        this.f337i = false;
    }

    /* renamed from: a */
    public final int mo373a() {
        return this.f330b.capacity();
    }

    /* renamed from: b */
    public final void mo374b() {
        if (this.f334f != null) {
            mo375c();
        }
        this.f335g = true;
        this.f334f = new Thread(this);
        this.f334f.start();
    }

    /* renamed from: c */
    public final void mo375c() {
        while (this.f334f != null) {
            this.f335g = false;
            try {
                this.f334f.join();
                this.f334f = null;
            } catch (InterruptedException unused) {
            }
        }
    }

    public final void run() {
        int i = 3;
        while (this.f335g) {
            if (!this.f337i && i > 0) {
                m192d();
                AudioRecord audioRecord = new AudioRecord(1, this.f331c, this.f332d, this.f333e, this.f330b.capacity());
                this.f336h = audioRecord;
                int state = audioRecord.getState();
                boolean z = true;
                if (state != 1) {
                    z = false;
                }
                this.f337i = z;
                if (z) {
                    this.f330b.position(0);
                    this.f336h.startRecording();
                    i = 3;
                } else {
                    Log.e("FMOD", "AudioRecord failed to initialize (status " + this.f336h.getState() + ")");
                    i += -1;
                    m192d();
                }
            }
            if (this.f337i && this.f336h.getRecordingState() == 3) {
                AudioRecord audioRecord2 = this.f336h;
                ByteBuffer byteBuffer = this.f330b;
                this.f329a.fmodProcessMicData(this.f330b, audioRecord2.read(byteBuffer, byteBuffer.capacity()));
                this.f330b.position(0);
            }
        }
        m192d();
    }
}
