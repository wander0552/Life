package com.wander.life.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wander.life.R;


/**
 * Created by wander on 2016/7/13.
 */
public class ImageLoaderUtils {

    private static volatile DisplayImageOptions singerOption;
    private static volatile DisplayImageOptions roundedOption;
    private static volatile DisplayImageOptions circleOption;
    private static DisplayImageOptions DefaultOption;

    public static DisplayImageOptions getRoundedImage() {
        if (roundedOption == null) {
            synchronized (ImageLoaderUtils.class) {
                if (roundedOption == null) {
                    roundedOption = new DisplayImageOptions.Builder().cacheOnDisk(true)
                            .cacheInMemory(true)
                            .displayer(new RoundedBitmapDisplayer(10))
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .imageScaleType(ImageScaleType.EXACTLY)
                            .showImageOnLoading(R.drawable.image_default)
                            .showImageForEmptyUri(R.drawable.image_default)
                            .showImageOnFail(R.drawable.image_default)
                            .build();
                }
            }
        }
        return roundedOption;
    }

    public static DisplayImageOptions getCircleImage() {
        if (circleOption == null) {
            synchronized (ImageLoaderUtils.class) {
                if (circleOption == null) {
                    circleOption = new DisplayImageOptions.Builder().cacheOnDisk(true)
                            .cacheInMemory(true)
                            .displayer(new CircleBitmapDisplayer())
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .imageScaleType(ImageScaleType.EXACTLY)
                            .showImageForEmptyUri(R.drawable.image_default)
                            .showImageOnFail(R.drawable.image_default)
                            .build();
                }
            }
        }
        return circleOption;
    }

    public static DisplayImageOptions getDefaultImage() {
        if (DefaultOption == null) {
            synchronized (ImageLoaderUtils.class) {
                if (DefaultOption == null) {
                    DefaultOption = new DisplayImageOptions.Builder().cacheOnDisk(true)
                            .cacheInMemory(true)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .imageScaleType(ImageScaleType.EXACTLY)
                            .showImageOnLoading(R.drawable.image_default)
                            .showImageForEmptyUri(R.drawable.image_default)
                            .showImageOnFail(R.drawable.image_default)
                            .build();
                }
            }
        }
        return DefaultOption;
    }

    public static DisplayImageOptions getSingerOptions() {
        if (singerOption == null) {
            synchronized (ImageLoaderUtils.class) {
                if (singerOption == null) {
                    singerOption = new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .cacheOnDisk(true)
                            .resetViewBeforeLoading(true)
                            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                            .showImageOnLoading(R.drawable.image_default)
                            .showImageOnFail(R.drawable.image_default)
                            .showImageForEmptyUri(R.drawable.image_default)
                            .bitmapConfig(Bitmap.Config.RGB_565)  // default
                            .displayer(new SimpleBitmapDisplayer())
                            .build();
                }
            }
        }
        return singerOption;
    }

//    //    static String[] size = new String[]{"150","700"};
//    public static ImageLoadingListener getSingerHeaderFailed() {
//        return new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                super.onLoadingFailed(imageUri, view, failReason);
//                if (view == null || imageUri == null) {
//                    return;
//                }
//                ImageLoader.getInstance().displayImage(UrlUtils.getSingerHeader(imageUri, "150"), (ImageView) view, ImageLoaderUtils.getSingerOptions());
//            }
//        };
//    }
}
