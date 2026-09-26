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
public final class C0128c {

    /* renamed from: b */
    private static CameraManager f176b;

    /* renamed from: c */
    private static String[] f177c;
    /* access modifiers changed from: private */

    /* renamed from: e */
    public static Semaphore f178e = new Semaphore(1);

    /* renamed from: A */
    private CameraCaptureSession.CaptureCallback f179A = new CameraCaptureSession.CaptureCallback() {
        public final void onCaptureCompleted(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult) {
            C0128c.this.m85a(captureRequest.getTag());
        }

        public final void onCaptureFailed(CameraCaptureSession cameraCaptureSession, CaptureRequest captureRequest, CaptureFailure captureFailure) {
            C0137f.Log(5, "Camera2: Capture session failed " + captureRequest.getTag() + " reason " + captureFailure.getReason());
            C0128c.this.m85a(captureRequest.getTag());
        }

        public final void onCaptureSequenceAborted(CameraCaptureSession cameraCaptureSession, int i) {
        }

        public final void onCaptureSequenceCompleted(CameraCaptureSession cameraCaptureSession, int i, long j) {
        }
    };

    /* renamed from: B */
    private final CameraDevice.StateCallback f180B = new CameraDevice.StateCallback() {
        public final void onClosed(CameraDevice cameraDevice) {
            C0128c.f178e.release();
        }

        public final void onDisconnected(CameraDevice cameraDevice) {
            C0137f.Log(5, "Camera2: CameraDevice disconnected.");
            C0128c.this.m83a(cameraDevice);
            C0128c.f178e.release();
        }

        public final void onError(CameraDevice cameraDevice, int i) {
            C0137f.Log(6, "Camera2: Error opeining CameraDevice " + i);
            C0128c.this.m83a(cameraDevice);
            C0128c.f178e.release();
        }

        public final void onOpened(CameraDevice cameraDevice) {
            CameraDevice unused = C0128c.this.f184d = cameraDevice;
            C0128c.f178e.release();
        }
    };

    /* renamed from: C */
    private final ImageReader.OnImageAvailableListener f181C = new ImageReader.OnImageAvailableListener() {
        public final void onImageAvailable(ImageReader imageReader) {
            if (C0128c.f178e.tryAcquire()) {
                Image acquireNextImage = imageReader.acquireNextImage();
                if (acquireNextImage != null) {
                    Image.Plane[] planes = acquireNextImage.getPlanes();
                    if (acquireNextImage.getFormat() == 35 && planes != null && planes.length == 3) {
                        C0136e h = C0128c.this.f183a;
                        ByteBuffer buffer = planes[0].getBuffer();
                        ByteBuffer buffer2 = planes[1].getBuffer();
                        ByteBuffer buffer3 = planes[2].getBuffer();
                        h.mo155a(buffer, buffer2, buffer3, planes[0].getRowStride(), planes[1].getRowStride(), planes[1].getPixelStride());
                    } else {
                        C0137f.Log(6, "Camera2: Wrong image format.");
                    }
                    if (C0128c.this.f198s != null) {
                        C0128c.this.f198s.close();
                    }
                    Image unused = C0128c.this.f198s = acquireNextImage;
                }
                C0128c.f178e.release();
            }
        }
    };

