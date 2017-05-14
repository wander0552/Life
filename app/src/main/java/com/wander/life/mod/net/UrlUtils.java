package com.wander.life.mod.net;

import android.os.Build;
import android.text.TextUtils;

import com.wander.base.context.AppContext;


public class UrlUtils {
    private static final String TAG = "UrlUtils";
    public static final String TEST_HOST = "http://221.238.18.36:8880";
    private static String UID_FETCH_URL="";


    //设备硬件信息获取唯一UID服务的url
    public static String getUidUrl(final String uid) {
        final String versionName = AppContext.APP_NAME + "_ar_" + AppContext.VERSION_NAME;
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(uid) && !uid.equals("0")) {
            sb.append("&uid=").append(uid);
        }
        sb.append("&mac=").append(AppContext.DEVICE_ID);
        sb.append("&hd=").append(AppContext.DEVICE_ID);
        sb.append("&vmac=").append("");
        sb.append("&ver=").append(versionName);
        sb.append("&src=").append(AppContext.INSTALL_SOURCE);
        sb.append("&dev=").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append(" ").append(
                Build.DEVICE);
        String params = sb.toString();

        String url = UID_FETCH_URL;
        return url;
    }
}
