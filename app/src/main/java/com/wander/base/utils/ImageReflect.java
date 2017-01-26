package com.wander.base.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.view.View;

public class ImageReflect {
    private static int reflectImageHeight = 100;

    public static Bitmap convertViewToBitmap(View paramView) {
        paramView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, 0));
        paramView.layout(0, 0, paramView.getMeasuredWidth(), paramView.getMeasuredHeight());
        paramView.buildDrawingCache();
        return paramView.getDrawingCache();
    }

    public static Bitmap createCutReflectedImage(Bitmap paramBitmap) {
        int k = paramBitmap.getWidth();
        int j = paramBitmap.getHeight();
        int i = j / 2;
        Object localObject = new Matrix();
        ((Matrix) localObject).preScale(1.0F, -1.0F);
        Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, j - i, k, i, (Matrix) localObject, true);
        paramBitmap = Bitmap.createBitmap(k, i, Bitmap.Config.ARGB_8888);
        localObject = new Canvas(paramBitmap);
        ((Canvas) localObject).drawBitmap(localBitmap, 0.0F, 0.0F, null);
        LinearGradient localLinearGradient = new LinearGradient(0.0F, 0.0F, 0.0F, paramBitmap.getHeight(), -2130706433, 16777215, Shader.TileMode.CLAMP);
        Paint localPaint = new Paint();
        localPaint.setShader(localLinearGradient);
        localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        ((Canvas) localObject).drawRect(0.0F, 0.0F, k, paramBitmap.getHeight(), localPaint);
        if (!localBitmap.isRecycled()) {
            localBitmap.recycle();
        }
        System.gc();
        return paramBitmap;
    }

    public static Bitmap createCutReflectedImage(Bitmap paramBitmap, int paramInt) {
        int j = paramBitmap.getWidth();
        int i = paramBitmap.getHeight();
        if (i <= reflectImageHeight + paramInt) {
            paramBitmap = null;
        }
        for (; ; ) {
            Object localObject = new Matrix();
            ((Matrix) localObject).preScale(1.0F, -1.0F);
            Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, i - reflectImageHeight - paramInt, j, reflectImageHeight, (Matrix) localObject, true);
            paramBitmap = Bitmap.createBitmap(j, reflectImageHeight, Bitmap.Config.ARGB_8888);
            Canvas localCanvas = new Canvas(paramBitmap);
            localCanvas.drawBitmap(localBitmap, 0.0F, 0.0F, null);
            LinearGradient localLinearGradient = new LinearGradient(0.0F, 0.0F, 0.0F, paramBitmap.getHeight(), -2130706433, 16777215, Shader.TileMode.CLAMP);
            localObject = new Paint();
            ((Paint) localObject).setShader(localLinearGradient);
            ((Paint) localObject).setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            localCanvas.drawRect(0.0F, 0.0F, j, paramBitmap.getHeight(), (Paint) localObject);
            if (!localBitmap.isRecycled()) {
                localBitmap.recycle();
            }
            System.gc();
            return paramBitmap;
        }
    }

    public static Bitmap createReflectedImage(Bitmap paramBitmap, int paramInt) {
        int j = paramBitmap.getWidth();
        int i = paramBitmap.getHeight();
        if (i <= paramInt) {
            paramBitmap = null;
        }
        for (; ; ) {
            Object localObject1 = new Matrix();
            ((Matrix) localObject1).preScale(1.0F, -1.0F);
            Object localObject2 = Bitmap.createBitmap(paramBitmap, 0, i - paramInt, j, paramInt, (Matrix) localObject1, true);
            paramBitmap = Bitmap.createBitmap(j, paramInt, Bitmap.Config.ARGB_8888);
            localObject1 = new Canvas(paramBitmap);
            ((Canvas) localObject1).drawBitmap((Bitmap) localObject2, 0.0F, 0.0F, null);
            LinearGradient localLinearGradient = new LinearGradient(0.0F, 0.0F, 0.0F, paramBitmap.getHeight(), -2130706433, 16777215, Shader.TileMode.CLAMP);
            localObject2 = new Paint();
            ((Paint) localObject2).setShader(localLinearGradient);
            ((Paint) localObject2).setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            ((Canvas) localObject1).drawRect(0.0F, 0.0F, j, paramBitmap.getHeight(), (Paint) localObject2);
            return paramBitmap;
        }
    }
}
