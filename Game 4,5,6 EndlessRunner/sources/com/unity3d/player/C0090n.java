package com.unity3d.player;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;

/* renamed from: com.unity3d.player.n */
public final class C0090n extends FrameLayout implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, MediaController.MediaPlayerControl {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public static boolean f266a = false;

    /* renamed from: b */
    private final Context f267b;

    /* renamed from: c */
    private final SurfaceView f268c;

    /* renamed from: d */
    private final SurfaceHolder f269d;

    /* renamed from: e */
    private final String f270e;

    /* renamed from: f */
    private final int f271f;

    /* renamed from: g */
    private final int f272g;

    /* renamed from: h */
    private final boolean f273h;

    /* renamed from: i */
    private final long f274i;

    /* renamed from: j */
    private final long f275j;

    /* renamed from: k */
    private final FrameLayout f276k;

    /* renamed from: l */
    private final Display f277l;

    /* renamed from: m */
    private int f278m;

    /* renamed from: n */
    private int f279n;

    /* renamed from: o */
    private int f280o;

    /* renamed from: p */
    private int f281p;

    /* renamed from: q */
    private MediaPlayer f282q;

    /* renamed from: r */
    private MediaController f283r;

    /* renamed from: s */
    private boolean f284s = false;

    /* renamed from: t */
    private boolean f285t = false;

    /* renamed from: u */
    private int f286u = 0;

    /* renamed from: v */
    private boolean f287v = false;

    /* renamed from: w */
    private boolean f288w = false;

    /* renamed from: x */
    private C0091a f289x;

    /* renamed from: y */
    private C0092b f290y;

    /* renamed from: z */
    private volatile int f291z = 0;

    /* renamed from: com.unity3d.player.n$a */
    public interface C0091a {
        /* renamed from: a */
        void mo352a(int i);
    }

    /* renamed from: com.unity3d.player.n$b */
    public class C0092b implements Runnable {

        /* renamed from: b */
        private C0090n f293b;

        /* renamed from: c */
        private boolean f294c = false;

        public C0092b(C0090n nVar) {
            this.f293b = nVar;
        }

        /* renamed from: a */
        public final void mo353a() {
            this.f294c = true;
        }

