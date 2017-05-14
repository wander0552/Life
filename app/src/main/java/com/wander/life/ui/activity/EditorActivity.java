package com.wander.life.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.wander.base.log.WLog;
import com.wander.life.R;
import com.wander.life.bean.LetterItem;
import com.wander.life.mod.editor.EditorManage;
import com.wander.life.presenter.EditPresenter;
import com.wander.life.ui.adapter.EditAdapter;
import com.wander.life.ui.adapter.cell.EditCell;
import com.wander.life.ui.adapter.cell.ImageCell;
import com.wander.life.ui.iviews.IEditView;
import com.wander.life.ui.widget.EditRecyclerView;
import com.wander.life.widget.recycler.cell.TextCell;

public class EditorActivity extends PresenterActivity<EditPresenter> implements IEditView {
    //调用系统相册-选择图片
    private static final int IMAGE = 1;

    private EditRecyclerView mRecyclerView;
    private EditAdapter mAdapter;
    private ImageView edit_bar_back, edit_bar_share, edit_bar_theme;
    private LinearLayoutManager mLinearManager;

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
        mLinearManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearManager);
        mAdapter = new EditAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.add(new TextCell("<html>  \n" +
                "<h2>数字显示</h2>  \n" +
                "<ul>" +
                "<li>第一天</li>" +
                "<li>第二天</li>" +
                "<li>第三天</li>" +
                "<li>第四天</li>" +
                "</ul>  \n" +
                "  \n" +"<font\n" +
                " color=\"#ff0000\">红色</font>"+
                "<h2>数字显示,自己确定开始数字</h2>  \n" +
                "<ol start=\"5\">  \n" +
                "<li>第一天</li>  \n" +
                "<li>第二天</li>  \n" +
                "<li>第三天</li>  \n" +
                "<li>第四天</li>  \n" +
                "</ol>  \n" +
                "<html>  "));
        mAdapter.add(new EditCell(mPresenter.createEditItem()));
        mAdapter.add(new ImageCell(new LetterItem()));
        mLinearManager.scrollToPosition(0);

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
