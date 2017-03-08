package com.wander.base.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.View;

import com.wander.base.log.WLog;

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
