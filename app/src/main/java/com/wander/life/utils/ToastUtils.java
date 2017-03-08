package com.wander.life.utils;

import android.view.View;
import android.widget.Toast;

import com.wander.base.log.WLog;
import com.wander.life.App;


/**
 * Toast工具类，这样做的目的是避免出现 第一个toast完全消失，第二个toast才会出现的问题
 */
public class ToastUtils {
    private static Toast toast;

    private static String TAG = "toast";


    public static void makeTextShort(int content) {
        if (content < -1) {
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(App.getAppContext(), "", Toast.LENGTH_SHORT);
        }
        View toastView = toast.getView();
        if (toastView != null) {
            toast.setView(toastView);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(content);
        toast.show();
    }

    public static void makeTextShort(CharSequence content) {
        if (content == null) {
            return;
        }
        WLog.e(TAG,content.toString());
        if (null == toast) {
            toast = Toast.makeText(App.getAppContext(), "", Toast.LENGTH_SHORT);
        }
        View toastView = toast.getView();
        if (toastView != null) {
            toast.setView(toastView);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(content);
        toast.show();
    }

    public static void makeTextLong(CharSequence content) {
        if (content == null) {
            return;
        }
        WLog.e(TAG,content.toString());
        if (null == toast) {
            toast = Toast.makeText(App.getAppContext(), "", Toast.LENGTH_LONG);
        }
        View toastView = toast.getView();
        if (toastView != null) {
            toast.setView(toastView);
        }
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(content);
        toast.show();
    }

}
