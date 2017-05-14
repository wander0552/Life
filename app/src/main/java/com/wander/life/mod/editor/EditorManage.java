package com.wander.life.mod.editor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.MainThread;
import android.support.v7.widget.RecyclerView;

import com.wander.base.dir.DirsUtils;
import com.wander.base.log.WLog;
import com.wander.base.utils.BitmapTools;
import com.wander.base.utils.ImageUtil;
import com.wander.life.mod.utils.IModBase;
import com.wander.life.ui.adapter.cell.EditBaseCell;
import com.wander.life.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * Created by wander on 2017/3/5.
 */

public class EditorManage implements IModBase {

    private List<Bitmap> bitmapList;

    @Override
    public void init() {

    }

    @Override
    public void release() {

    }


    @MainThread
    public void screenShot(List<EditBaseCell> data, final RecyclerView mRecyclerView) {
        if (data == null || data.isEmpty() || mRecyclerView == null) {
            return;
        }
        Paint paint = new Paint();
        bitmapList = new ArrayList<>();
        int measuredHeight = mRecyclerView.getMeasuredHeight();
        WLog.e(TAG, "measureHeight:" + measuredHeight);
        int shotHeight = 0;
        for (EditBaseCell cell : data) {
            shotHeight += cell.getHeight(mRecyclerView);
        }


        while (mRecyclerView.canScrollVertically(-1)) {
            mRecyclerView.scrollBy(0, -measuredHeight);
        }

        //绘制截图的背景
        final Bitmap bigBitmap = Bitmap.createBitmap(mRecyclerView.getMeasuredWidth(), shotHeight, Bitmap.Config.RGB_565);
        Canvas bigCanvas = new Canvas(bigBitmap);
        Drawable lBackground = mRecyclerView.getBackground();
        if (lBackground instanceof ColorDrawable) {
            ColorDrawable lColorDrawable = (ColorDrawable) lBackground;
            int lColor = lColorDrawable.getColor();
            bigCanvas.drawColor(lColor);
        }


        int drawOffset = 0;
        while (mRecyclerView.canScrollVertically(1)) {
            WLog.e(TAG, "drawOffset" + drawOffset);
            //每次重新获取新的布局
            mRecyclerView.setDrawingCacheEnabled(true);
//            getDrawingCache()中已经调用
//            mRecyclerView.buildDrawingCache();
            Bitmap bitmap = mRecyclerView.getDrawingCache();
            //调用这个方法会销毁当前的bitmap cache
//            mRecyclerView.setDrawingCacheEnabled(false);
            bigCanvas.drawBitmap(bitmap, 0, drawOffset, paint);
            drawOffset += measuredHeight;
            mRecyclerView.scrollBy(0, measuredHeight);
        }

        //不足一屏时的处理
        int top = measuredHeight - (shotHeight - drawOffset);

        WLog.e(TAG, "last" + top);
        if (top > 0) {
            mRecyclerView.setDrawingCacheEnabled(true);
            Bitmap bitmap = mRecyclerView.getDrawingCache();
            bigCanvas.drawBitmap(bitmap, new Rect(0, top, bitmap.getWidth(), bitmap.getHeight()),
                    new Rect(0, drawOffset, bigBitmap.getWidth(), bigBitmap.getHeight()), paint);
        }


        //恢复位置，可以先放置一张截图，或者是创建一个新的recyclerView来截图，同时可以截图时进行不同的处理
//        while (mRecyclerView.canScrollVertically(-1)) {
//            mRecyclerView.scrollBy(0, -measuredHeight);
//        }


//        mRecyclerView.scrollBy(0, scrollOffset);
//        int i1 = scrollOffset / measuredHeight;
//        for (int i = 0; i < i1; i++) {
//            mRecyclerView.scrollBy(0, measuredHeight);
//        }


        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                ImageUtil.saveBitmap(DirsUtils.getDir(DirsUtils.PICS) + "screenShot.jpeg", bigBitmap);
                e.onNext(true);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

                ToastUtils.makeTextShort("screen shot ok");
                mRecyclerView.destroyDrawingCache();
                BitmapTools.recycleBitmap(bigBitmap);

//               for (Bitmap bitmap:bitmapList){
//                   BitmapTools.recycleBitmap(bitmap);
//               }
//
//                bitmapList = null;
            }
        });

    }
}
