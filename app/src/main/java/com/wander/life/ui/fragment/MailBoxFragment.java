package com.wander.life.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.wander.life.R;
import com.wander.life.bean.Letter;
import com.wander.life.presenter.MailBoxPresenter;
import com.wander.life.ui.adapter.MailBoxAdapter;
import com.wander.life.ui.iviews.IMailBoxView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wander on 2017/2/7.
 */

public class MailBoxFragment extends PresenterFragment<MailBoxPresenter> implements IMailBoxView {

    private RecyclerView mRecyclerView;
    private MailBoxAdapter mAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

        mPresenter.loadData();

    }

    private void initView() {
        setHasOptionsMenu(true);
        Toolbar mToolbar = (Toolbar) mView.findViewById(R.id.mailbox_bar);
        ((AppCompatActivity)mContext).setSupportActionBar(mToolbar);


        mRecyclerView = (RecyclerView) mView.findViewById(R.id.mailbox_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MailBoxAdapter(mContext, mPresenter.getList());
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected MailBoxPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new MailBoxPresenter(mContext, this);
        }
        return mPresenter;
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mailbox;
    }

    @Override
    public void onLoadSuccess(List<Letter> letters) {
        mAdapter.addData(letters);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mailbox,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