        public final void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
            if (!this.f294c) {
                if (C0090n.f266a) {
                    C0090n.m168b("Stopping the video player due to timeout.");
                }
                this.f293b.CancelOnPrepare();
            }
        }
    }

    protected C0090n(Context context, String str, int i, int i2, int i3, boolean z, long j, long j2, C0091a aVar) {
        super(context);
        this.f289x = aVar;
        this.f267b = context;
        this.f276k = this;
        SurfaceView surfaceView = new SurfaceView(context);
        this.f268c = surfaceView;
        SurfaceHolder holder = surfaceView.getHolder();
        this.f269d = holder;
        holder.addCallback(this);
        this.f276k.setBackgroundColor(i);
        this.f276k.addView(this.f268c);
        this.f277l = ((WindowManager) this.f267b.getSystemService("window")).getDefaultDisplay();
        this.f270e = str;
        this.f271f = i2;
        this.f272g = i3;
        this.f273h = z;
        this.f274i = j;
        this.f275j = j2;
        if (f266a) {
            m168b("fileName: " + this.f270e);
        }
        if (f266a) {
            m168b("backgroundColor: " + i);
        }
        if (f266a) {
            m168b("controlMode: " + this.f271f);
        }
        if (f266a) {
            m168b("scalingMode: " + this.f272g);
        }
        if (f266a) {
            m168b("isURL: " + this.f273h);
        }
        if (f266a) {
            m168b("videoOffset: " + this.f274i);
        }
        if (f266a) {
            m168b("videoLength: " + this.f275j);
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    /* renamed from: a */
    private void m166a(int i) {
        this.f291z = i;
        C0091a aVar = this.f289x;
        if (aVar != null) {
            aVar.mo352a(this.f291z);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: b */
    public static void m168b(String str) {
        Log.i("Video", "VideoPlayer: " + str);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:17|18|19|20|21) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x007d */
    /* renamed from: c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m170c() {
        /*
            r8 = this;
            android.media.MediaPlayer r0 = r8.f282q
            if (r0 == 0) goto L_0x001c
            android.view.SurfaceHolder r1 = r8.f269d
            r0.setDisplay(r1)
            boolean r0 = r8.f287v
            if (r0 != 0) goto L_0x001b
            boolean r0 = f266a
            if (r0 == 0) goto L_0x0016
            java.lang.String r0 = "Resuming playback"
            m168b(r0)
        L_0x0016:
            android.media.MediaPlayer r0 = r8.f282q
            r0.start()
        L_0x001b:
            return
        L_0x001c:
            r0 = 0
            r8.m166a((int) r0)
            r8.doCleanUp()
            android.media.MediaPlayer r0 = new android.media.MediaPlayer     // Catch:{ Exception -> 0x00cc }
            r0.<init>()     // Catch:{ Exception -> 0x00cc }
            r8.f282q = r0     // Catch:{ Exception -> 0x00cc }
            boolean r1 = r8.f273h     // Catch:{ Exception -> 0x00cc }
            if (r1 == 0) goto L_0x003a
            android.content.Context r1 = r8.f267b     // Catch:{ Exception -> 0x00cc }
            java.lang.String r2 = r8.f270e     // Catch:{ Exception -> 0x00cc }
            android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch:{ Exception -> 0x00cc }
            r0.setDataSource(r1, r2)     // Catch:{ Exception -> 0x00cc }
            goto L_0x008e
        L_0x003a:
            long r0 = r8.f275j     // Catch:{ Exception -> 0x00cc }
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 == 0) goto L_0x005a
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00cc }
            java.lang.String r1 = r8.f270e     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r2 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            java.io.FileDescriptor r3 = r0.getFD()     // Catch:{ Exception -> 0x00cc }
            long r4 = r8.f274i     // Catch:{ Exception -> 0x00cc }
            long r6 = r8.f275j     // Catch:{ Exception -> 0x00cc }
            r2.setDataSource(r3, r4, r6)     // Catch:{ Exception -> 0x00cc }
        L_0x0056:
            r0.close()     // Catch:{ Exception -> 0x00cc }
            goto L_0x008e
        L_0x005a:
            android.content.res.Resources r0 = r8.getResources()     // Catch:{ Exception -> 0x00cc }
            android.content.res.AssetManager r0 = r0.getAssets()     // Catch:{ Exception -> 0x00cc }
            java.lang.String r1 = r8.f270e     // Catch:{ IOException -> 0x007d }
            android.content.res.AssetFileDescriptor r0 = r0.openFd(r1)     // Catch:{ IOException -> 0x007d }
            android.media.MediaPlayer r1 = r8.f282q     // Catch:{ IOException -> 0x007d }
            java.io.FileDescriptor r2 = r0.getFileDescriptor()     // Catch:{ IOException -> 0x007d }
            long r3 = r0.getStartOffset()     // Catch:{ IOException -> 0x007d }
            long r5 = r0.getLength()     // Catch:{ IOException -> 0x007d }
            r1.setDataSource(r2, r3, r5)     // Catch:{ IOException -> 0x007d }
            r0.close()     // Catch:{ IOException -> 0x007d }
            goto L_0x008e
        L_0x007d:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00cc }
            java.lang.String r1 = r8.f270e     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r1 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            java.io.FileDescriptor r2 = r0.getFD()     // Catch:{ Exception -> 0x00cc }
            r1.setDataSource(r2)     // Catch:{ Exception -> 0x00cc }
            goto L_0x0056
        L_0x008e:
            android.media.MediaPlayer r0 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            android.view.SurfaceHolder r1 = r8.f269d     // Catch:{ Exception -> 0x00cc }
            r0.setDisplay(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            r1 = 1
            r0.setScreenOnWhilePlaying(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            r0.setOnBufferingUpdateListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            r0.setOnCompletionListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            r0.setOnPreparedListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            r0.setOnVideoSizeChangedListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            r1 = 3
            r0.setAudioStreamType(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f282q     // Catch:{ Exception -> 0x00cc }
            r0.prepareAsync()     // Catch:{ Exception -> 0x00cc }
            com.unity3d.player.n$b r0 = new com.unity3d.player.n$b     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r8)     // Catch:{ Exception -> 0x00cc }
            r8.f290y = r0     // Catch:{ Exception -> 0x00cc }
            java.lang.Thread r0 = new java.lang.Thread     // Catch:{ Exception -> 0x00cc }
            com.unity3d.player.n$b r1 = r8.f290y     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00cc }
            r0.start()     // Catch:{ Exception -> 0x00cc }
            return
        L_0x00cc:
            r0 = move-exception
            boolean r1 = f266a
            if (r1 == 0) goto L_0x00e9
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "error: "
            r1.<init>(r2)
            java.lang.String r2 = r0.getMessage()
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            m168b(r0)
        L_0x00e9:
            r0 = 2
            r8.m166a((int) r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.C0090n.m170c():void");
    }

    /* renamed from: d */
    private void m171d() {
        if (!isPlaying()) {
            m166a(1);
            if (f266a) {
                m168b("startVideoPlayback");
            }
            updateVideoLayout();
            if (!this.f287v) {
                start();
            }
        }
    }

    public final void CancelOnPrepare() {
        m166a(2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public final boolean mo328a() {
        return this.f287v;
    }

    public final boolean canPause() {
        return true;
    }

    public final boolean canSeekBackward() {
        return true;
    }

    public final boolean canSeekForward() {
        return true;
    }

    /* access modifiers changed from: protected */
    public final void destroyPlayer() {
        if (f266a) {
            m168b("destroyPlayer");
        }
        if (!this.f287v) {
            pause();
        }
        doCleanUp();
    }

    /* access modifiers changed from: protected */
    public final void doCleanUp() {
        C0092b bVar = this.f290y;
        if (bVar != null) {
            bVar.mo353a();
            this.f290y = null;
        }
        MediaPlayer mediaPlayer = this.f282q;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            this.f282q = null;
        }
        this.f280o = 0;
        this.f281p = 0;
        this.f285t = false;
        this.f284s = false;
    }

    public final int getAudioSessionId() {
        MediaPlayer mediaPlayer = this.f282q;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getAudioSessionId();
    }

    public final int getBufferPercentage() {
        if (this.f273h) {
            return this.f286u;
        }
        return 100;
    }

    public final int getCurrentPosition() {
        MediaPlayer mediaPlayer = this.f282q;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    public final int getDuration() {
        MediaPlayer mediaPlayer = this.f282q;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getDuration();
    }

    public final boolean isPlaying() {
        boolean z = this.f285t && this.f284s;
        MediaPlayer mediaPlayer = this.f282q;
        return mediaPlayer == null ? !z : mediaPlayer.isPlaying() || !z;
    }

    public final void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        if (f266a) {
            m168b("onBufferingUpdate percent:" + i);
        }
        this.f286u = i;
    }

    public final void onCompletion(MediaPlayer mediaPlayer) {
        if (f266a) {
            m168b("onCompletion called");
        }
        destroyPlayer();
        m166a(3);
    }

    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 || (this.f271f == 2 && i != 0 && !keyEvent.isSystem())) {
            destroyPlayer();
            m166a(3);
            return true;
        }
        MediaController mediaController = this.f283r;
        return mediaController != null ? mediaController.onKeyDown(i, keyEvent) : super.onKeyDown(i, keyEvent);
    }

    public final void onPrepared(MediaPlayer mediaPlayer) {
        if (f266a) {
            m168b("onPrepared called");
        }
        C0092b bVar = this.f290y;
        if (bVar != null) {
            bVar.mo353a();
            this.f290y = null;
        }
        int i = this.f271f;
        if (i == 0 || i == 1) {
            MediaController mediaController = new MediaController(this.f267b);
            this.f283r = mediaController;
            mediaController.setMediaPlayer(this);
            this.f283r.setAnchorView(this);
            this.f283r.setEnabled(true);
            Context context = this.f267b;
            if (context instanceof Activity) {
                this.f283r.setSystemUiVisibility(((Activity) context).getWindow().getDecorView().getSystemUiVisibility());
            }
            this.f283r.show();
        }
        this.f285t = true;
        if (1 != 0 && this.f284s) {
            m171d();
        }
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (this.f271f == 2 && action == 0) {
            destroyPlayer();
            m166a(3);
            return true;
        }
        MediaController mediaController = this.f283r;
        return mediaController != null ? mediaController.onTouchEvent(motionEvent) : super.onTouchEvent(motionEvent);
    }

    public final void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        if (f266a) {
            m168b("onVideoSizeChanged called " + i + "x" + i2);
        }
        if (i != 0 && i2 != 0) {
            this.f284s = true;
            this.f280o = i;
            this.f281p = i2;
            if (this.f285t && 1 != 0) {
                m171d();
            }
        } else if (f266a) {
            m168b("invalid video width(" + i + ") or height(" + i2 + ")");
        }
    }

    public final void pause() {
        MediaPlayer mediaPlayer = this.f282q;
        if (mediaPlayer != null) {
            if (this.f288w) {
                mediaPlayer.pause();
            }
            this.f287v = true;
        }
    }

    public final void seekTo(int i) {
        MediaPlayer mediaPlayer = this.f282q;
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(i);
        }
    }

    public final void start() {
        if (f266a) {
            m168b("Start");
        }
        MediaPlayer mediaPlayer = this.f282q;
        if (mediaPlayer != null) {
            if (this.f288w) {
                mediaPlayer.start();
            }
            this.f287v = false;
        }
    }

    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (f266a) {
            m168b("surfaceChanged called " + i + " " + i2 + "x" + i3);
        }
        if (this.f278m != i2 || this.f279n != i3) {
            this.f278m = i2;
            this.f279n = i3;
            if (this.f288w) {
                updateVideoLayout();
            }
        }
    }

    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (f266a) {
            m168b("surfaceCreated called");
        }
        this.f288w = true;
        m170c();
    }

    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (f266a) {
            m168b("surfaceDestroyed called");
        }
        this.f288w = false;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004d, code lost:
        if (r5 <= r3) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0053, code lost:
        r0 = (int) (((float) r1) * r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005d, code lost:
        if (r5 >= r3) goto L_0x004f;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void updateVideoLayout() {
        /*
            r8 = this;
            boolean r0 = f266a
            if (r0 == 0) goto L_0x0009
            java.lang.String r0 = "updateVideoLayout"
            m168b(r0)
        L_0x0009:
            android.media.MediaPlayer r0 = r8.f282q
            if (r0 != 0) goto L_0x000e
            return
        L_0x000e:
            int r0 = r8.f278m
            if (r0 == 0) goto L_0x0016
            int r0 = r8.f279n
            if (r0 != 0) goto L_0x0034
        L_0x0016:
            android.content.Context r0 = r8.f267b
            java.lang.String r1 = "window"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.view.WindowManager r0 = (android.view.WindowManager) r0
            android.util.DisplayMetrics r1 = new android.util.DisplayMetrics
            r1.<init>()
            android.view.Display r0 = r0.getDefaultDisplay()
            r0.getMetrics(r1)
            int r0 = r1.widthPixels
            r8.f278m = r0
            int r0 = r1.heightPixels
            r8.f279n = r0
        L_0x0034:
            int r0 = r8.f278m
            int r1 = r8.f279n
            boolean r2 = r8.f284s
            if (r2 == 0) goto L_0x0065
            int r2 = r8.f280o
            float r3 = (float) r2
            int r4 = r8.f281p
            float r5 = (float) r4
            float r3 = r3 / r5
            float r5 = (float) r0
            float r6 = (float) r1
            float r5 = r5 / r6
            int r6 = r8.f272g
            r7 = 1
            if (r6 != r7) goto L_0x0058
            int r2 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r2 > 0) goto L_0x0053
        L_0x004f:
            float r1 = (float) r0
            float r1 = r1 / r3
            int r1 = (int) r1
            goto L_0x006e
        L_0x0053:
            float r0 = (float) r1
            float r0 = r0 * r3
            int r0 = (int) r0
            goto L_0x006e
        L_0x0058:
            r7 = 2
            if (r6 != r7) goto L_0x0060
            int r2 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r2 < 0) goto L_0x0053
            goto L_0x004f
        L_0x0060:
            if (r6 != 0) goto L_0x006e
            r0 = r2
            r1 = r4
            goto L_0x006e
        L_0x0065:
            boolean r2 = f266a
            if (r2 == 0) goto L_0x006e
            java.lang.String r2 = "updateVideoLayout: Video size is not known yet"
            m168b(r2)
        L_0x006e:
            int r2 = r8.f278m
            if (r2 != r0) goto L_0x0076
            int r2 = r8.f279n
            if (r2 == r1) goto L_0x00a1
        L_0x0076:
            boolean r2 = f266a
            if (r2 == 0) goto L_0x0093
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "frameWidth = "
            r2.<init>(r3)
            r2.append(r0)
            java.lang.String r3 = "; frameHeight = "
            r2.append(r3)
            r2.append(r1)
            java.lang.String r2 = r2.toString()
            m168b(r2)
        L_0x0093:
            android.widget.FrameLayout$LayoutParams r2 = new android.widget.FrameLayout$LayoutParams
            r3 = 17
            r2.<init>(r0, r1, r3)
            android.widget.FrameLayout r0 = r8.f276k
            android.view.SurfaceView r1 = r8.f268c
            r0.updateViewLayout(r1, r2)
        L_0x00a1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.C0090n.updateVideoLayout():void");
    }
}
