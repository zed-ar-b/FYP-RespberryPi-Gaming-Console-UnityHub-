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
public final class C0157n extends FrameLayout implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, MediaController.MediaPlayerControl {
    /* access modifiers changed from: private */

    /* renamed from: a */
    public static boolean f265a = false;

    /* renamed from: b */
    private final Context f266b;

    /* renamed from: c */
    private final SurfaceView f267c;

    /* renamed from: d */
    private final SurfaceHolder f268d;

    /* renamed from: e */
    private final String f269e;

    /* renamed from: f */
    private final int f270f;

    /* renamed from: g */
    private final int f271g;

    /* renamed from: h */
    private final boolean f272h;

    /* renamed from: i */
    private final long f273i;

    /* renamed from: j */
    private final long f274j;

    /* renamed from: k */
    private final FrameLayout f275k;

    /* renamed from: l */
    private final Display f276l;

    /* renamed from: m */
    private int f277m;

    /* renamed from: n */
    private int f278n;

    /* renamed from: o */
    private int f279o;

    /* renamed from: p */
    private int f280p;

    /* renamed from: q */
    private MediaPlayer f281q;

    /* renamed from: r */
    private MediaController f282r;

    /* renamed from: s */
    private boolean f283s = false;

    /* renamed from: t */
    private boolean f284t = false;

    /* renamed from: u */
    private int f285u = 0;

    /* renamed from: v */
    private boolean f286v = false;

    /* renamed from: w */
    private boolean f287w = false;

    /* renamed from: x */
    private C0158a f288x;

    /* renamed from: y */
    private C0159b f289y;

    /* renamed from: z */
    private volatile int f290z = 0;

    /* renamed from: com.unity3d.player.n$a */
    public interface C0158a {
        /* renamed from: a */
        void mo450a(int i);
    }

    /* renamed from: com.unity3d.player.n$b */
    public class C0159b implements Runnable {

        /* renamed from: b */
        private C0157n f292b;

        /* renamed from: c */
        private boolean f293c = false;

        public C0159b(C0157n nVar) {
            this.f292b = nVar;
        }

        /* renamed from: a */
        public final void mo451a() {
            this.f293c = true;
        }