    /* renamed from: D */
    private final SurfaceTexture.OnFrameAvailableListener f182D = new SurfaceTexture.OnFrameAvailableListener() {
        public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
            C0128c.this.f183a.mo154a(surfaceTexture);
        }
    };
    /* access modifiers changed from: private */

    /* renamed from: a */
    public C0136e f183a = null;
    /* access modifiers changed from: private */

    /* renamed from: d */
    public CameraDevice f184d;

    /* renamed from: f */
    private HandlerThread f185f;

    /* renamed from: g */
    private Handler f186g;

    /* renamed from: h */
    private Rect f187h;

    /* renamed from: i */
    private Rect f188i;

    /* renamed from: j */
    private int f189j;

    /* renamed from: k */
    private int f190k;

    /* renamed from: l */
    private float f191l = -1.0f;

    /* renamed from: m */
    private float f192m = -1.0f;

    /* renamed from: n */
    private int f193n;

    /* renamed from: o */
    private int f194o;

    /* renamed from: p */
    private boolean f195p = false;
    /* access modifiers changed from: private */

    /* renamed from: q */
    public Range f196q;
    /* access modifiers changed from: private */

    /* renamed from: r */
    public ImageReader f197r = null;
    /* access modifiers changed from: private */

    /* renamed from: s */
    public Image f198s;
    /* access modifiers changed from: private */

    /* renamed from: t */
    public CaptureRequest.Builder f199t;
    /* access modifiers changed from: private */

    /* renamed from: u */
    public CameraCaptureSession f200u = null;
    /* access modifiers changed from: private */

    /* renamed from: v */
    public Object f201v = new Object();

    /* renamed from: w */
    private int f202w;

    /* renamed from: x */
    private SurfaceTexture f203x;
    /* access modifiers changed from: private */

    /* renamed from: y */
    public Surface f204y = null;

    /* renamed from: z */
    private int f205z = C0134a.f213c;

    /* renamed from: com.unity3d.player.c$a */
    private enum C0134a {
        ;

        static {
            f214d = new int[]{1, 2, 3};
        }
    }

    protected C0128c(C0136e eVar) {
        this.f183a = eVar;
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
            C0137f.Log(6, "Camera2: CameraAccessException " + e);
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
        synchronized (this.f201v) {
            this.f200u = null;
        }
        cameraDevice.close();
        this.f184d = null;
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public void m85a(Object obj) {
        if (obj == "Focus") {
            this.f195p = false;
            synchronized (this.f201v) {
                if (this.f200u != null) {
                    try {
                        this.f199t.set(CaptureRequest.CONTROL_AF_TRIGGER, 0);
                        this.f199t.setTag("Regular");
                        this.f200u.setRepeatingRequest(this.f199t.build(), this.f179A, this.f186g);
                    } catch (CameraAccessException e) {
                        C0137f.Log(6, "Camera2: CameraAccessException " + e);
                    }
                }
            }
        } else if (obj == "Cancel focus") {
            synchronized (this.f201v) {
                if (this.f200u != null) {
                    m107j();
                }
            }
        }
    }

    /* renamed from: a */
    private static Size[] m86a(CameraCharacteristics cameraCharacteristics) {
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap == null) {
            C0137f.Log(6, "Camera2: configuration map is not available.");
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
        if (f176b == null) {
            f176b = (CameraManager) context.getSystemService("camera");
        }
        return f176b;
    }

    /* renamed from: b */
    private void m89b(CameraCharacteristics cameraCharacteristics) {
        int intValue = ((Integer) cameraCharacteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)).intValue();
        this.f190k = intValue;
        if (intValue > 0) {
            Rect rect = (Rect) cameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            this.f188i = rect;
            float width = ((float) rect.width()) / ((float) this.f188i.height());
            float width2 = ((float) this.f187h.width()) / ((float) this.f187h.height());
            if (width2 > width) {
                this.f193n = 0;
                this.f194o = (int) ((((float) this.f188i.height()) - (((float) this.f188i.width()) / width2)) / 2.0f);
            } else {
                this.f194o = 0;
                this.f193n = (int) ((((float) this.f188i.width()) - (((float) this.f188i.height()) * width2)) / 2.0f);
            }
            this.f189j = Math.min(this.f188i.width(), this.f188i.height()) / 20;
        }
    }

    /* renamed from: b */
    public static boolean m91b(Context context, int i) {
        try {
            return ((Integer) m87b(context).getCameraCharacteristics(m94c(context)[i]).get(CameraCharacteristics.LENS_FACING)).intValue() == 0;
        } catch (CameraAccessException e) {
            C0137f.Log(6, "Camera2: CameraAccessException " + e);
            return false;
        }
    }

    /* renamed from: c */
    public static boolean m93c(Context context, int i) {
        try {
            return ((Integer) m87b(context).getCameraCharacteristics(m94c(context)[i]).get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF)).intValue() > 0;
        } catch (CameraAccessException e) {
            C0137f.Log(6, "Camera2: CameraAccessException " + e);
            return false;
        }
    }

    /* renamed from: c */
    private static String[] m94c(Context context) {
        if (f177c == null) {
            try {
                f177c = m87b(context).getCameraIdList();
            } catch (CameraAccessException e) {
                C0137f.Log(6, "Camera2: CameraAccessException " + e);
                f177c = new String[0];
            }
        }
        return f177c;
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
            C0137f.Log(6, "Camera2: CameraAccessException " + e);
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
            C0137f.Log(6, "Camera2: CameraAccessException " + e);
            return null;
        }
    }

    /* renamed from: g */
    private void m101g() {
        HandlerThread handlerThread = new HandlerThread("CameraBackground");
        this.f185f = handlerThread;
        handlerThread.start();
        this.f186g = new Handler(this.f185f.getLooper());
    }

    /* renamed from: h */
    private void m104h() {
        this.f185f.quit();
        try {
            this.f185f.join(4000);
            this.f185f = null;
            this.f186g = null;
        } catch (InterruptedException e) {
            this.f185f.interrupt();
            C0137f.Log(6, "Camera2: Interrupted while waiting for the background thread to finish " + e);
        }
    }

    /* renamed from: i */
    private void m106i() {
        try {
            if (!f178e.tryAcquire(4, TimeUnit.SECONDS)) {
                C0137f.Log(5, "Camera2: Timeout waiting to lock camera for closing.");
                return;
            }
            this.f184d.close();
            try {
                if (!f178e.tryAcquire(4, TimeUnit.SECONDS)) {
                    C0137f.Log(5, "Camera2: Timeout waiting to close camera.");
                }
            } catch (InterruptedException e) {
                C0137f.Log(6, "Camera2: Interrupted while waiting to close camera " + e);
            }
            this.f184d = null;
            f178e.release();
        } catch (InterruptedException e2) {
            C0137f.Log(6, "Camera2: Interrupted while trying to lock camera for closing " + e2);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: j */
    public void m107j() {
        try {
            if (this.f190k != 0 && this.f191l >= 0.0f && this.f191l <= 1.0f && this.f192m >= 0.0f) {
                if (this.f192m <= 1.0f) {
                    this.f195p = true;
                    int max = Math.max(this.f189j + 1, Math.min((int) ((((float) (this.f188i.width() - (this.f193n * 2))) * this.f191l) + ((float) this.f193n)), (this.f188i.width() - this.f189j) - 1));
                    int max2 = Math.max(this.f189j + 1, Math.min((int) ((((double) (this.f188i.height() - (this.f194o * 2))) * (1.0d - ((double) this.f192m))) + ((double) this.f194o)), (this.f188i.height() - this.f189j) - 1));
                    this.f199t.set(CaptureRequest.CONTROL_AF_REGIONS, new MeteringRectangle[]{new MeteringRectangle(max - this.f189j, max2 - this.f189j, this.f189j * 2, this.f189j * 2, 999)});
                    this.f199t.set(CaptureRequest.CONTROL_AF_MODE, 1);
                    this.f199t.set(CaptureRequest.CONTROL_AF_TRIGGER, 1);
                    this.f199t.setTag("Focus");
                    this.f200u.capture(this.f199t.build(), this.f179A, this.f186g);
                    return;
                }
            }
            this.f199t.set(CaptureRequest.CONTROL_AF_MODE, 4);
            this.f199t.setTag("Regular");
            if (this.f200u != null) {
                this.f200u.setRepeatingRequest(this.f199t.build(), this.f179A, this.f186g);
            }
        } catch (CameraAccessException e) {
            C0137f.Log(6, "Camera2: CameraAccessException " + e);
        }
    }

    /* renamed from: k */
    private void m108k() {
        try {
            if (this.f200u != null) {
                this.f200u.stopRepeating();
                this.f199t.set(CaptureRequest.CONTROL_AF_TRIGGER, 2);
                this.f199t.set(CaptureRequest.CONTROL_AF_MODE, 0);
                this.f199t.setTag("Cancel focus");
                this.f200u.capture(this.f199t.build(), this.f179A, this.f186g);
            }
        } catch (CameraAccessException e) {
            C0137f.Log(6, "Camera2: CameraAccessException " + e);
        }
    }

    /* renamed from: a */
    public final Rect mo353a() {
        return this.f187h;
    }

    /* renamed from: a */
    public final boolean mo354a(float f, float f2) {
        if (this.f190k <= 0) {
            return false;
        }
        if (!this.f195p) {
            this.f191l = f;
            this.f192m = f2;
            synchronized (this.f201v) {
                if (!(this.f200u == null || this.f205z == C0134a.f212b)) {
                    m108k();
                }
            }
            return true;
        }
        C0137f.Log(5, "Camera2: Setting manual focus point already started.");
        return false;
    }

    /* renamed from: a */
    public final boolean mo355a(Context context, int i, int i2, int i3, int i4, int i5) {
        try {
            CameraCharacteristics cameraCharacteristics = f176b.getCameraCharacteristics(m94c(context)[i]);
            if (((Integer) cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue() == 2) {
                C0137f.Log(5, "Camera2: only LEGACY hardware level is supported.");
                return false;
            }
            Size[] a = m86a(cameraCharacteristics);
            if (!(a == null || a.length == 0)) {
                this.f187h = m77a(a, (double) i2, (double) i3);
                Range[] rangeArr = (Range[]) cameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                if (rangeArr == null || rangeArr.length == 0) {
                    C0137f.Log(6, "Camera2: target FPS ranges are not avialable.");
                } else {
                    int a2 = m76a(rangeArr, i4);
                    this.f196q = new Range(Integer.valueOf(a2), Integer.valueOf(a2));
                    try {
                        if (!f178e.tryAcquire(4, TimeUnit.SECONDS)) {
                            C0137f.Log(5, "Camera2: Timeout waiting to lock camera for opening.");
                            return false;
                        }
                        try {
                            f176b.openCamera(m94c(context)[i], this.f180B, this.f186g);
                            try {
                                if (!f178e.tryAcquire(4, TimeUnit.SECONDS)) {
                                    C0137f.Log(5, "Camera2: Timeout waiting to open camera.");
                                    return false;
                                }
                                f178e.release();
                                this.f202w = i5;
                                m89b(cameraCharacteristics);
                                return this.f184d != null;
                            } catch (InterruptedException e) {
                                C0137f.Log(6, "Camera2: Interrupted while waiting to open camera " + e);
                            }
                        } catch (CameraAccessException e2) {
                            C0137f.Log(6, "Camera2: CameraAccessException " + e2);
                            f178e.release();
                            return false;
                        }
                    } catch (InterruptedException e3) {
                        C0137f.Log(6, "Camera2: Interrupted while trying to lock camera for opening " + e3);
                        return false;
                    }
                }
            }
            return false;
        } catch (CameraAccessException e4) {
            C0137f.Log(6, "Camera2: CameraAccessException " + e4);
            return false;
        }
    }

    /* renamed from: b */
    public final void mo356b() {
        if (this.f184d != null) {
            mo359e();
            m106i();
            this.f179A = null;
            this.f204y = null;
            this.f203x = null;
            Image image = this.f198s;
            if (image != null) {
                image.close();
                this.f198s = null;
            }
            ImageReader imageReader = this.f197r;
            if (imageReader != null) {
                imageReader.close();
                this.f197r = null;
            }
        }
        m104h();
    }

    /* renamed from: c */
    public final void mo357c() {
        List list;
        if (this.f197r == null) {
            ImageReader newInstance = ImageReader.newInstance(this.f187h.width(), this.f187h.height(), 35, 2);
            this.f197r = newInstance;
            newInstance.setOnImageAvailableListener(this.f181C, this.f186g);
            this.f198s = null;
            if (this.f202w != 0) {
                SurfaceTexture surfaceTexture = new SurfaceTexture(this.f202w);
                this.f203x = surfaceTexture;
                surfaceTexture.setDefaultBufferSize(this.f187h.width(), this.f187h.height());
                this.f203x.setOnFrameAvailableListener(this.f182D, this.f186g);
                this.f204y = new Surface(this.f203x);
            }
        }
        try {
            if (this.f200u == null) {
                CameraDevice cameraDevice = this.f184d;
                if (this.f204y != null) {
                    list = Arrays.asList(new Surface[]{this.f204y, this.f197r.getSurface()});
                } else {
                    list = Arrays.asList(new Surface[]{this.f197r.getSurface()});
                }
                cameraDevice.createCaptureSession(list, new CameraCaptureSession.StateCallback() {
                    public final void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                        C0137f.Log(6, "Camera2: CaptureSession configuration failed.");
                    }

                    public final void onConfigured(CameraCaptureSession cameraCaptureSession) {
                        String str;
                        if (C0128c.this.f184d != null) {
                            synchronized (C0128c.this.f201v) {
                                CameraCaptureSession unused = C0128c.this.f200u = cameraCaptureSession;
                                try {
                                    CaptureRequest.Builder unused2 = C0128c.this.f199t = C0128c.this.f184d.createCaptureRequest(1);
                                    if (C0128c.this.f204y != null) {
                                        C0128c.this.f199t.addTarget(C0128c.this.f204y);
                                    }
                                    C0128c.this.f199t.addTarget(C0128c.this.f197r.getSurface());
                                    C0128c.this.f199t.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, C0128c.this.f196q);
                                    C0128c.this.m107j();
                                } catch (CameraAccessException e) {
                                    str = "Camera2: CameraAccessException " + e;
                                    C0137f.Log(6, str);
                                } catch (IllegalStateException e2) {
                                    str = "Camera2: IllegalStateException " + e2;
                                    C0137f.Log(6, str);
                                }
                            }
                        }
                    }
                }, this.f186g);
            } else if (this.f205z == C0134a.f212b) {
                this.f200u.setRepeatingRequest(this.f199t.build(), this.f179A, this.f186g);
            }
            this.f205z = C0134a.f211a;
        } catch (CameraAccessException e) {
            C0137f.Log(6, "Camera2: CameraAccessException " + e);
        }
    }

    /* renamed from: d */
    public final void mo358d() {
        synchronized (this.f201v) {
            if (this.f200u != null) {
                try {
                    this.f200u.stopRepeating();
                    this.f205z = C0134a.f212b;
                } catch (CameraAccessException e) {
                    C0137f.Log(6, "Camera2: CameraAccessException " + e);
                }
            }
        }
    }

    /* renamed from: e */
    public final void mo359e() {
        synchronized (this.f201v) {
            if (this.f200u != null) {
                try {
                    this.f200u.abortCaptures();
                } catch (CameraAccessException e) {
                    C0137f.Log(6, "Camera2: CameraAccessException " + e);
                }
                this.f200u.close();
                this.f200u = null;
                this.f205z = C0134a.f213c;
            }
        }
    }
}
