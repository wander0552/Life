package com.wander.life.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.wander.base.dir.DirsUtils;
import com.wander.base.log.WLog;
import com.wander.base.utils.BitmapTools;
import com.wander.base.utils.ImageUtil;
import com.wander.base.utils.ShotUtils;
import com.wander.life.R;
import com.wander.life.bean.LetterItem;
import com.wander.life.mod.editor.EditorManage;
import com.wander.life.presenter.EditPresenter;
import com.wander.life.ui.adapter.EditAdapter;
import com.wander.life.ui.adapter.cell.EditBaseCell;
import com.wander.life.ui.adapter.cell.EditCell;
import com.wander.life.ui.adapter.cell.ImageCell;
import com.wander.life.ui.iviews.IEditView;
import com.wander.life.ui.widget.EditRecyclerView;
import com.wander.life.utils.ToastUtils;
import com.wander.life.widget.recycler.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.flowable.FlowableFromArray;
import io.reactivex.schedulers.Schedulers;

public class EditorActivity extends PresenterActivity<EditPresenter> implements IEditView {
    //调用系统相册-选择图片
    private static final int IMAGE = 1;

    private EditRecyclerView mRecyclerView;
    private EditAdapter mAdapter;
    private ImageView edit_bar_back, edit_bar_share, edit_bar_theme;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_editor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initRecycler();
    }

    private void initView() {
        edit_bar_back = (ImageView) findViewById(R.id.edit_bar_back);
        edit_bar_share = (ImageView) findViewById(R.id.edit_bar_share);
        edit_bar_theme = (ImageView) findViewById(R.id.edit_bar_theme);


    }


    @Override
    protected EditPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new EditPresenter(this, this);
        }
        return mPresenter;
    }

    @Override
    protected void initEventAndData() {

    }

    private void initRecycler() {
        mRecyclerView = (EditRecyclerView) findViewById(R.id.editor_recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EditAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.add(new EditCell(mPresenter.createEditItem()));
    }


    public void editClick(View view) {
        switch (view.getId()) {
            case R.id.edit_bar_back:
                mAdapter.add(new EditCell(new LetterItem()));
                break;
            case R.id.edit_bar_share:
                goChoosePic();

                break;
            case R.id.edit_bar_theme:
                mAdapter.add(new ImageCell(new LetterItem()));
                break;
            case R.id.mark_bold:
                mPresenter.saveLetter();
                break;
            case R.id.mark_bold2:
                EditorManage editorManage = new EditorManage();
                editorManage.screenShot(mAdapter.getData(), mRecyclerView);
//                scrollShot();
                break;
            case R.id.mark_bold3:
                "a".charAt(2);
                break;
            default:
                break;
        }

    }


    private void goChoosePic() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }

    }

    private void showImage(String imagePath) {
        WLog.e(TAG, imagePath);
        LetterItem letterItem = new LetterItem();
        letterItem.setImageFile("file://" + imagePath);
        mAdapter.add(new ImageCell(letterItem));

    }

}
