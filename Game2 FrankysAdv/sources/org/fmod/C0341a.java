package org.fmod;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;

/* renamed from: org.fmod.a */
final class C0341a implements Runnable {

    /* renamed from: a */
    private final FMODAudioDevice f363a;

    /* renamed from: b */
    private final ByteBuffer f364b;

    /* renamed from: c */
    private final int f365c;

    /* renamed from: d */
    private final int f366d;

    /* renamed from: e */
    private final int f367e = 2;

    /* renamed from: f */
    private volatile Thread f368f;

    /* renamed from: g */
    private volatile boolean f369g;

    /* renamed from: h */
    private AudioRecord f370h;

    /* renamed from: i */
    private boolean f371i;

    C0341a(FMODAudioDevice fMODAudioDevice, int i, int i2) {
        this.f363a = fMODAudioDevice;
        this.f365c = i;
        this.f366d = i2;
        this.f364b = ByteBuffer.allocateDirect(AudioRecord.getMinBufferSize(i, i2, 2));
    }

    /* renamed from: d */
    private void m192d() {
        AudioRecord audioRecord = this.f370h;
        if (audioRecord != null) {
            if (audioRecord.getState() == 1) {
                this.f370h.stop();
            }
            this.f370h.release();
            this.f370h = null;
        }
        this.f364b.position(0);
        this.f371i = false;
    }

    /* renamed from: a */
    public final int mo1354a() {
        return this.f364b.capacity();
    }

    /* renamed from: b */
    public final void mo1355b() {
        if (this.f368f != null) {
            mo1356c();
        }
        this.f369g = true;
        this.f368f = new Thread(this);
        this.f368f.start();
    }

    /* renamed from: c */
    public final void mo1356c() {
        while (this.f368f != null) {
            this.f369g = false;
            try {
                this.f368f.join();
                this.f368f = null;
            } catch (InterruptedException unused) {
            }
        }
    }

    public final void run() {
        int i = 3;
        while (this.f369g) {
            if (!this.f371i && i > 0) {
                m192d();
                AudioRecord audioRecord = new AudioRecord(1, this.f365c, this.f366d, this.f367e, this.f364b.capacity());
                this.f370h = audioRecord;
                int state = audioRecord.getState();
                boolean z = true;
                if (state != 1) {
                    z = false;
                }
                this.f371i = z;
                if (z) {
                    this.f364b.position(0);
                    this.f370h.startRecording();
                    i = 3;
                } else {
                    Log.e("FMOD", "AudioRecord failed to initialize (status " + this.f370h.getState() + ")");
                    i += -1;
                    m192d();
                }
            }
            if (this.f371i && this.f370h.getRecordingState() == 3) {
                AudioRecord audioRecord2 = this.f370h;
                ByteBuffer byteBuffer = this.f364b;
                this.f363a.fmodProcessMicData(this.f364b, audioRecord2.read(byteBuffer, byteBuffer.capacity()));
                this.f364b.position(0);
            }
        }
        m192d();
    }
}
