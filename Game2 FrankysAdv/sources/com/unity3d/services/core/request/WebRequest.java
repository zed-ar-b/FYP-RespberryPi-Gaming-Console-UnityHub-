package com.unity3d.services.core.request;

import com.unity3d.services.core.log.DeviceLog;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

public class WebRequest {
    private ByteArrayOutputStream _baos;
    private byte[] _body;
    private boolean _canceled;
    private int _connectTimeout;
    private long _contentLength;
    private Map<String, List<String>> _headers;
    private IWebRequestProgressListener _progressListener;
    private int _readTimeout;
    private String _requestType;
    private int _responseCode;
    private Map<String, List<String>> _responseHeaders;
    private URL _url;

    public enum RequestType {
        POST,
        GET,
        HEAD
    }

    public WebRequest(String str, String str2) throws MalformedURLException {
        this(str, str2, (Map<String, List<String>>) null);
    }

    public WebRequest(String str, String str2, Map<String, List<String>> map) throws MalformedURLException {
        this(str, str2, map, 30000, 30000);
    }

    public WebRequest(String str, String str2, Map<String, List<String>> map, int i, int i2) throws MalformedURLException {
        this._responseCode = -1;
        this._contentLength = -1;
        this._canceled = false;
        this._url = new URL(str);
        this._requestType = str2;
        this._headers = map;
        this._connectTimeout = i;
        this._readTimeout = i2;
    }

    public void cancel() {
        this._canceled = true;
    }

    public boolean isCanceled() {
        return this._canceled;
    }

    public URL getUrl() {
        return this._url;
    }

    public String getRequestType() {
        return this._requestType;
    }

    public byte[] getBody() {
        return this._body;
    }

    public void setBody(String str) {
        this._body = str.getBytes(StandardCharsets.UTF_8);
    }

    public void setBody(byte[] bArr) {
        this._body = bArr;
    }

    public String getQuery() {
        URL url = this._url;
        if (url != null) {
            return url.getQuery();
        }
        return null;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return this._responseHeaders;
    }

    public Map<String, List<String>> getHeaders() {
        return this._headers;
    }

    public int getResponseCode() {
        return this._responseCode;
    }

    public long getContentLength() {
        return this._contentLength;
    }

    public int getConnectTimeout() {
        return this._connectTimeout;
    }

    public void setConnectTimeout(int i) {
        this._connectTimeout = i;
    }

    public int getReadTimeout() {
        return this._readTimeout;
    }

    public void setReadTimeout(int i) {
        this._readTimeout = i;
    }

    public void setProgressListener(IWebRequestProgressListener iWebRequestProgressListener) {
        this._progressListener = iWebRequestProgressListener;
    }

