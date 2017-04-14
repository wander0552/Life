package com.wander.life.ui.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wander.base.log.WLog;
import com.wander.base.utils.PrefsUtils;
import com.wander.base.utils.ScreenUtils;
import com.wander.life.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class EntryActivity extends Activity {

    private ImageView entry_bg;
    private boolean inited;
    private SimpleImageLoadingListener loadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            super.onLoadingComplete(imageUri, view, loadedImage);
            WLog.e("entryActivity", "image show" + String.valueOf(System.currentTimeMillis() - start));
            goSwitch();
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            super.onLoadingFailed(imageUri, view, failReason);
            goSwitch();
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
            super.onLoadingCancelled(imageUri, view);
            goSwitch();
        }
    };
    private String LAST_IMAGE_URL = "LAST_IMAGE_URL";

    private void goSwitch() {
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        inited = true;
                        finish();
                    }
                });
    }

    private long start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ScreenUtils.fullTranslucent(this);
        }
        if (!inited) {
            initView();
//        loadImageOne();
            loadImage();
        } else {
            goSwitch();
        }
    }

    boolean firstFocus = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (firstFocus && !inited) {
                goInitOnBack();
                firstFocus = false;
            }
        }

    }

    private void goInitOnBack() {

    }

    private void loadImageOne() {
        entry_bg.setImageResource(R.mipmap.entry_bg);
        goSwitch();
    }

    private void loadImage() {
        String url = "https://a-ssl.duitang.com/uploads/item/201603/20/20160320215950_YtEhG.thumb.700_0.jpeg";
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(false).bitmapConfig(Bitmap.Config.ARGB_8888).imageScaleType(ImageScaleType.NONE).build();
        start = System.currentTimeMillis();
        File localImage = ImageLoader.getInstance().getDiskCache().get(url);
        WLog.e("entryActivity", "find file" + String.valueOf(System.currentTimeMillis() - start));
        if (localImage.exists()) {
            WLog.e("entry_bg", localImage.getAbsolutePath());
//            Bitmap bitmap = ImageUtil.getBitmap(localImage.getAbsolutePath());
//            entry_bg.setImageBitmap(bitmap);
//            goSwitch();
            ImageLoader.getInstance().displayImage(url, entry_bg, options, loadingListener);
            PrefsUtils.savePrefString(LAST_IMAGE_URL, url);
        } else {
            String imageUrl = PrefsUtils.loadPrefString(LAST_IMAGE_URL);
            if (TextUtils.isEmpty(imageUrl)) {
                entry_bg.setImageResource(R.mipmap.entry_bg);
                goSwitch();

            }
            ImageLoader.getInstance().displayImage(imageUrl, entry_bg, options, loadingListener);
            DisplayImageOptions options1 = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();
            ImageLoader.getInstance().loadImage(url, options1, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        entry_bg = (ImageView) findViewById(R.id.entry_bg);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        entry_bg.setImageBitmap(null);
    }
}
