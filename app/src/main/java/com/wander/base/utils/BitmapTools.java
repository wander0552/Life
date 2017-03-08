package com.wander.base.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class BitmapTools {
    public static Bitmap readBitmapAutoSize(String filePath, int outWidth,
                                            int outHeight) {
        // outWidth和outHeight是目标图片的最大宽度和高度，用作限制
        FileInputStream fs = null;
        BufferedInputStream bs = null;
        try {
            fs = new FileInputStream(filePath);
            bs = new BufferedInputStream(fs);
            BitmapFactory.Options options = setBitmapOption(filePath, outWidth, outHeight);
            return BitmapFactory.decodeStream(bs, null, options);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bs.close();
                fs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static BitmapFactory.Options setBitmapOption(String filePath,
                                                         int width, int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        // 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
        BitmapFactory.decodeFile(filePath, opt);

        int outWidth = opt.outWidth; // 获得图片的实际高和宽
        int outHeight = opt.outHeight;
        opt.inDither = false;
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        // 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
        opt.inSampleSize = 1;
        // 设置缩放比,1表示原比例，2表示原来的四分之一....
        // 计算缩放比
        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
            int sampleSize = (outWidth / width + outHeight / height) / 2;
            opt.inSampleSize = sampleSize;
        }

        opt.inJustDecodeBounds = false;// 最后把标志复原
        return opt;
    }

    public static BitmapFactory.Options getBitmapOptions(int width, int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        opt.inDither = false;
        opt.inPreferredConfig = Bitmap.Config.RGB_565; // 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
        opt.inSampleSize = 1;// 设置缩放比,1表示原比例，2表示原来的四分之一....
        opt.inJustDecodeBounds = false;// 最后把标志复原
        return opt;
    }

    //画质降低
    public static BitmapFactory.Options getBitmapOptions(Context context, int res, int width, int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;//设置为true后，调用decode方法时，并不真正为图片分配内存，也不会返回bitmap,但可以通过Options得到bitmap信息
        //BitmapFactory.decodeResource(context.getResources(), res, opt);//不会真正被解码，只会将size存到opt里
        opt.inDither = false;
        //opt.inPurgeable = true;
        opt.inPreferredConfig = Bitmap.Config.RGB_565; // 设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上
        opt.inSampleSize = 2;// 设置缩放比,1表示原比例，2表示原来的四分之一....
        // 计算缩放比，其实是在加载比显示尺寸还要大的图的时候才有效果，在智能电视用不到
        /*if (opt.outHeight != 0 && opt.outWidth != 0 && width != 0 && height != 0) {
			int yRatio = (int)Math.ceil(opt.outHeight/width);  
			int xRatio = (int)Math.ceil(opt.outWidth/height);  
			if(xRatio>1||yRatio>1){
				opt.inSampleSize = xRatio;
			}
		}*/

        opt.inJustDecodeBounds = false;// 最后把标志复原
        return opt;
    }

    public static Bitmap createBitmapByInputstream(Context context, int res, int width, int height, boolean isReduce) {
        BitmapFactory.Options opts;
        if (isReduce) {
            opts = getBitmapOptions(context, res, width, height);
        } else {
            opts = getBitmapOptions(width, height);
        }
        InputStream is = context.getResources().openRawResource(res);
        return BitmapFactory.decodeStream(is, null, opts);
    }

    public static Bitmap scaleBitmap(Bitmap source, float sx, float sy) {
        Matrix m = new Matrix();
        m.preScale(sx, sy);
        Bitmap bmp = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, false);
        return bmp;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public static boolean bitmapIsRecycled(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            return false;
        }
        return true;
    }
}
