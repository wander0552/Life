package com.wander.base.net;

import android.text.TextUtils;

import com.wander.base.utils.StringUtils;

import java.io.IOException;


/**
 * Created on 2016/10/28.
 */

public abstract class TextResponseCallback extends BytesResponseCallback {
    private String mCharset;
    public TextResponseCallback(){
    }

    public TextResponseCallback(String charset){
        setCharset(charset);
    }

    public void setCharset(String charset){
        mCharset = charset;
    }

    public String getCharset(){
        return mCharset;
    }

    @Override
    protected void onSuccess(byte[] bytes){
        if(bytes != null){
            String charset = mCharset;
            if(TextUtils.isEmpty(charset)){
                charset = DEF_CHARSET;
            }
            String content = StringUtils.dataToString(bytes, charset);
            onSuccess(content);
        }else{
            onFailure(new IOException("response error"));
        }
    }

    protected abstract void onSuccess(String content);
}
