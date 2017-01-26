package com.wander.base.net;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 2016/10/28.
 */

public abstract class BytesResponseCallback implements ResponseCallback {
    @Override
    public void onFailure(Call call, IOException e) {
        Request request = call.request();
        if(request != null && request.url() != null && request.url().toString() != null){
            //When OkHttp fails, we use tcp proxy
            TCPProxy.KWTCPProxyResult r = TCPProxy.getInstance()
                    .syncHTTP(request.url().toString(), null);
            if (r!=null && r.success) {
                onSuccess(r.data);
                return ;
            }
        }
        onFailure(e);
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (response != null && response.body() != null && response.isSuccessful()) {
            try {
                onSuccess(response.body().bytes());
            } catch (IOException e) {
                onFailure(call, e);
            } finally {
                if (response != null) {
                    response.close();
                }
            }
        } else {
            onFailure(call, new IOException("response error"));
        }
    }

    protected abstract void onFailure(Throwable e);
    protected abstract void onSuccess(byte[] content);
}
