package com.unity3d.player;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Range;
import android.util.Size;
import android.util.SizeF;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/* renamed from: com.unity3d.player.c */
public final class C0061c {

    /* renamed from: b */
    private static CameraManager f177b;

    /* renamed from: c */
    private static String[] f178c;
    /* access modifiers changed from: private */

    /* renamed from: e */
    public static Semaphore f179e = new Semaphore(1);

    /* renamed from: A */
    private CameraCaptureSession.CaptureCallback f180A = new CameraCaptureSession.CaptureCallback() {
        public final void onCaptureCompleted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult) {
            C0061c.this.m85a(captureRequest.getTag());
        }

        public final void onCaptureFailed(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, CaptureFailure captureFailure) {
            C0070f.Log(5, "Camera2: Capture session failed " + captureRequest.getTag() + " reason " + captureFailure.getReason());
            C0061c.this.m85a(captureRequest.getTag());
        }

        public final void onCaptureSequenceAborted(CameraCaptureSession cameraCaptureSession, int i) {
        }

        public final void onCaptureSequenceCompleted(CameraCaptureSession cameraCaptureSession, int i, long j) {
        }
    };

    /* renamed from: B */
    private final CameraDevice.StateCallback f181B = new CameraDevice.StateCallback() {
        public final void onClosed(CameraDevice cameraDevice) {
            C0061c.f179e.release();
        }

        public final void onDisconnected(CameraDevice cameraDevice) {
            C0070f.Log(5, "Camera2: CameraDevice disconnected.");
            C0061c.this.m83a(cameraDevice);
            C0061c.f179e.release();
        }

        public final void onError(CameraDevice cameraDevice, int i) {
            C0070f.Log(6, "Camera2: Error opeining CameraDevice " + i);
            C0061c.this.m83a(cameraDevice);
            C0061c.f179e.release();
        }

        public final void onOpened(CameraDevice cameraDevice) {
            CameraDevice unused = C0061c.this.f185d = cameraDevice;
            C0061c.f179e.release();
        }
    };

    /* renamed from: C */
    private final ImageReader.OnImageAvailableListener f182C = new ImageReader.OnImageAvailableListener() {
        public final void onImageAvailable(ImageReader imageReader) {
            if (C0061c.f179e.tryAcquire()) {
                Image acquireNextImage = imageReader.acquireNextImage();
                if (acquireNextImage != null) {
                    Image.Plane[] planes = acquireNextImage.getPlanes();
                    if (acquireNextImage.getFormat() == 35 && planes != null && planes.length == 3) {
                        C0069e h = C0061c.this.f184a;
                        ByteBuffer buffer = planes[0].getBuffer();
                        ByteBuffer buffer2 = planes[1].getBuffer();
                        ByteBuffer buffer3 = planes[2].getBuffer();
                        h.mo57a(buffer, buffer2, buffer3, planes[0].getRowStride(), planes[1].getRowStride(), planes[1].getPixelStride());
                    } else {
                        C0070f.Log(6, "Camera2: Wrong image format.");
                    }
                    if (C0061c.this.f199s != null) {
                        C0061c.this.f199s.close();
                    }
                    Image unused = C0061c.this.f199s = acquireNextImage;
                }
                C0061c.f179e.release();
            }
        }
    };

    /* renamed from: D */
    private final SurfaceTexture.OnFrameAvailableListener f183D = new SurfaceTexture.OnFrameAvailableListener() {
        public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
            C0061c.this.f184a.mo56a(surfaceTexture);
        }
    };
    /* access modifiers changed from: private */

    /* renamed from: a */
    public C0069e f184a = null;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public CameraDevice f185d;

    /* renamed from: f */
    private HandlerThread f186f;

    /* renamed from: g */
    private Handler f187g;

    /* renamed from: h */
    private Rect f188h;

    /* renamed from: i */
    private Rect f189i;

    /* renamed from: j */
    private int f190j;

    /* renamed from: k */
    private int f191k;

    /* renamed from: l */
    private float f192l = -1.0f;

    /* renamed from: m */
    private float f193m = -1.0f;

    /* renamed from: n */
    private int f194n;

    /* renamed from: o */
    private int f195o;

    /* renamed from: p */
    private boolean f196p = false;
    /* access modifiers changed from: private */

    /* renamed from: q */
    public Range f197q;
    /* access modifiers changed from: private */

    /* renamed from: r */
    public ImageReader f198r = null;
    /* access modifiers changed from: private */

    /* renamed from: s */
    public Image f199s;
    /* access modifiers changed from: private */

    /* renamed from: t */
    public CaptureRequest.Builder f200t;
    /* access modifiers changed from: private */

    /* renamed from: u */
    public CameraCaptureSession f201u = null;
    /* access modifiers changed from: private */

    /* renamed from: v */
    public Object f202v = new Object();

    /* renamed from: w */
    private int f203w;

    /* renamed from: x */
    private SurfaceTexture f204x;
    /* access modifiers changed from: private */

    /* renamed from: y */
    public Surface f205y = null;

    /* renamed from: z */
    private int f206z = C0067a.f214c;

    /* renamed from: com.unity3d.player.c$a */
    private enum C0067a {
        ;

        static {
            f215d = new int[]{1, 2, 3};
        }
    }

    protected C0061c(C0069e eVar) {
        this.f184a = eVar;
        m101g();
    }

    /* renamed from: a */
    public static int m74a(Context context) {
        return m94c(context).length;
    }

    /* renamed from: a */
    public static int m75a(Context context, int i) {
        try {
            return ((Integer) m87b(context).getCameraCharacteristics(m94c(context)[i]).get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue();
        } catch (CameraAccessException e) {
            C0070f.Log(6, "Camera2: CameraAccessException " + e);
            return 0;
        }
    }

    /* renamed from: a */
    private static int m76a(Range[] rangeArr, int i) {
        int i2 = -1;
        double d = Double.MAX_VALUE;
        for (int i3 = 0; i3 < rangeArr.length; i3++) {
            int intValue = ((Integer) rangeArr[i3].getLower()).intValue();
            int intValue2 = ((Integer) rangeArr[i3].getUpper()).intValue();
            float f = (float) i;
            if (f + 0.1f > ((float) intValue) && f - 0.1f < ((float) intValue2)) {
                return i;
            }
            double min = (double) ((float) Math.min(Math.abs(i - intValue), Math.abs(i - intValue2)));
            if (min < d) {
                i2 = i3;
                d = min;
            }
        }
        return ((Integer) (i > ((Integer) rangeArr[i2].getUpper()).intValue() ? rangeArr[i2].getUpper() : rangeArr[i2].getLower())).intValue();
    }

    /* renamed from: a */
    private static Rect m77a(Size[] sizeArr, double d, double d2) {
        Size[] sizeArr2 = sizeArr;
        double d3 = Double.MAX_VALUE;
        int i = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < sizeArr2.length; i3++) {
            int width = sizeArr2[i3].getWidth();
            int height = sizeArr2[i3].getHeight();
            double abs = Math.abs(Math.log(d / ((double) width))) + Math.abs(Math.log(d2 / ((double) height)));
            if (abs < d3) {
                i = width;
                i2 = height;
                d3 = abs;
            }
        }
        return new Rect(0, 0, i, i2);
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public void m83a(CameraDevice cameraDevice) {
        synchronized (this.f202v) {
            this.f201u = null;
        }
        cameraDevice.close();
        this.f185d = null;
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public void m85a(Object obj) {
        if (obj == "Focus") {
            this.f196p = false;
            synchronized (this.f202v) {
                if (this.f201u != null) {
                    try {
                        this.f200t.set(CaptureRequest.CONTROL_AF_TRIGGER, 0);
                        this.f200t.setTag("Regular");
                        this.f201u.setRepeatingRequest(this.f200t.build(), this.f180A, this.f187g);
                    } catch (CameraAccessException e) {
                        C0070f.Log(6, "Camera2: CameraAccessException " + e);
                    }
                }
            }
        } else if (obj == "Cancel focus") {
            synchronized (this.f202v) {
                if (this.f201u != null) {
                    m107j();
                }
            }
        }
    }

    /* renamed from: a */
    private static Size[] m86a(CameraCharacteristics cameraCharacteristics) {
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap == null) {
            C0070f.Log(6, "Camera2: configuration map is not available.");
            return null;
        }
        Size[] outputSizes = streamConfigurationMap.getOutputSizes(35);
        if (outputSizes == null || outputSizes.length == 0) {
            return null;
        }
        return outputSizes;
    }

    /* renamed from: b */
    private static CameraManager m87b(Context context) {
        if (f177b == null) {
            f177b = (CameraManager) context.getSystemService("camera");
        }
        return f177b;
    }

    /* renamed from: b */
    private void m89b(CameraCharacteristics cameraCharacteristics) {
        int intValue = ((Integer) cameraCharacteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)).intValue();
        this.f191k = intValue;
        if (intValue > 0) {
            Rect rect = (Rect) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            this.f189i = rect;
            float width = ((float) rect.width()) / ((float) this.f189i.height());
            float width2 = ((float) this.f188h.width()) / ((float) this.f188h.height());
            if (width2 > width) {
                this.f194n = 0;
                this.f195o = (int) ((((float) this.f189i.height()) - (((float) this.f189i.width()) / width2)) / 2.0f);
            } else {
                this.f195o = 0;
                this.f194n = (int) ((((float) this.f189i.width()) - (((float) this.f189i.height()) * width2)) / 2.0f);
            }
            this.f190j = Math.min(this.f189i.width(), this.f189i.height()) / 20;
        }
    }

    /* renamed from: b */
    public static boolean m91b(Context context, int i) {
        try {
            return ((Integer) m87b(context).getCameraCharacteristics(m94c(context)[i]).get(CameraCharacteristics.LENS_FACING)).intValue() == 0;
        } catch (CameraAccessException e) {
            C0070f.Log(6, "Camera2: CameraAccessException " + e);
            return false;
        }
    }

    /* renamed from: c */
    public static boolean m93c(Context context, int i) {
        try {
            return ((Integer) m87b(context).getCameraCharacteristics(m94c(context)[i]).get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)).intValue() > 0;
        } catch (CameraAccessException e) {
            C0070f.Log(6, "Camera2: CameraAccessException " + e);
            return false;
        }
    }

    /* renamed from: c */
    private static String[] m94c(Context context) {
        if (f178c == null) {
            try {
                f178c = m87b(context).getCameraIdList();
            } catch (CameraAccessException e) {
                C0070f.Log(6, "Camera2: CameraAccessException " + e);
                f178c = new String[0];
            }
        }
        return f178c;
    }

    /* renamed from: d */
    public static int m95d(Context context, int i) {
        try {
            CameraCharacteristics cameraCharacteristics = m87b(context).getCameraCharacteristics(m94c(context)[i]);
            float[] fArr = (float[]) cameraCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
            SizeF sizeF = (SizeF) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
            if (fArr.length > 0) {
                return (int) ((fArr[0] * 36.0f) / sizeF.getWidth());
            }
        } catch (CameraAccessException e) {
            C0070f.Log(6, "Camera2: CameraAccessException " + e);
        }
        return 0;
    }

    /* renamed from: e */
    public static int[] m98e(Context context, int i) {
        try {
            Size[] a = m86a(m87b(context).getCameraCharacteristics(m94c(context)[i]));
            if (a == null) {
                return null;
            }
            int[] iArr = new int[(a.length * 2)];
            for (int i2 = 0; i2 < a.length; i2++) {
                int i3 = i2 * 2;
                iArr[i3] = a[i2].getWidth();
                iArr[i3 + 1] = a[i2].getHeight();
            }
            return iArr;
        } catch (CameraAccessException e) {
            C0070f.Log(6, "Camera2: CameraAccessException " + e);
            return null;
        }
    }

    /* renamed from: g */
    private void m101g() {
        HandlerThread handlerThread = new HandlerThread("CameraBackground");
        this.f186f = handlerThread;
        handlerThread.start();
        this.f187g = new Handler(this.f186f.getLooper());
    }

    /* renamed from: h */
    private void m104h() {
        this.f186f.quit();
        try {
            this.f186f.join(4000);
            this.f186f = null;
            this.f187g = null;
        } catch (InterruptedException e) {
            this.f186f.interrupt();
            C0070f.Log(6, "Camera2: Interrupted while waiting for the background thread to finish " + e);
        }
    }

    /* renamed from: i */
    private void m106i() {
        try {
            if (!f179e.tryAcquire(4, TimeUnit.SECONDS)) {
                C0070f.Log(5, "Camera2: Timeout waiting to lock camera for closing.");
                return;
            }
            this.f185d.close();
            try {
                if (!f179e.tryAcquire(4, TimeUnit.SECONDS)) {
                    C0070f.Log(5, "Camera2: Timeout waiting to close camera.");
                }
            } catch (InterruptedException e) {
                C0070f.Log(6, "Camera2: Interrupted while waiting to close camera " + e);
            }
            this.f185d = null;
            f179e.release();
        } catch (InterruptedException e2) {
            C0070f.Log(6, "Camera2: Interrupted while trying to lock camera for closing " + e2);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: j */
    public void m107j() {
        try {
            if (this.f191k != 0 && this.f192l >= 0.0f && this.f192l <= 1.0f && this.f193m >= 0.0f) {
                if (this.f193m <= 1.0f) {
                    this.f196p = true;
                    int max = Math.max(this.f190j + 1, Math.min((int) ((((float) (this.f189i.width() - (this.f194n * 2))) * this.f192l) + ((float) this.f194n)), (this.f189i.width() - this.f190j) - 1));
                    int max2 = Math.max(this.f190j + 1, Math.min((int) ((((double) (this.f189i.height() - (this.f195o * 2))) * (1.0d - ((double) this.f193m))) + ((double) this.f195o)), (this.f189i.height() - this.f190j) - 1));
                    this.f200t.set(CaptureRequest.CONTROL_AF_REGIONS, new MeteringRectangle[]{new MeteringRectangle(max - this.f190j, max2 - this.f190j, this.f190j * 2, this.f190j * 2, 999)});
                    this.f200t.set(CaptureRequest.CONTROL_AF_MODE, 1);
                    this.f200t.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
                    this.f200t.setTag("Focus");
                    this.f201u.capture(this.f200t.build(), this.f180A, this.f187g);
                    return;
                }
            }
            this.f200t.set(CaptureRequest.CONTROL_AF_MODE, 4);
            this.f200t.setTag("Regular");
            if (this.f201u != null) {
                this.f201u.setRepeatingRequest(this.f200t.build(), this.f180A, this.f187g);
            }
        } catch (CameraAccessException e) {
            C0070f.Log(6, "Camera2: CameraAccessException " + e);
        }
    }

    /* renamed from: k */
    private void m108k() {
        try {
            if (this.f201u != null) {
                this.f201u.stopRepeating();
                this.f200t.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
                this.f200t.set(CaptureRequest.CONTROL_AF_MODE, 0);
                this.f200t.setTag("Cancel focus");
                this.f201u.capture(this.f200t.build(), this.f180A, this.f187g);
            }
        } catch (CameraAccessException e) {
            C0070f.Log(6, "Camera2: CameraAccessException " + e);
        }
    }

    /* renamed from: a */
    public final Rect mo255a() {
        return this.f188h;
    }

    /* renamed from: a */
    public final boolean mo256a(float f, float f2) {
        if (this.f191k <= 0) {
            return false;
        }
        if (!this.f196p) {
            this.f192l = f;
            this.f193m = f2;
            synchronized (this.f202v) {
                if (!(this.f201u == null || this.f206z == C0067a.f213b)) {
                    m108k();
                }
            }
            return true;
        }
        C0070f.Log(5, "Camera2: Setting manual focus point already started.");
        return false;
    }

    /* renamed from: a */
    public final boolean mo257a(Context context, int i, int i2, int i3, int i4, int i5) {
        try {
            CameraCharacteristics cameraCharacteristics = f177b.getCameraCharacteristics(m94c(context)[i]);
            if (((Integer) cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue() == 2) {
                C0070f.Log(5, "Camera2: only LEGACY hardware level is supported.");
                return false;
            }
            Size[] a = m86a(cameraCharacteristics);
            if (!(a == null || a.length == 0)) {
                this.f188h = m77a(a, (double) i2, (double) i3);
                Range[] rangeArr = (Range[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                if (rangeArr == null || rangeArr.length == 0) {
                    C0070f.Log(6, "Camera2: target FPS ranges are not avialable.");
                } else {
                    int a2 = m76a(rangeArr, i4);
                    this.f197q = new Range(Integer.valueOf(a2), Integer.valueOf(a2));
                    try {
                        if (!f179e.tryAcquire(4, TimeUnit.SECONDS)) {
                            C0070f.Log(5, "Camera2: Timeout waiting to lock camera for opening.");
                            return false;
                        }
                        try {
                            f177b.openCamera(m94c(context)[i], this.f181B, this.f187g);
                            try {
                                if (!f179e.tryAcquire(4, TimeUnit.SECONDS)) {
                                    C0070f.Log(5, "Camera2: Timeout waiting to open camera.");
                                    return false;
                                }
                                f179e.release();
                                this.f203w = i5;
                                m89b(cameraCharacteristics);
                                return this.f185d != null;
                            } catch (InterruptedException e) {
                                C0070f.Log(6, "Camera2: Interrupted while waiting to open camera " + e);
                            }
                        } catch (CameraAccessException e2) {
                            C0070f.Log(6, "Camera2: CameraAccessException " + e2);
                            f179e.release();
                            return false;
                        }
                    } catch (InterruptedException e3) {
                        C0070f.Log(6, "Camera2: Interrupted while trying to lock camera for opening " + e3);
                        return false;
                    }
                }
            }
            return false;
        } catch (CameraAccessException e4) {
            C0070f.Log(6, "Camera2: CameraAccessException " + e4);
            return false;
        }
    }

    /* renamed from: b */
    public final void mo258b() {
        if (this.f185d != null) {
            mo261e();
            m106i();
            this.f180A = null;
            this.f205y = null;
            this.f204x = null;
            Image image = this.f199s;
            if (image != null) {
                image.close();
                this.f199s = null;
            }
            ImageReader imageReader = this.f198r;
            if (imageReader != null) {
                imageReader.close();
                this.f198r = null;
            }
        }
        m104h();
    }

    /* renamed from: c */
    public final void mo259c() {
        List list;
        if (this.f198r == null) {
            ImageReader newInstance = ImageReader.newInstance(this.f188h.width(), this.f188h.height(), 35, 2);
            this.f198r = newInstance;
            newInstance.setOnImageAvailableListener(this.f182C, this.f187g);
            this.f199s = null;
            if (this.f203w != 0) {
                SurfaceTexture surfaceTexture = new SurfaceTexture(this.f203w);
                this.f204x = surfaceTexture;
                surfaceTexture.setDefaultBufferSize(this.f188h.width(), this.f188h.height());
                this.f204x.setOnFrameAvailableListener(this.f183D, this.f187g);
                this.f205y = new Surface(this.f204x);
            }
        }
        try {
            if (this.f201u == null) {
                CameraDevice cameraDevice = this.f185d;
                if (this.f205y != null) {
                    list = Arrays.asList(new Surface[]{this.f205y, this.f198r.getSurface()});
                } else {
                    list = Arrays.asList(new Surface[]{this.f198r.getSurface()});
                }
                cameraDevice.createCaptureSession(list, new CameraCaptureSession.StateCallback() {
                    public final void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                        C0070f.Log(6, "Camera2: CaptureSession configuration failed.");
                    }

                    public final void onConfigured(CameraCaptureSession cameraCaptureSession) {
                        String str;
                        if (C0061c.this.f185d != null) {
                            synchronized (C0061c.this.f202v) {
                                CameraCaptureSession unused = C0061c.this.f201u = cameraCaptureSession;
                                try {
                                    CaptureRequest.Builder unused2 = C0061c.this.f200t = C0061c.this.f185d.createCaptureRequest(1);
                                    if (C0061c.this.f205y != null) {
                                        C0061c.this.f200t.addTarget(C0061c.this.f205y);
                                    }
                                    C0061c.this.f200t.addTarget(C0061c.this.f198r.getSurface());
                                    C0061c.this.f200t.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, C0061c.this.f197q);
                                    C0061c.this.m107j();
                                } catch (CameraAccessException e) {
                                    str = "Camera2: CameraAccessException " + e;
                                    C0070f.Log(6, str);
                                } catch (IllegalStateException e2) {
                                    str = "Camera2: IllegalStateException " + e2;
                                    C0070f.Log(6, str);
                                }
                            }
                        }
                    }
                }, this.f187g);
            } else if (this.f206z == C0067a.f213b) {
                this.f201u.setRepeatingRequest(this.f200t.build(), this.f180A, this.f187g);
            }
            this.f206z = C0067a.f212a;
        } catch (CameraAccessException e) {
            C0070f.Log(6, "Camera2: CameraAccessException " + e);
        }
    }

    /* renamed from: d */
    public final void mo260d() {
        synchronized (this.f202v) {
            if (this.f201u != null) {
                try {
                    this.f201u.stopRepeating();
                    this.f206z = C0067a.f213b;
                } catch (CameraAccessException e) {
                    C0070f.Log(6, "Camera2: CameraAccessException " + e);
                }
            }
        }
    }

    /* renamed from: e */
    public final void mo261e() {
        synchronized (this.f202v) {
            if (this.f201u != null) {
                try {
                    this.f201u.abortCaptures();
                } catch (CameraAccessException e) {
                    C0070f.Log(6, "Camera2: CameraAccessException " + e);
                }
                this.f201u.close();
                this.f201u = null;
                this.f206z = C0067a.f214c;
            }
        }
    }
}
