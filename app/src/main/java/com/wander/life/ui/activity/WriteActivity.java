package com.wander.life.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.wander.base.log.WLog;
import com.wander.base.uiUtils.SoftKeyboardHelper;
import com.wander.life.R;
import com.wander.life.bean.Letter;
import com.wander.life.database.DbUtils;
import com.wander.life.presenter.WritePresenter;
import com.wander.life.ui.iviews.IWriteView;
import com.wander.life.utils.ToastUtils;

import java.util.List;

public class WriteActivity extends PresenterActivity<WritePresenter> implements IWriteView, View.OnTouchListener {
    private boolean editing = false;
    private EditText mEditText;
    private String sHTML = "<li>\n" +
            "<strong>android:textAlignment</strong>    //设置EditText中文本显示的位置,center(居中),inherit(默认,居左边显示),viewStart(居左显示),viewEnd(居右显示),textStart(居左显示),textEnd(居右显示).这里需要注意的是最低支持的API版本是17,前两个可以在API14中使用,而后面使用就会报红线...</li>";
    private String ss2 = "<h1>This is heading 1</h1>\n" +
            "<h2>This is heading 2</h2>\n" +
            "<h3>This is heading 3</h3>\n" +
            "<h4>This is heading 4</h4>\n" +
            "<h5>This is heading 5</h5>\n" +
            "<h6>This is heading 6</h6>\n" +
            "\n" +
            "<p>请仅仅把标题标签用于标题文本。不要仅仅为了产生粗体文本而使用它们。请使用其它标签或 CSS 代替。</p>";
    private MenuItem menuItem1;
    private MenuItem menuItem2;
    private SoftKeyboardHelper keyboardHelper;
    private View mark_layout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_write;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEditText = (EditText) findViewById(R.id.write_edit);

        mEditText.append(Html.fromHtml(sHTML));
        mEditText.append(Html.fromHtml(ss2));
        mEditText.setCursorVisible(editing);
        mEditText.setOnTouchListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Letter letter = new Letter();
                letter.setContent(mEditText.getText().toString());
                letter.setType(Letter.LETTER_TYPE_DRAFT);
                DbUtils.saveLetter(letter);


                Snackbar.make(view, "恢复显示", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                List<Letter> letters = DbUtils.listLetter(Letter.LETTER_TYPE_DRAFT, 0, 50);
                                if (letters.size() > 0) {
                                    mEditText.setText(letters.get(0).getContent() + "read");
                                }

                            }
                        }).show();
            }
        });
    }

    private void initView() {
        mark_layout = findViewById(R.id.mark_layout);

        keyboardHelper = new SoftKeyboardHelper();
        keyboardHelper.observeSoftKeyboard(this, new SoftKeyboardHelper.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeyboardHeight, boolean visible) {
                mEditText.scrollBy(0, (int) (mark_layout.getHeight() + mEditText.getTextSize()));
                mark_layout.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        keyboardHelper.releaseListener();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.write_menu, menu);
        menuItem1 = menu.findItem(R.id.write_menu1);
        menuItem2 = menu.findItem(R.id.write_menu2);
        WLog.e(TAG, "menuCreate");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.write_menu1:
                if (editing) {
                    ToastUtils.makeTextShort("pic");
                } else {
                    ToastUtils.makeTextShort("delete");
                }
                break;
            case R.id.write_menu2:
                if (editing) {
                    ToastUtils.makeTextShort("save");
                } else {
                    ToastUtils.makeTextShort("share");
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected WritePresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new WritePresenter(this, this);
        }
        return mPresenter;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
