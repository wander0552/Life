package com.wander.base.utils;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.WindowManager;

/**
 * Created by wander on 2017/1/22.
 */

public class ScreenUtils {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void fullTranslucent(Activity activity){
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

}
