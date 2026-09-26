package org.fmod;

import android.media.AudioTrack;
import android.util.Log;
import java.nio.ByteBuffer;

public class FMODAudioDevice implements Runnable {

    /* renamed from: h */
    private static int f317h = 0;

    /* renamed from: i */
    private static int f318i = 1;

    /* renamed from: j */
    private static int f319j = 2;

    /* renamed from: k */
    private static int f320k = 3;

    /* renamed from: l */
    private static int f321l = 4;

    /* renamed from: a */
    private volatile Thread f322a = null;

    /* renamed from: b */
    private volatile boolean f323b = false;

    /* renamed from: c */
    private AudioTrack f324c = null;

    /* renamed from: d */
    private boolean f325d = false;

    /* renamed from: e */
    private ByteBuffer f326e = null;

    /* renamed from: f */
    private byte[] f327f = null;

    /* renamed from: g */
    private volatile C0101a f328g;

    private native int fmodGetInfo(int i);

    private native int fmodProcess(ByteBuffer byteBuffer);

    private void releaseAudioTrack() {
        AudioTrack audioTrack = this.f324c;
        if (audioTrack != null) {
            if (audioTrack.getState() == 1) {
                this.f324c.stop();
            }
            this.f324c.release();
            this.f324c = null;
        }
        this.f326e = null;
        this.f327f = null;
        this.f325d = false;
    }

    public synchronized void close() {
        stop();
    }

    /* access modifiers changed from: package-private */
    public native int fmodProcessMicData(ByteBuffer byteBuffer, int i);

    public boolean isRunning() {
        return this.f322a != null && this.f322a.isAlive();
    }

    public void run() {
        int i = 3;
        while (this.f323b) {
            if (!this.f325d && i > 0) {
                releaseAudioTrack();
                int fmodGetInfo = fmodGetInfo(f317h);
                int i2 = fmodGetInfo(f321l) == 1 ? 4 : 12;
                int minBufferSize = AudioTrack.getMinBufferSize(fmodGetInfo, i2, 2);
                int fmodGetInfo2 = fmodGetInfo(f321l) * 2;
                int round = Math.round(((float) minBufferSize) * 1.1f) & (~(fmodGetInfo2 - 1));
                int fmodGetInfo3 = fmodGetInfo(f318i);
                int fmodGetInfo4 = fmodGetInfo(f319j) * fmodGetInfo3 * fmodGetInfo2;
                AudioTrack audioTrack = new AudioTrack(3, fmodGetInfo, i2, 2, fmodGetInfo4 > round ? fmodGetInfo4 : round, 1);
                this.f324c = audioTrack;
                boolean z = audioTrack.getState() == 1;
                this.f325d = z;
                if (z) {
                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fmodGetInfo3 * fmodGetInfo2);
                    this.f326e = allocateDirect;
                    this.f327f = new byte[allocateDirect.capacity()];
                    this.f324c.play();
                    i = 3;
                } else {
                    Log.e("FMOD", "AudioTrack failed to initialize (status " + this.f324c.getState() + ")");
                    releaseAudioTrack();
                    i += -1;
                }
            }
            if (this.f325d) {
                if (fmodGetInfo(f320k) == 1) {
                    fmodProcess(this.f326e);
                    ByteBuffer byteBuffer = this.f326e;
                    byteBuffer.get(this.f327f, 0, byteBuffer.capacity());
                    this.f324c.write(this.f327f, 0, this.f326e.capacity());
                    this.f326e.position(0);
                } else {
                    releaseAudioTrack();
                }
            }
        }
        releaseAudioTrack();
    }

    public synchronized void start() {
        if (this.f322a != null) {
            stop();
        }
        this.f322a = new Thread(this, "FMODAudioDevice");
        this.f322a.setPriority(10);
        this.f323b = true;
        this.f322a.start();
        if (this.f328g != null) {
            this.f328g.mo374b();
        }
    }

    public synchronized int startAudioRecord(int i, int i2, int i3) {
        if (this.f328g == null) {
            this.f328g = new C0101a(this, i, i2);
            this.f328g.mo374b();
        }
        return this.f328g.mo373a();
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
            java.lang.Thread r0 = r1.f322a     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x0011
            r0 = 0
            r1.f323b = r0     // Catch:{ all -> 0x001c }
            java.lang.Thread r0 = r1.f322a     // Catch:{ InterruptedException -> 0x0001 }
            r0.join()     // Catch:{ InterruptedException -> 0x0001 }
            r0 = 0
            r1.f322a = r0     // Catch:{ InterruptedException -> 0x0001 }
            goto L_0x0001
        L_0x0011:
            org.fmod.a r0 = r1.f328g     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x001a
            org.fmod.a r0 = r1.f328g     // Catch:{ all -> 0x001c }
            r0.mo375c()     // Catch:{ all -> 0x001c }
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
        if (this.f328g != null) {
            this.f328g.mo375c();
            this.f328g = null;
        }
    }
}
