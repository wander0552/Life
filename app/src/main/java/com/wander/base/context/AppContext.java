package com.wander.base.context;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import com.wander.base.log.WLog;
import com.wander.base.net.HttpClient;
import com.wander.base.utils.PrefsUtils;
import com.wander.base.utils.WDate;
import com.wander.life.App;
import com.wander.life.utils.Constants;
import com.wander.life.mod.net.UrlUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Locale;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;


public class AppContext {
    private static final String TAG = "AppContext";
    private static final String DEVICE_ID_FILENAME = ".id";
    private static boolean sInited = false;
    public static String APP_NAME = "kwmusicTV";
    public static String DEVICE_ID = "000000";
    public static int VERSION_CODE;
    public static String VERSION_NAME = "1.0.0.1";
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static String mChannel = "";
    public static String mBrand = "";

    // 打包时间
    public static String PACK_TIME = "";
    public static String INSTALL_SOURCE = "wander.apk";

    // 总内存(B为单位）
    public static long TOTAL_MEM;
    // 总内存(MB为单位）
    public static long TOTAL_MEM_MB;
    public static boolean appShowing;
    public static boolean debugMode = false;


    public AppContext() {
    }

    public static synchronized boolean init(Context context) {
        if (sInited) {
            return true;
        } else {
            try {
                WindowManager wm1 = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm1.getDefaultDisplay();
                SCREEN_WIDTH = display.getWidth();
                SCREEN_HEIGHT = display.getHeight();
                WLog.i("AppContext", String.format("Screen width: %s  hight: %s", new Object[]{Integer.valueOf(SCREEN_WIDTH), Integer.valueOf(SCREEN_HEIGHT)}));

                PackageManager packageManager = context.getPackageManager();
                PackageInfo pi1 = packageManager.getPackageInfo(context.getPackageName(), 0);
                VERSION_CODE = pi1.versionCode;
                VERSION_NAME = pi1.versionName;
                WLog.i("AppContext", "install VERSION_CODE:" + VERSION_CODE);
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                mChannel = applicationInfo.metaData.getString("UMENG_CHANNEL");

                if (!TextUtils.isEmpty(mChannel)) {
                    INSTALL_SOURCE = APP_NAME + "_" + VERSION_CODE + "_" + mChannel + ".apk";
                }else{
                    INSTALL_SOURCE = APP_NAME + "_" + VERSION_CODE + ".apk";
                }

                TOTAL_MEM = getTotalMemory();
                TOTAL_MEM_MB =  TOTAL_MEM /1024 / 1024;

                String e = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                if (e == null) {
                    WifiManager pi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wm = pi.getConnectionInfo();
                    e = wm.getMacAddress();
                }
                if (e == null) {
                    e = "000000";
                }
                DEVICE_ID = e;
                WLog.i("AppContext", "DEVICE_ID:" + DEVICE_ID);
            } catch (Exception var5) {
                WLog.printStackTrace(var5);
                return false;
            }finally {

                WLog.i("AppContext", "MODEL: " + Build.MODEL);
                WLog.i("AppContext", "BOARD: " + Build.BOARD);
                WLog.i("AppContext", "BRAND: " + Build.BRAND);
                WLog.i("AppContext", "DEVICE: " + Build.DEVICE);
                WLog.i("AppContext", "PRODUCT: " + Build.PRODUCT);
                WLog.i("AppContext", "DISPLAY: " + Build.DISPLAY);
                WLog.i("AppContext", "HOST: " + Build.HOST);
                WLog.i("AppContext", "ID: " + Build.ID);
                WLog.i("AppContext", "USER: " + Build.USER);
                WLog.i("AppContext", "TOTALMEM: " + TOTAL_MEM_MB);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String s = "";
                    for (String abi:Build.SUPPORTED_ABIS){
                        s+=abi;
                    }
                    WLog.i(TAG, "CPU_API: " + s);
                } else {
                    WLog.i(TAG, "CPU_API: " + Build.CPU_ABI);
                }

                mBrand = Build.BRAND;

//                loadAppUid();
            }

            sInited = true;
            return true;
        }
    }

    public static boolean isChinese(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        return locale.toString().equals("zh_CN");
    }

    public static boolean isScreenPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    private static String randDeviceId() {
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        boolean i = false;
        int i1 = rand.nextInt(5);
        i1 = i1 == 0 ? 1 : i1;
        i1 *= 10000;
        i1 += rand.nextInt(i1);
        sb.append(i1);
        i1 = rand.nextInt(5) + 5;
        i1 *= 100000;
        i1 += rand.nextInt(i1);
        sb.append(i1);
        return sb.toString();
    }

    /**
     * 获取当前进程名
     * @return
     */
    public static String getCurProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) App.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static boolean isX86Eabi() {
        return "x86".equalsIgnoreCase(Build.CPU_ABI);
    }

    public static String getAppUid(){
        String uid = PrefsUtils.loadPrefString(App.getAppContext(), Constants.PREFERENCE_UID_INTERNAL, Constants.DEF_ERROR_UID);
        if (TextUtils.isEmpty(uid) || Constants.DEF_ERROR_UID.equals(uid)) {//get uid from network
            loadAppUid();
            return Constants.DEF_ERROR_UID;
        }
        return uid;
    }

    private static void loadAppUid(){
        long lastTime = PrefsUtils.loadPrefLong(App.getAppContext(),
                Constants.PREFERENCE_UID_LAST_INTERNAL, 0L);
        if (Math.abs(System.currentTimeMillis() - lastTime) < WDate.T_MS_DAY) {
            //每天只取一次
            return;
        }
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                String uidurl = UrlUtils.getUidUrl(null);
                if(!TextUtils.isEmpty(uidurl)){
                    String result = HttpClient.syncGetString(uidurl);
                    if (!TextUtils.isEmpty(result)) {
                        int index = result.toUpperCase().indexOf("ID=");
                        if (index == 0) {
                            String mUid = result.replaceAll("ID=", "");
                            if (TextUtils.isEmpty(mUid) || "0".equals(mUid)) {
                                return;
                            }
                            //保存相关数据
                            PrefsUtils.savePrefString(App.getAppContext(),
                                    Constants.PREFERENCE_UID_INTERNAL, mUid);
                            PrefsUtils.savePrefLong(App.getAppContext(),
                                    Constants.PREFERENCE_UID_LAST_INTERNAL, System.currentTimeMillis());
                        }
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private static long getTotalMemory() {
        String[] arrayOfString;
        long initial_memory = 0;

        try {
            FileReader localFileReader = new FileReader("/proc/meminfo");
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 1024);
            try {
                String firstLine = localBufferedReader.readLine();
                if(firstLine == null) return initial_memory;
                arrayOfString = firstLine.split("\\s+");
                initial_memory = (long)(Integer.valueOf(arrayOfString[1]).intValue()) * 1024;
            } finally {
                localBufferedReader.close();
            }
        } catch (Throwable e) {}
        return initial_memory;
    }

    public static long getAvailMemory(Context context){
        try {
            // 获取android当前可用内存大小
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            if(getAvailMemoryInfo(context, mi)) {
                return mi.availMem / (1024 * 1024);//MB单位
            }else{
                return 0;
            }
        } catch (Exception e) {

        }
        return  0;
    }

    public static boolean getAvailMemoryInfo(Context context, ActivityManager.MemoryInfo memoryInfo){
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            am.getMemoryInfo(memoryInfo);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}