        public final void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException unused) {
                Thread.currentThread().interrupt();
            }
            if (!this.f293c) {
                if (C0157n.f265a) {
                    C0157n.m168b("Stopping the video player due to timeout.");
                }
                this.f292b.CancelOnPrepare();
            }
        }
    }

    protected C0157n(Context context, String str, int i, int i2, int i3, boolean z, long j, long j2, C0158a aVar) {
        super(context);
        this.f288x = aVar;
        this.f266b = context;
        this.f275k = this;
        SurfaceView surfaceView = new SurfaceView(context);
        this.f267c = surfaceView;
        SurfaceHolder holder = surfaceView.getHolder();
        this.f268d = holder;
        holder.addCallback(this);
        this.f275k.setBackgroundColor(i);
        this.f275k.addView(this.f267c);
        this.f276l = ((WindowManager) this.f266b.getSystemService("window")).getDefaultDisplay();
        this.f269e = str;
        this.f270f = i2;
        this.f271g = i3;
        this.f272h = z;
        this.f273i = j;
        this.f274j = j2;
        if (f265a) {
            m168b("fileName: " + this.f269e);
        }
        if (f265a) {
            m168b("backgroundColor: " + i);
        }
        if (f265a) {
            m168b("controlMode: " + this.f270f);
        }
        if (f265a) {
            m168b("scalingMode: " + this.f271g);
        }
        if (f265a) {
            m168b("isURL: " + this.f272h);
        }
        if (f265a) {
            m168b("videoOffset: " + this.f273i);
        }
        if (f265a) {
            m168b("videoLength: " + this.f274j);
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    /* renamed from: a */
    private void m166a(int i) {
        this.f290z = i;
        C0158a aVar = this.f288x;
        if (aVar != null) {
            aVar.mo450a(this.f290z);
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
            android.media.MediaPlayer r0 = r8.f281q
            if (r0 == 0) goto L_0x001c
            android.view.SurfaceHolder r1 = r8.f268d
            r0.setDisplay(r1)
            boolean r0 = r8.f286v
            if (r0 != 0) goto L_0x001b
            boolean r0 = f265a
            if (r0 == 0) goto L_0x0016
            java.lang.String r0 = "Resuming playback"
            m168b(r0)
        L_0x0016:
            android.media.MediaPlayer r0 = r8.f281q
            r0.start()
        L_0x001b:
            return
        L_0x001c:
            r0 = 0
            r8.m166a((int) r0)
            r8.doCleanUp()
            android.media.MediaPlayer r0 = new android.media.MediaPlayer     // Catch:{ Exception -> 0x00cc }
            r0.<init>()     // Catch:{ Exception -> 0x00cc }
            r8.f281q = r0     // Catch:{ Exception -> 0x00cc }
            boolean r1 = r8.f272h     // Catch:{ Exception -> 0x00cc }
            if (r1 == 0) goto L_0x003a
            android.content.Context r1 = r8.f266b     // Catch:{ Exception -> 0x00cc }
            java.lang.String r2 = r8.f269e     // Catch:{ Exception -> 0x00cc }
            android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch:{ Exception -> 0x00cc }
            r0.setDataSource(r1, r2)     // Catch:{ Exception -> 0x00cc }
            goto L_0x008e
        L_0x003a:
            long r0 = r8.f274j     // Catch:{ Exception -> 0x00cc }
            r2 = 0
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 == 0) goto L_0x005a
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00cc }
            java.lang.String r1 = r8.f269e     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r2 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            java.io.FileDescriptor r3 = r0.getFD()     // Catch:{ Exception -> 0x00cc }
            long r4 = r8.f273i     // Catch:{ Exception -> 0x00cc }
            long r6 = r8.f274j     // Catch:{ Exception -> 0x00cc }
            r2.setDataSource(r3, r4, r6)     // Catch:{ Exception -> 0x00cc }
        L_0x0056:
            r0.close()     // Catch:{ Exception -> 0x00cc }
            goto L_0x008e
        L_0x005a:
            android.content.res.Resources r0 = r8.getResources()     // Catch:{ Exception -> 0x00cc }
            android.content.res.AssetManager r0 = r0.getAssets()     // Catch:{ Exception -> 0x00cc }
            java.lang.String r1 = r8.f269e     // Catch:{ IOException -> 0x007d }
            android.content.res.AssetFileDescriptor r0 = r0.openFd(r1)     // Catch:{ IOException -> 0x007d }
            android.media.MediaPlayer r1 = r8.f281q     // Catch:{ IOException -> 0x007d }
            java.io.FileDescriptor r2 = r0.getFileDescriptor()     // Catch:{ IOException -> 0x007d }
            long r3 = r0.getStartOffset()     // Catch:{ IOException -> 0x007d }
            long r5 = r0.getLength()     // Catch:{ IOException -> 0x007d }
            r1.setDataSource(r2, r3, r5)     // Catch:{ IOException -> 0x007d }
            r0.close()     // Catch:{ IOException -> 0x007d }
            goto L_0x008e
        L_0x007d:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00cc }
            java.lang.String r1 = r8.f269e     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r1 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            java.io.FileDescriptor r2 = r0.getFD()     // Catch:{ Exception -> 0x00cc }
            r1.setDataSource(r2)     // Catch:{ Exception -> 0x00cc }
            goto L_0x0056
        L_0x008e:
            android.media.MediaPlayer r0 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            android.view.SurfaceHolder r1 = r8.f268d     // Catch:{ Exception -> 0x00cc }
            r0.setDisplay(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            r1 = 1
            r0.setScreenOnWhilePlaying(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            r0.setOnBufferingUpdateListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            r0.setOnCompletionListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            r0.setOnPreparedListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            r0.setOnVideoSizeChangedListener(r8)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            r1 = 3
            r0.setAudioStreamType(r1)     // Catch:{ Exception -> 0x00cc }
            android.media.MediaPlayer r0 = r8.f281q     // Catch:{ Exception -> 0x00cc }
            r0.prepareAsync()     // Catch:{ Exception -> 0x00cc }
            com.unity3d.player.n$b r0 = new com.unity3d.player.n$b     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r8)     // Catch:{ Exception -> 0x00cc }
            r8.f289y = r0     // Catch:{ Exception -> 0x00cc }
            java.lang.Thread r0 = new java.lang.Thread     // Catch:{ Exception -> 0x00cc }
            com.unity3d.player.n$b r1 = r8.f289y     // Catch:{ Exception -> 0x00cc }
            r0.<init>(r1)     // Catch:{ Exception -> 0x00cc }
            r0.start()     // Catch:{ Exception -> 0x00cc }
            return
        L_0x00cc:
            r0 = move-exception
            boolean r1 = f265a
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
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.C0157n.m170c():void");
    }

    /* renamed from: d */
    private void m171d() {
        if (!isPlaying()) {
            m166a(1);
            if (f265a) {
                m168b("startVideoPlayback");
            }
            updateVideoLayout();
            if (!this.f286v) {
                start();
            }
        }
    }

    public final void CancelOnPrepare() {
        m166a(2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public final boolean mo426a() {
        return this.f286v;
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
        if (f265a) {
            m168b("destroyPlayer");
        }
        if (!this.f286v) {
            pause();
        }
        doCleanUp();
    }

    /* access modifiers changed from: protected */
    public final void doCleanUp() {
        C0159b bVar = this.f289y;
        if (bVar != null) {
            bVar.mo451a();
            this.f289y = null;
        }
        MediaPlayer mediaPlayer = this.f281q;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            this.f281q = null;
        }
        this.f279o = 0;
        this.f280p = 0;
        this.f284t = false;
        this.f283s = false;
    }

    public final int getAudioSessionId() {
        MediaPlayer mediaPlayer = this.f281q;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getAudioSessionId();
    }

    public final int getBufferPercentage() {
        if (this.f272h) {
            return this.f285u;
        }
        return 100;
    }

    public final int getCurrentPosition() {
        MediaPlayer mediaPlayer = this.f281q;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getCurrentPosition();
    }

    public final int getDuration() {
        MediaPlayer mediaPlayer = this.f281q;
        if (mediaPlayer == null) {
            return 0;
        }
        return mediaPlayer.getDuration();
    }

    public final boolean isPlaying() {
        boolean z = this.f284t && this.f283s;
        MediaPlayer mediaPlayer = this.f281q;
        return mediaPlayer == null ? !z : mediaPlayer.isPlaying() || !z;
    }

    public final void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        if (f265a) {
            m168b("onBufferingUpdate percent:" + i);
        }
        this.f285u = i;
    }

    public final void onCompletion(MediaPlayer mediaPlayer) {
        if (f265a) {
            m168b("onCompletion called");
        }
        destroyPlayer();
        m166a(3);
    }

    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4 || (this.f270f == 2 && i != 0 && !keyEvent.isSystem())) {
            destroyPlayer();
            m166a(3);
            return true;
        }
        MediaController mediaController = this.f282r;
        return mediaController != null ? mediaController.onKeyDown(i, keyEvent) : super.onKeyDown(i, keyEvent);
    }

    public final void onPrepared(MediaPlayer mediaPlayer) {
        if (f265a) {
            m168b("onPrepared called");
        }
        C0159b bVar = this.f289y;
        if (bVar != null) {
            bVar.mo451a();
            this.f289y = null;
        }
        int i = this.f270f;
        if (i == 0 || i == 1) {
            MediaController mediaController = new MediaController(this.f266b);
            this.f282r = mediaController;
            mediaController.setMediaPlayer(this);
            this.f282r.setAnchorView(this);
            this.f282r.setEnabled(true);
            Context context = this.f266b;
            if (context instanceof Activity) {
                this.f282r.setSystemUiVisibility(((Activity) context).getWindow().getDecorView().getSystemUiVisibility());
            }
            this.f282r.show();
        }
        this.f284t = true;
        if (1 != 0 && this.f283s) {
            m171d();
        }
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (this.f270f == 2 && action == 0) {
            destroyPlayer();
            m166a(3);
            return true;
        }
        MediaController mediaController = this.f282r;
        return mediaController != null ? mediaController.onTouchEvent(motionEvent) : super.onTouchEvent(motionEvent);
    }

    public final void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        if (f265a) {
            m168b("onVideoSizeChanged called " + i + "x" + i2);
        }
        if (i != 0 && i2 != 0) {
            this.f283s = true;
            this.f279o = i;
            this.f280p = i2;
            if (this.f284t && 1 != 0) {
                m171d();
            }
        } else if (f265a) {
            m168b("invalid video width(" + i + ") or height(" + i2 + ")");
        }
    }

    public final void pause() {
        MediaPlayer mediaPlayer = this.f281q;
        if (mediaPlayer != null) {
            if (this.f287w) {
                mediaPlayer.pause();
            }
            this.f286v = true;
        }
    }

    public final void seekTo(int i) {
        MediaPlayer mediaPlayer = this.f281q;
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(i);
        }
    }

    public final void start() {
        if (f265a) {
            m168b("Start");
        }
        MediaPlayer mediaPlayer = this.f281q;
        if (mediaPlayer != null) {
            if (this.f287w) {
                mediaPlayer.start();
            }
            this.f286v = false;
        }
    }

    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (f265a) {
            m168b("surfaceChanged called " + i + " " + i2 + "x" + i3);
        }
        if (this.f277m != i2 || this.f278n != i3) {
            this.f277m = i2;
            this.f278n = i3;
            if (this.f287w) {
                updateVideoLayout();
            }
        }
    }

    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (f265a) {
            m168b("surfaceCreated called");
        }
        this.f287w = true;
        m170c();
    }

    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (f265a) {
            m168b("surfaceDestroyed called");
        }
        this.f287w = false;
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
            boolean r0 = f265a
            if (r0 == 0) goto L_0x0009
            java.lang.String r0 = "updateVideoLayout"
            m168b(r0)
        L_0x0009:
            android.media.MediaPlayer r0 = r8.f281q
            if (r0 != 0) goto L_0x000e
            return
        L_0x000e:
            int r0 = r8.f277m
            if (r0 == 0) goto L_0x0016
            int r0 = r8.f278n
            if (r0 != 0) goto L_0x0034
        L_0x0016:
            android.content.Context r0 = r8.f266b
            java.lang.String r1 = "window"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.view.WindowManager r0 = (android.view.WindowManager) r0
            android.util.DisplayMetrics r1 = new android.util.DisplayMetrics
            r1.<init>()
            android.view.Display r0 = r0.getDefaultDisplay()
            r0.getMetrics(r1)
            int r0 = r1.widthPixels
            r8.f277m = r0
            int r0 = r1.heightPixels
            r8.f278n = r0
        L_0x0034:
            int r0 = r8.f277m
            int r1 = r8.f278n
            boolean r2 = r8.f283s
            if (r2 == 0) goto L_0x0065
            int r2 = r8.f279o
            float r3 = (float) r2
            int r4 = r8.f280p
            float r5 = (float) r4
            float r3 = r3 / r5
            float r5 = (float) r0
            float r6 = (float) r1
            float r5 = r5 / r6
            int r6 = r8.f271g
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
            boolean r2 = f265a
            if (r2 == 0) goto L_0x006e
            java.lang.String r2 = "updateVideoLayout: Video size is not known yet"
            m168b(r2)
        L_0x006e:
            int r2 = r8.f277m
            if (r2 != r0) goto L_0x0076
            int r2 = r8.f278n
            if (r2 == r1) goto L_0x00a1
        L_0x0076:
            boolean r2 = f265a
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
            android.widget.FrameLayout r0 = r8.f275k
            android.view.SurfaceView r1 = r8.f267c
            r0.updateViewLayout(r1, r2)
        L_0x00a1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.player.C0157n.updateVideoLayout():void");
    }
}
