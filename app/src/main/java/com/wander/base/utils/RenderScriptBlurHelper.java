package com.wander.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RSRuntimeException;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;


/**
 * Simple helper used to blur a bitmap thanks to render script.
 */
public class RenderScriptBlurHelper {

    /**
     * Log cat
     */
    private static final String TAG = RenderScriptBlurHelper.class.getSimpleName();

    /**
     * Non instantiable class.
     */
    private RenderScriptBlurHelper() {

    }

    /**
     *
     * @param context
     * @param org
     * @param target
     * @param radius  (0,25]
     */
    public static void goBlur(final Context context, final View org, final View target, final float radius, final View mView) {
        org.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                target.setVisibility(View.VISIBLE);
                org.getViewTreeObserver().removeOnPreDrawListener(this);
                org.buildDrawingCache();
                Bitmap bitmap = org.getDrawingCache();
                Drawable drawable = RenderScriptBlurHelper.blurToDrawable(context, bitmap, target, radius);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mView.setBackground(drawable);
                } else {
                    mView.setBackgroundDrawable(drawable);
                }
                target.setVisibility(View.GONE);
                return true;
            }
        });
    }

    /**
     * @param context
     * @param org     取背景色的view
     * @param target  设置的view
     */
    public static void goBlur(final Context context, final View org, final View target) {
        org.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                org.getViewTreeObserver().removeOnPreDrawListener(this);
                org.buildDrawingCache();
                Bitmap bitmap = org.getDrawingCache();
                RenderScriptBlurHelper.blur(context, bitmap, target, 20);
                return true;
            }
        });
    }

    /**
     * 圆角模糊处理
     *
     * @param mContext
     * @param bkg      模糊的背景图片，需已经设置在view上之后获取
     * @param view     设置模糊的位置
     */
    public static void blur(Context mContext, Bitmap bkg, View view, float radius) {
        if (bkg == null || bkg.isRecycled()) {
            return;
        }
        try {
            long startMs = System.currentTimeMillis();
            float scaleFactor = 3f;

            Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                    (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
//            //画一个和原始图片一样大小的圆角矩形
//            RectF rectF = new RectF(0, 0, bkg.getWidth(), bkg.getHeight());
//            canvas.drawRoundRect(rectF, 50, 50, paint);
//            设置相交模式
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//            paint.setFlags(Paint.FILTER_BITMAP_FLAG);

//            ColorMatrix colorMatrix = new ColorMatrix();
//            colorMatrix.setSaturation(0.9f);
//            ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
//                    colorMatrix);
//            paint.setColorFilter(colorMatrixFilter);
            canvas.drawBitmap(bkg, 0, 0, paint);

            overlay = RenderScriptBlurHelper.doBlur(overlay, (int) radius, true, mContext);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(new BitmapDrawable(mContext.getResources(), overlay));
            } else {
                view.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), overlay));
            }
//            WLog.e("blur_time", System.currentTimeMillis() - startMs + "ms");
        } catch (Exception e) {

        }
    }

    /**
     * 模糊处理
     *
     * @param mContext
     * @param bkg      模糊的背景图片，需已经设置在view上之后获取
     * @param view     设置模糊的位置
     * @return 模糊的图片背景
     */
    public static Drawable blurToDrawable(Context mContext, Bitmap bkg, View view, float radius) {
        if (bkg == null || bkg.isRecycled()) {
            return null;
        }
        try {
            long startMs = System.currentTimeMillis();
            float scaleFactor = 4f;

            Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
                    (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(overlay);
            canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
            canvas.scale(1 / scaleFactor, 1 / scaleFactor);
            Paint paint = new Paint();
//            //画一个和原始图片一样大小的圆角矩形
//            RectF rectF = new RectF(0, 0, bkg.getWidth(), bkg.getHeight());
//            canvas.drawRoundRect(rectF, 50, 50, paint);
//            //设置相交模式
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//            paint.setFlags(Paint.FILTER_BITMAP_FLAG);

//            ColorMatrix colorMatrix = new ColorMatrix();
//            colorMatrix.setSaturation(0.85f);
//            ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
//                    colorMatrix);
//            paint.setColorFilter(colorMatrixFilter);
            canvas.drawBitmap(bkg, 0, 0, paint);

            overlay = RenderScriptBlurHelper.doBlur(overlay, (int) radius, true, mContext);
            BitmapDrawable background = new BitmapDrawable(mContext.getResources(), overlay);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(new BitmapDrawable(mContext.getResources(), overlay));
            } else {
                view.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), overlay));
            }
//            WLog.e("blur_time", System.currentTimeMillis() - startMs + "ms");
            return background;
        } catch (Exception e) {
            return null;
        }
    }

//    public static Bitmap doGray(Context context, Bitmap mInBitmap) {
//        if (mInBitmap.getConfig() == Bitmap.Config.RGB_565) {
//            mInBitmap = convertRGB565toARGB888(mInBitmap);
//        }
//
//        try {
//            RenderScript mRenderScript = RenderScript.create(context);
//            ScriptC_Rray mGrayScript = new ScriptC_Rray(mRenderScript);
//
//            final Allocation input = Allocation.createFromBitmap(mRenderScript, mInBitmap, Allocation.MipmapControl.MIPMAP_NONE,
//                    Allocation.USAGE_SCRIPT);
//            final Allocation output = Allocation.createTyped(mRenderScript, input.getType());
//
//            mGrayScript.set_gPos(200);
//            mGrayScript.forEach_root(input, output);
//            output.copyTo(mInBitmap);
//            return mInBitmap;
//        } catch (RSRuntimeException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * blur a given bitmap
     *
     * @param sentBitmap       bitmap to blur
     * @param radius           blur radius
     * @param canReuseInBitmap true if bitmap must be reused without blur
     * @param context          used by RenderScript, can be null if RenderScript disabled
     * @return blurred bitmap
     */
    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap, Context context) {
        Bitmap bitmap;

        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (bitmap.getConfig() == Bitmap.Config.RGB_565) {
            // RenderScript hates RGB_565 so we convert it to ARGB_8888
            // (see http://stackoverflow.com/questions/21563299/
            // defect-of-image-with-scriptintrinsicblur-from-support-library)
            bitmap = convertRGB565toARGB888(bitmap);
        }

        try {
            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        } catch (RSRuntimeException e) {
            Log.e(TAG, "RenderScript known error : https://code.google.com/p/android/issues/detail?id=71347 "
                    + "continue with the FastBlur approach.");
        }

        return null;
    }

    private static Bitmap convertRGB565toARGB888(Bitmap bitmap) {
        return bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }
}
