package org.fmod;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;

/* renamed from: org.fmod.a */
final class C0168a implements Runnable {

    /* renamed from: a */
    private final FMODAudioDevice f328a;

    /* renamed from: b */
    private final ByteBuffer f329b;

    /* renamed from: c */
    private final int f330c;

    /* renamed from: d */
    private final int f331d;

    /* renamed from: e */
    private final int f332e = 2;

    /* renamed from: f */
    private volatile Thread f333f;

    /* renamed from: g */
    private volatile boolean f334g;

    /* renamed from: h */
    private AudioRecord f335h;

    /* renamed from: i */
    private boolean f336i;

    C0168a(FMODAudioDevice fMODAudioDevice, int i, int i2) {
        this.f328a = fMODAudioDevice;
        this.f330c = i;
        this.f331d = i2;
        this.f329b = ByteBuffer.allocateDirect(AudioRecord.getMinBufferSize(i, i2, 2));
    }

    /* renamed from: d */
    private void m192d() {
        AudioRecord audioRecord = this.f335h;
        if (audioRecord != null) {
            if (audioRecord.getState() == 1) {
                this.f335h.stop();
            }
            this.f335h.release();
            this.f335h = null;
        }
        this.f329b.position(0);
        this.f336i = false;
    }

    /* renamed from: a */
    public final int mo471a() {
        return this.f329b.capacity();
    }

    /* renamed from: b */
    public final void mo472b() {
        if (this.f333f != null) {
            mo473c();
        }
        this.f334g = true;
        this.f333f = new Thread(this);
        this.f333f.start();
    }

    /* renamed from: c */
    public final void mo473c() {
        while (this.f333f != null) {
            this.f334g = false;
            try {
                this.f333f.join();
                this.f333f = null;
            } catch (InterruptedException unused) {
            }
        }
    }

    public final void run() {
        int i = 3;
        while (this.f334g) {
            if (!this.f336i && i > 0) {
                m192d();
                AudioRecord audioRecord = new AudioRecord(1, this.f330c, this.f331d, this.f332e, this.f329b.capacity());
                this.f335h = audioRecord;
                int state = audioRecord.getState();
                boolean z = true;
                if (state != 1) {
                    z = false;
                }
                this.f336i = z;
                if (z) {
                    this.f329b.position(0);
                    this.f335h.startRecording();
                    i = 3;
                } else {
                    Log.e("FMOD", "AudioRecord failed to initialize (status " + this.f335h.getState() + ")");
                    i += -1;
                    m192d();
                }
            }
            if (this.f336i && this.f335h.getRecordingState() == 3) {
                AudioRecord audioRecord2 = this.f335h;
                ByteBuffer byteBuffer = this.f329b;
                this.f328a.fmodProcessMicData(this.f329b, audioRecord2.read(byteBuffer, byteBuffer.capacity()));
                this.f329b.position(0);
            }
        }
        m192d();
    }
}
