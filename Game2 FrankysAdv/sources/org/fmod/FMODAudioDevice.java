package org.fmod;

import android.media.AudioTrack;
import android.util.Log;
import java.nio.ByteBuffer;

public class FMODAudioDevice implements Runnable {

    /* renamed from: h */
    private static int f351h = 0;

    /* renamed from: i */
    private static int f352i = 1;

    /* renamed from: j */
    private static int f353j = 2;

    /* renamed from: k */
    private static int f354k = 3;

    /* renamed from: l */
    private static int f355l = 4;

    /* renamed from: a */
    private volatile Thread f356a = null;

    /* renamed from: b */
    private volatile boolean f357b = false;

    /* renamed from: c */
    private AudioTrack f358c = null;

    /* renamed from: d */
    private boolean f359d = false;

    /* renamed from: e */
    private ByteBuffer f360e = null;

    /* renamed from: f */
    private byte[] f361f = null;

    /* renamed from: g */
    private volatile C0341a f362g;

    private native int fmodGetInfo(int i);

    private native int fmodProcess(ByteBuffer byteBuffer);

    private void releaseAudioTrack() {
        AudioTrack audioTrack = this.f358c;
        if (audioTrack != null) {
            if (audioTrack.getState() == 1) {
                this.f358c.stop();
            }
            this.f358c.release();
            this.f358c = null;
        }
        this.f360e = null;
        this.f361f = null;
        this.f359d = false;
    }

    public synchronized void close() {
        stop();
    }

    /* access modifiers changed from: package-private */
    public native int fmodProcessMicData(ByteBuffer byteBuffer, int i);

    public boolean isRunning() {
        return this.f356a != null && this.f356a.isAlive();
    }

    public void run() {
        int i = 3;
        while (this.f357b) {
            if (!this.f359d && i > 0) {
                releaseAudioTrack();
                int fmodGetInfo = fmodGetInfo(f351h);
                int i2 = fmodGetInfo(f355l) == 1 ? 4 : 12;
                int minBufferSize = AudioTrack.getMinBufferSize(fmodGetInfo, i2, 2);
                int fmodGetInfo2 = fmodGetInfo(f355l) * 2;
                int round = Math.round(((float) minBufferSize) * 1.1f) & (~(fmodGetInfo2 - 1));
                int fmodGetInfo3 = fmodGetInfo(f352i);
                int fmodGetInfo4 = fmodGetInfo(f353j) * fmodGetInfo3 * fmodGetInfo2;
                AudioTrack audioTrack = new AudioTrack(3, fmodGetInfo, i2, 2, fmodGetInfo4 > round ? fmodGetInfo4 : round, 1);
                this.f358c = audioTrack;
                boolean z = audioTrack.getState() == 1;
                this.f359d = z;
                if (z) {
                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(fmodGetInfo3 * fmodGetInfo2);
                    this.f360e = allocateDirect;
                    this.f361f = new byte[allocateDirect.capacity()];
                    this.f358c.play();
                    i = 3;
                } else {
                    Log.e("FMOD", "AudioTrack failed to initialize (status " + this.f358c.getState() + ")");
                    releaseAudioTrack();
                    i += -1;
                }
            }
            if (this.f359d) {
                if (fmodGetInfo(f354k) == 1) {
                    fmodProcess(this.f360e);
                    ByteBuffer byteBuffer = this.f360e;
                    byteBuffer.get(this.f361f, 0, byteBuffer.capacity());
                    this.f358c.write(this.f361f, 0, this.f360e.capacity());
                    this.f360e.position(0);
                } else {
                    releaseAudioTrack();
                }
            }
        }
        releaseAudioTrack();
    }

    public synchronized void start() {
        if (this.f356a != null) {
            stop();
        }
        this.f356a = new Thread(this, "FMODAudioDevice");
        this.f356a.setPriority(10);
        this.f357b = true;
        this.f356a.start();
        if (this.f362g != null) {
            this.f362g.mo1355b();
        }
    }

    public synchronized int startAudioRecord(int i, int i2, int i3) {
        if (this.f362g == null) {
            this.f362g = new C0341a(this, i, i2);
            this.f362g.mo1355b();
        }
        return this.f362g.mo1354a();
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
            java.lang.Thread r0 = r1.f356a     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x0011
            r0 = 0
            r1.f357b = r0     // Catch:{ all -> 0x001c }
            java.lang.Thread r0 = r1.f356a     // Catch:{ InterruptedException -> 0x0001 }
            r0.join()     // Catch:{ InterruptedException -> 0x0001 }
            r0 = 0
            r1.f356a = r0     // Catch:{ InterruptedException -> 0x0001 }
            goto L_0x0001
        L_0x0011:
            org.fmod.a r0 = r1.f362g     // Catch:{ all -> 0x001c }
            if (r0 == 0) goto L_0x001a
            org.fmod.a r0 = r1.f362g     // Catch:{ all -> 0x001c }
            r0.mo1356c()     // Catch:{ all -> 0x001c }
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
        if (this.f362g != null) {
            this.f362g.mo1356c();
            this.f362g = null;
        }
    }
}
