package com.wander.life.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.wander.life.R;
import com.wander.life.bean.WorldInfo;
import com.wander.life.presenter.WorldPresenter;
import com.wander.life.ui.adapter.WorldCardAdapter;
import com.wander.life.ui.iviews.IWorldView;

/**
 * Created by wander on 2017/1/26.
 */

public class WorldFragment extends PresenterFragment<WorldPresenter> implements IWorldView {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearManager;
    private WorldCardAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.world_layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        setHasOptionsMenu(true);
        Toolbar mToolbar = (Toolbar) mView.findViewById(R.id.world_bar);
        ((AppCompatActivity)mContext).setSupportActionBar(mToolbar);
//        setTitle("不二世界");

        initRecycler();

    }

    private void initRecycler() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.world_recycler);
        mLinearManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearManager);
        mAdapter = new WorldCardAdapter(mContext, WorldInfo.getList());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.world_menu,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.world_menu_search){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected WorldPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new WorldPresenter(mContext, this);
        }
        return mPresenter;
    }

    @Override
    protected void initEventAndData() {

    }
}
