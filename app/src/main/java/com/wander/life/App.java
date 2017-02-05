package com.wander.life;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;
import com.wander.base.context.AppContext;
import com.wander.base.context.CrashHandler;
import com.wander.base.dir.DirsUtils;
import com.wander.base.log.BaseLogger;
import com.wander.base.log.WLog;
import com.wander.base.utils.NetworkStateUtil;

import java.io.File;


public class App extends MultiDexApplication {
    private static App appContext;
    private String TAG = "application";
    private double mAppCount;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;
        init();
        initSDK();

    }

/*    */

    /**
     * 字体不根据系统字体改变
     *//*
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }*/
    private void init() {
        //  初始化数据库

        // 设置日志级别
        WLog.init(AppContext.APP_NAME);
        initCrashHandler();
        if (!BuildConfig.DEBUG) {
            WLog.trace(false);
            WLog.debug(false);
        } else {
            WLog.trace(true);
            WLog.debug(true);
            WLog.getLogger().setLevel(BaseLogger.DEBUG);
        }

        // 初始化上下文
        AppContext.init(this);
        NetworkStateUtil.init(getApplicationContext());

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mAppCount++;
                WLog.e("myconut", "onActivityStarted myconut: " + mAppCount);
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                mAppCount--;
                WLog.e("myconut", "onStopped myconut: " + mAppCount);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });


    }

    private void initSDK() {
//        如果开发者调用Process.kill或者System.exit之类的方法杀死进程，请务必在此之前调用MobclickAgent.onKillProcess(Context context)方法，用来保存统计数据。
        //有盟统计
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_ANALYTICS_OEM);

        MobclickAgent.openActivityDurationTrack(false);// 禁止默认的页面统计方式，这样将不会再自动统计Activity

        MobclickAgent.enableEncrypt(true);//6.0.0版本及以后

        initImageLoad();
    }

    private void initCrashHandler() {
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    private void initImageLoad() {
        int memClass = ((ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int m = memClass / 6;
        File cacheDir = null;
        cacheDir = StorageUtils.getOwnCacheDirectory(this, DirsUtils.getDir(DirsUtils.PIC_CACHE));
        if (!cacheDir.exists()) {
            cacheDir = StorageUtils.getCacheDirectory(this);
        }
//        WLog.e("memoryCache",m+"\t"+cacheDir.getAbsolutePath());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75)
                // //设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(m * 1024 * 1024))
//				.memoryCache(new WeakMemoryCache())
                // default
//                .memoryCacheSize(m * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                // default
                .diskCacheSize(300 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .tasksProcessingOrder(QueueProcessingType.LIFO) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        L.writeLogs(false);

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        WLog.d(TAG, "onTerminate");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        WLog.w(TAG, "onLowMemory");
        System.gc();
    }

    private String tag = "trimMemory";

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level) {
            case TRIM_MEMORY_COMPLETE:
                WLog.e(tag, "内存不足，并且该进程在后台进程列表最后一个，马上就要被清理");
                break;
            case TRIM_MEMORY_MODERATE:
                WLog.e(tag, "内存不足，并且该进程在后台进程列表的中部。");
                break;
            case TRIM_MEMORY_BACKGROUND:
                WLog.e(tag, "内存不足，并且该进程是后台进程");
                break;
            default:
                break;
        }
    }

    public static App getAppContext() {
        return appContext;
    }

    public static void exitApp() {
        NetworkStateUtil.release(getAppContext());
        if (appContext != null) {
            MobclickAgent.onKillProcess(appContext);
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
