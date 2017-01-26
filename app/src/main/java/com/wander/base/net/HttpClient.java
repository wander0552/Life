package com.wander.base.net;

import android.text.TextUtils;

import com.wander.base.utils.StringUtils;

import java.io.IOException;
import java.net.URL;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created on 2016/10/28.
 */

public class HttpClient {
    public static OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(new ChangeIpInterceptor());
        OkHttpClient client = builder.build();
        return client;
    }

    public static String syncGetString(String url) {
        return syncGetString(url, ResponseCallback.DEF_CHARSET);
    }

    public static String syncGetString(String url, String charset) {
        byte[] bytes = syncGet(url);
        if (bytes != null) {
            return StringUtils.dataToString(bytes, charset);
        } else {
            return null;
        }
    }

    public static byte[] syncGet(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        byte[] responseBytes = null;
        try {
            OkHttpClient client = createOkHttpClient();
            response = client.newCall(request).execute();
            if (response != null && response.body() != null && response.isSuccessful()) {
                responseBytes = response.body().bytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        if (responseBytes == null) {//When OkHttp fails, we use tcp proxy
            TCPProxy.KWTCPProxyResult r = TCPProxy.getInstance().syncHTTP(url, null);
            if (r != null && r.success) {
                responseBytes = r.data;
            }
        }
        return responseBytes;
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static byte[] syncPost(String url, RequestBody body) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        byte[] responseBytes = null;
        try {
            OkHttpClient client = createOkHttpClient();
            response = client.newCall(request).execute();
            if (response != null && response.body() != null && response.isSuccessful()) {
                responseBytes = response.body().bytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        if (responseBytes == null) {//When OkHttp fails, we use tcp proxy
            TCPProxy.KWTCPProxyResult r = TCPProxy.getInstance().syncHTTP(url, null);
            if (r != null && r.success) {
                responseBytes = r.data;
            }
        }
        return responseBytes;

    }

    public static void asyncGet(String url, ResponseCallback callback) {
        OkHttpClient client = createOkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }

    private static class ChangeIpInterceptor implements Interceptor {
        private Response doRequest(Chain chain, Request request) {
            Response response = null;
            try {
                response = chain.proceed(request);
            } catch (Throwable e) {// This is important
            }
            return response;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = doRequest(chain, request);
            if (response == null || !response.isSuccessful()) {
                android.util.Log.i("DNSLookup", "DNSLookup");
                if (request.url() == null) {
                    return response;
                }
                URL url = request.url().url();
                String host = url.getHost();
                DNSLookup.IPInfo info = DNSLookup.getInstance().ipFromDomain(host);
                if (info != null && !TextUtils.isEmpty(info.ipAddr)) {//IP直连
                    StringBuilder newUrl = new StringBuilder(url.getProtocol());
                    newUrl.append("://").append(info.ipAddr);
                    int port = url.getPort();
                    if (port != -1) {
                        newUrl.append(":").append(port);
                    }
                    newUrl.append(url.getPath());
                    String queryString = url.getQuery();
                    if (!TextUtils.isEmpty(queryString)) {
                        newUrl.append("?").append(queryString);
                    }
                    Request newRequest = request.newBuilder().url(newUrl.toString()).build();
                    response = doRequest(chain, newRequest);
                }
            }
            if (response == null) {
                throw new IOException();
            }
            return response;
        }
    }
}
