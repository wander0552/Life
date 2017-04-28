package com.wander.base.context;

/**
 * Created by wander on 2017/4/28.
 */

import android.os.Handler;
import android.os.Looper;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * @since 2017/4/12
 */

public class NeverCrash {

    private CrashHandler mCrashHandler;

    private static NeverCrash mInstance;

    private NeverCrash(){

    }

    private static NeverCrash getInstance(){
        if(mInstance == null){
            synchronized (NeverCrash.class){
                if(mInstance == null){
                    mInstance = new NeverCrash();
                }
            }
        }

        return mInstance;
    }

    public static void init(CrashHandler crashHandler){
        getInstance().setCrashHandler(crashHandler);
    }

    private void setCrashHandler(CrashHandler crashHandler){

        mCrashHandler = crashHandler;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        if (mCrashHandler != null) {//捕获异常处理
                            mCrashHandler.uncaughtException(Looper.getMainLooper().getThread(), e);
                        }
                    }
                }
            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if(mCrashHandler!=null){//捕获异常处理
                    mCrashHandler.uncaughtException(t,e);
                }
            }
        });

    }

    public interface CrashHandler{
        void uncaughtException(Thread t,Throwable e);
    }
}
