package com.wander.life.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wander.base.log.WLog;
import com.wander.base.utils.ScreenUtils;
import com.wander.life.R;
import com.wander.life.bean.WorldInfo;
import com.wander.life.presenter.WorldDetailPresenter;
import com.wander.life.ui.adapter.WorldCardAdapter;
import com.wander.life.ui.iviews.IWorldDetail;

public class WorldDetailActivity extends PresenterActivity<WorldDetailPresenter> implements IWorldDetail {

    private String url;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearManager;
    private WorldCardAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_world_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
           getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initView();


    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.world_detail_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("详情介绍");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AppBarLayout mBarLayout = (AppBarLayout) findViewById(R.id.world_detail_bar_layout);
        mBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                WLog.e(TAG,"\t"+verticalOffset);

            }
        });

        findViewById(R.id.world_detail_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorldDetailActivity.this,WriteActivity.class));
            }
        });


        initRecycler();


    }

    private void initRecycler() {
        mRecyclerView = (RecyclerView) findViewById(R.id.world_detail_recycler);
        mLinearManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearManager);
        mAdapter = new WorldCardAdapter(mContext, WorldInfo.getList());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                WLog.e(TAG,"recycler\t"+dx+"\tdy"+dy);
            }
        });
    }


    @Override
    protected WorldDetailPresenter getPresenter() {
        if (mPresenter == null){
            mPresenter = new WorldDetailPresenter(this,this);
        }
        return mPresenter;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.world_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.world_menu_search){

        }else if (itemId == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initEventAndData() {
        Bundle extras = getIntent().getExtras();
        url = "http://s9.sinaimg.cn/orignal/69db613dgd5775c5712f8&690";
        if (extras != null) {
            url = extras.getString("url");
        }
        ImageView imageView = (ImageView) findViewById(R.id.world_detail_image);
        ImageLoader.getInstance().displayImage(url, imageView);
    }
}