    public long makeStreamRequest(OutputStream outputStream) throws Exception {
        InputStream inputStream;
        HttpURLConnection httpUrlConnectionWithHeaders = getHttpUrlConnectionWithHeaders();
        httpUrlConnectionWithHeaders.setDoInput(true);
        if (getRequestType().equals(RequestType.POST.name())) {
            httpUrlConnectionWithHeaders.setDoOutput(true);
            OutputStream outputStream2 = null;
            try {
                OutputStream outputStream3 = httpUrlConnectionWithHeaders.getOutputStream();
                if (getBody() == null) {
                    String query = getQuery();
                    if (query != null) {
                        outputStream3.write(query.getBytes(StandardCharsets.UTF_8));
                    }
                } else {
                    outputStream3.write(getBody());
                }
                outputStream3.flush();
                if (outputStream3 != null) {
                    try {
                        outputStream3.close();
                    } catch (Exception e) {
                        Exception exc = e;
                        DeviceLog.exception("Error closing writer", exc);
                        throw exc;
                    }
                }
            } catch (IOException e2) {
                DeviceLog.exception("Error while writing POST params", e2);
                throw new NetworkIOException("Error writing POST params: " + e2.getMessage());
            } catch (Throwable th) {
                if (outputStream2 != null) {
                    try {
                        outputStream2.close();
                    } catch (Exception e3) {
                        Exception exc2 = e3;
                        DeviceLog.exception("Error closing writer", exc2);
                        throw exc2;
                    }
                }
                throw th;
            }
        }
        try {
            this._responseCode = httpUrlConnectionWithHeaders.getResponseCode();
            long contentLength = (long) httpUrlConnectionWithHeaders.getContentLength();
            this._contentLength = contentLength;
            if (contentLength == -1) {
                this._contentLength = (long) httpUrlConnectionWithHeaders.getHeaderFieldInt("X-OrigLength", -1);
            }
            ByteArrayOutputStream byteArrayOutputStream = this._baos;
            long j = 0;
            ByteArrayOutputStream byteArrayOutputStream2 = outputStream;
            if (byteArrayOutputStream != null && byteArrayOutputStream == byteArrayOutputStream2 && this._contentLength > 0) {
                ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream((int) this._contentLength);
                this._baos = byteArrayOutputStream3;
                byteArrayOutputStream2 = byteArrayOutputStream3;
            }
            if (httpUrlConnectionWithHeaders.getHeaderFields() != null) {
                this._responseHeaders = httpUrlConnectionWithHeaders.getHeaderFields();
            }
            try {
                inputStream = httpUrlConnectionWithHeaders.getInputStream();
            } catch (IOException e4) {
                IOException iOException = e4;
                inputStream = httpUrlConnectionWithHeaders.getErrorStream();
                if (inputStream == null) {
                    throw new NetworkIOException("Can't open error stream: " + iOException.getMessage());
                }
            }
            IWebRequestProgressListener iWebRequestProgressListener = this._progressListener;
            if (iWebRequestProgressListener != null) {
                iWebRequestProgressListener.onRequestStart(getUrl().toString(), this._contentLength, this._responseCode, this._responseHeaders);
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] bArr = new byte[4096];
            int i = 0;
            while (!isCanceled() && i != -1) {
                try {
                    i = bufferedInputStream.read(bArr);
                    if (i > 0) {
                        byteArrayOutputStream2.write(bArr, 0, i);
                        j += (long) i;
                        IWebRequestProgressListener iWebRequestProgressListener2 = this._progressListener;
                        if (iWebRequestProgressListener2 != null) {
                            iWebRequestProgressListener2.onRequestProgress(getUrl().toString(), j, this._contentLength);
                        }
                    }
                } catch (IOException e5) {
                    throw new NetworkIOException("Network exception: " + e5.getMessage());
                } catch (Exception e6) {
                    throw new Exception("Unknown Exception: " + e6.getMessage());
                }
            }
            httpUrlConnectionWithHeaders.disconnect();
            byteArrayOutputStream2.flush();
            return j;
        } catch (IOException | RuntimeException e7) {
            throw new NetworkIOException("Response code: " + e7.getMessage());
        }
    }

    public String makeRequest() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this._baos = byteArrayOutputStream;
        makeStreamRequest(byteArrayOutputStream);
        return this._baos.toString("UTF-8");
    }

    private HttpURLConnection getHttpUrlConnectionWithHeaders() throws NetworkIOException, IllegalArgumentException {
        HttpURLConnection httpURLConnection;
        if (getUrl().toString().startsWith("https://")) {
            try {
                httpURLConnection = (HttpsURLConnection) getUrl().openConnection();
            } catch (IOException e) {
                throw new NetworkIOException("Open HTTPS connection: " + e.getMessage());
            }
        } else if (getUrl().toString().startsWith("http://")) {
            try {
                httpURLConnection = (HttpURLConnection) getUrl().openConnection();
            } catch (IOException e2) {
                throw new NetworkIOException("Open HTTP connection: " + e2.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Invalid url-protocol in url: " + getUrl().toString());
        }
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setConnectTimeout(getConnectTimeout());
        httpURLConnection.setReadTimeout(getReadTimeout());
        try {
            httpURLConnection.setRequestMethod(getRequestType());
            if (getHeaders() != null && getHeaders().size() > 0) {
                for (String next : getHeaders().keySet()) {
                    for (String str : getHeaders().get(next)) {
                        DeviceLog.debug("Setting header: " + next + "=" + str);
                        httpURLConnection.setRequestProperty(next, str);
                    }
                }
            }
            return httpURLConnection;
        } catch (ProtocolException e3) {
            throw new NetworkIOException("Set Request Method: " + getRequestType() + ", " + e3.getMessage());
        }
    }
}
