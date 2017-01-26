package com.wander.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.wander.base.log.WLog;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


/**
 * Created by wander on 2016/6/22.
 * email 805677461@qq.com
 */
public class NetworkUtils {

    public static boolean isWifi(Context ctx) {
        ConnectivityManager mConnectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            return false;
        }
        int netType = info.getType();
        int netSubType = info.getSubtype();
        WLog.d("NetworkUtils", "netType:" + netType + " netSubType:" + netSubType);
        if (ConnectivityManager.TYPE_WIFI == netType) {
            return info.isConnectedOrConnecting();
        }
        return false;
    }

    /**
     * 检查网络是否可用
     *
     * @return true可用, false不可用
     */
    public static boolean isNetworkValidate(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
        if (activeNetworkInfo != null
                && activeNetworkInfo.isConnected()
                && activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
            return activeNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取本地ip地址
     * @return
     */
    public static String getLocalIP () {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {

                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "0.0.0.0";
    }

    /**
     * 获取手机Wifi的mac地址
     *
     * @return mac地址, 获取失败返回null
     */
    public static String getWifiMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.getConnectionInfo().getMacAddress();
    }
}
