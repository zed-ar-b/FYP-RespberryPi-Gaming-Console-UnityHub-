package com.unity3d.services.core.request;

import android.os.ConditionVariable;
import com.unity3d.services.core.log.DeviceLog;
import com.unity3d.services.core.request.WebRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class WebRequestThread {
    private static int _corePoolSize = 1;
    private static long _keepAliveTime = 1000;
    private static int _maximumPoolSize = 1;
    private static CancelableThreadPoolExecutor _pool = null;
    private static LinkedBlockingQueue<Runnable> _queue = null;
    /* access modifiers changed from: private */
    public static boolean _ready = false;
    /* access modifiers changed from: private */
    public static final Object _readyLock = new Object();

    private static synchronized void init() {
        synchronized (WebRequestThread.class) {
            _queue = new LinkedBlockingQueue<>();
            CancelableThreadPoolExecutor cancelableThreadPoolExecutor = new CancelableThreadPoolExecutor(_corePoolSize, _maximumPoolSize, _keepAliveTime, TimeUnit.MILLISECONDS, _queue);
            _pool = cancelableThreadPoolExecutor;
            cancelableThreadPoolExecutor.prestartAllCoreThreads();
            _queue.add(new Runnable() {
                public void run() {
                    boolean unused = WebRequestThread._ready = true;
                    synchronized (WebRequestThread._readyLock) {
                        WebRequestThread._readyLock.notify();
                    }
                }
            });
            while (!_ready) {
                try {
                    synchronized (_readyLock) {
                        _readyLock.wait();
                    }
                } catch (InterruptedException unused) {
                    DeviceLog.debug("Couldn't synchronize thread");
                    return;
                }
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:5|6|7|8|9) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x001b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void reset() {
        /*
            java.lang.Class<com.unity3d.services.core.request.WebRequestThread> r0 = com.unity3d.services.core.request.WebRequestThread.class
            monitor-enter(r0)
            cancel()     // Catch:{ all -> 0x002a }
            com.unity3d.services.core.request.CancelableThreadPoolExecutor r1 = _pool     // Catch:{ all -> 0x002a }
            if (r1 == 0) goto L_0x0028
            com.unity3d.services.core.request.CancelableThreadPoolExecutor r1 = _pool     // Catch:{ all -> 0x002a }
            r1.shutdown()     // Catch:{ all -> 0x002a }
            com.unity3d.services.core.request.CancelableThreadPoolExecutor r1 = _pool     // Catch:{ InterruptedException -> 0x001b }
            r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            java.util.concurrent.TimeUnit r4 = java.util.concurrent.TimeUnit.NANOSECONDS     // Catch:{ InterruptedException -> 0x001b }
            r1.awaitTermination(r2, r4)     // Catch:{ InterruptedException -> 0x001b }
        L_0x001b:
            java.util.concurrent.LinkedBlockingQueue<java.lang.Runnable> r1 = _queue     // Catch:{ all -> 0x002a }
            r1.clear()     // Catch:{ all -> 0x002a }
            r1 = 0
            _pool = r1     // Catch:{ all -> 0x002a }
            _queue = r1     // Catch:{ all -> 0x002a }
            r1 = 0
            _ready = r1     // Catch:{ all -> 0x002a }
        L_0x0028:
            monitor-exit(r0)
            return
        L_0x002a:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unity3d.services.core.request.WebRequestThread.reset():void");
    }

    public static synchronized void cancel() {
        synchronized (WebRequestThread.class) {
            if (_pool != null) {
                _pool.cancel();
                Iterator<Runnable> it = _queue.iterator();
                while (it.hasNext()) {
                    Runnable next = it.next();
                    if (next instanceof WebRequestRunnable) {
                        ((WebRequestRunnable) next).setCancelStatus(true);
                    }
                }
                _queue.clear();
                _pool.purge();
            }
        }
    }

    public static synchronized void request(String str, WebRequest.RequestType requestType, Map<String, List<String>> map, Integer num, Integer num2, IWebRequestListener iWebRequestListener) {
        synchronized (WebRequestThread.class) {
            request(str, requestType, map, (String) null, num, num2, iWebRequestListener);
        }
    }

    public static synchronized void request(String str, WebRequest.RequestType requestType, Map<String, List<String>> map, String str2, Integer num, Integer num2, IWebRequestListener iWebRequestListener) {
        String str3 = str;
        synchronized (WebRequestThread.class) {
            if (!_ready) {
                init();
            }
            if (str3 != null) {
                if (str.length() >= 3) {
                    _queue.add(new WebRequestRunnable(str, requestType.name(), str2, num.intValue(), num2.intValue(), map, iWebRequestListener));
                    return;
                }
            }
            iWebRequestListener.onFailed(str, "Request is NULL or too short");
        }
    }

    public static synchronized void setConcurrentRequestCount(int i) {
        synchronized (WebRequestThread.class) {
            _corePoolSize = i;
            _maximumPoolSize = i;
            if (_pool != null) {
                _pool.setCorePoolSize(i);
                _pool.setMaximumPoolSize(_maximumPoolSize);
            }
        }
    }

    public static synchronized void setMaximumPoolSize(int i) {
        synchronized (WebRequestThread.class) {
            _maximumPoolSize = i;
            if (_pool != null) {
                _pool.setMaximumPoolSize(i);
            }
        }
    }

    public static synchronized void setKeepAliveTime(long j) {
        synchronized (WebRequestThread.class) {
            _keepAliveTime = j;
            if (_pool != null) {
                _pool.setKeepAliveTime(j, TimeUnit.MILLISECONDS);
            }
        }
    }

    public static synchronized boolean resolve(final String str, final IResolveHostListener iResolveHostListener) {
        synchronized (WebRequestThread.class) {
            if (str != null) {
                if (str.length() >= 3) {
                    new Thread(new Runnable() {
                        public void run() {
                            Thread thread;
                            Exception e;
                            final ConditionVariable conditionVariable = new ConditionVariable();
                            try {
                                thread = new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            iResolveHostListener.onResolve(str, InetAddress.getByName(str).getHostAddress());
                                        } catch (UnknownHostException e) {
                                            DeviceLog.exception("Unknown host", e);
                                            iResolveHostListener.onFailed(str, ResolveHostError.UNKNOWN_HOST, e.getMessage());
                                        }
                                        conditionVariable.open();
                                    }
                                });
                                try {
                                    thread.start();
                                } catch (Exception e2) {
                                    e = e2;
                                }
                            } catch (Exception e3) {
                                Exception exc = e3;
                                thread = null;
                                e = exc;
                                DeviceLog.exception("Exception while resolving host", e);
                                iResolveHostListener.onFailed(str, ResolveHostError.UNEXPECTED_EXCEPTION, e.getMessage());
                                if (!conditionVariable.block(20000)) {
                                    return;
                                }
                            }
                            if (!conditionVariable.block(20000) && thread != null) {
                                thread.interrupt();
                                iResolveHostListener.onFailed(str, ResolveHostError.TIMEOUT, "Timeout");
                            }
                        }
                    }).start();
                    return true;
                }
            }
            iResolveHostListener.onFailed(str, ResolveHostError.INVALID_HOST, "Host is NULL");
            return false;
        }
    }
}
