package com.wander.base.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by wander on 2017/3/4.
 */

public class ShotUtils {


    /**
     * need layout
     * @param view
     * @return
     */
    public static Bitmap loadBitmapFromView(View view) {
        if (view == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(canvas);
        return bitmap;

    }
}
