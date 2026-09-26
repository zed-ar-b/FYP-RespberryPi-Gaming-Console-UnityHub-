package org.fmod;

import android.media.AudioTrack;
import android.util.Log;
import java.nio.ByteBuffer;

public class FMODAudioDevice implements Runnable {

    /* renamed from: h */
    private static int f316h = 0;

    /* renamed from: i */
    private static int f317i = 1;

    /* renamed from: j */
    private static int f318j = 2;

    /* renamed from: k */
    private static int f319k = 3;

    /* renamed from: l */
    private static int f320l = 4;

    /* renamed from: a */
    private volatile Thread f321a = null;

    /* renamed from: b */
    private volatile boolean f322b = false;

    /* renamed from: c */
    private AudioTrack f323c = null;

    /* renamed from: d */
    private boolean f324d = false;

    /* renamed from: e */
    private ByteBuffer f325e = null;

    /* renamed from: f */
    private byte[] f326f = null;

    /* renamed from: g */
    private volatile C0168a f327g;

    private native int fmodGetInfo(int i);

    private native int fmodProcess(ByteBuffer byteBuffer);

    private void releaseAudioTrack() {
        AudioTrack audioTrack = this.f323c;
        if (audioTrack != null) {
            if (audioTrack.getState() == 1) {
                this.f323c.stop();
            }
            this.f323c.release();
            this.f323c = null;
        }
        this.f325e = null;
        this.f326f = null;
        this.f324d = false;
    }

    public synchronized void close() {
        stop();
    }

    /* access modifiers changed from: package-private */
    public native int fmodProcessMicData(ByteBuffer byteBuffer, int i);

    public boolean isRunning() {
        return this.f321a != null && this.f321a.isAlive();
    }

    public void run() {
        int i = 3;
        while (this.f322b) {
            if (!this.f324d && i > 0) {
                releaseAudioTrack();
                int fmodGetInfo = fmodGetInfo(f316h);
                int i2 = fmodGetInfo(f320l) == 1 ? 4 : 12;
                int minBufferSize = AudioTrack.getMinBufferSize(fmodGetInfo, i2, 2);
                int fmodGetInfo2 = fmodGetInfo(f320l) * 2;
                int round = Math.round(((float) minBufferSize) * 1.1f) & (~(fmodGetInfo2 - 1));
                int fmodGetInfo3 = fmodGetInfo(f317i);
                int fmodGetInfo4 = fmodGetInfo(f318j) * fmodGetInfo3 * fmodGetInfo2;
                AudioTrack audioTrack = new AudioTrack(3, fmodGetInfo, i2, 2, fmodGetInfo4 > round ? fmodGetInfo4 : round, 1);
                this.f323c = audioTrack;
                boolean z = audioTrack.getState() == 1;
                this.f324d = z;
                if (z) {
                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fmodGetInfo3 * fmodGetInfo2);
                    this.f325e = allocateDirect;
                    this.f326f = new byte[allocateDirect.capacity()];
                    this.f323c.play();
                    i = 3;
                } else {
                    Log.e("FMOD", "AudioTrack failed to initialize (status " + this.f323c.getState() + ")");
                    releaseAudioTrack();
                    i += -1;
                }
            }
            if (this.f324d) {
                if (fmodGetInfo(f319k) == 1) {
                    fmodProcess(this.f325e);
                    ByteBuffer byteBuffer = this.f325e;
                    byteBuffer.get(this.f326f, 0, byteBuffer.capacity());
                    this.f323c.write(this.f326f, 0, this.f325e.capacity());
                    this.f325e.position(0);
                } else {
                    releaseAudioTrack();
                }
            }
        }
        releaseAudioTrack();
    }

    public synchronized void start() {
        if (this.f321a != null) {
            stop();
        }
        this.f321a = new Thread(this, "FMODAudioDevice");
        this.f321a.setPriority(10);
        this.f322b = true;
        this.f321a.start();
        if (this.f327g != null) {
            this.f327g.mo472b();
        }
    }

    public synchronized int startAudioRecord(int i, int i2, int i3) {
        if (this.f327g == null) {
            this.f327g = new C0168a(this, i, i2);
            this.f327g.mo472b();
        }
        return this.f327g.mo471a();
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:1:0x0001 */
    /* JADX WARNING: Removed duplicated region for block: B:1:0x0001 A[LOOP:0: B:1:0x0001->B:16:0x0001, LOOP_START, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void stop() {
        /*
            r1 = this;
            monitor-enter(r1)
        L_0x0001:
            java.lang.Thread r0 = r1.f321a     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x0011
            r0 = 0
            r1.f322b = r0     // Catch:{ all -> 0x001c }
            java.lang.Thread r0 = r1.f321a     // Catch:{ InterruptedException -> 0x0001 }
            r0.join()     // Catch:{ InterruptedException -> 0x0001 }
            r0 = 0
            r1.f321a = r0     // Catch:{ InterruptedException -> 0x0001 }
            goto L_0x0001
        L_0x0011:
            org.fmod.a r0 = r1.f327g     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x001a
            org.fmod.a r0 = r1.f327g     // Catch:{ all -> 0x001c }
            r0.mo473c()     // Catch:{ all -> 0x001c }
        L_0x001a:
            monitor-exit(r1)
            return
        L_0x001c:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.fmod.FMODAudioDevice.stop():void");
    }

    public synchronized void stopAudioRecord() {
        if (this.f327g != null) {
            this.f327g.mo473c();
            this.f327g = null;
        }
    }
}
