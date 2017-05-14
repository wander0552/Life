package com.wander.life.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.wander.life.R;
import com.wander.life.bean.Letter;
import com.wander.life.presenter.MailBoxPresenter;
import com.wander.life.ui.adapter.MailBoxAdapter;
import com.wander.life.ui.iviews.IMailBoxView;
import com.wander.life.utils.JumpUtils;

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
        ImageView icon = (ImageView) mView.findViewById(R.id.mail_box_icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.goEdit(mContext);
            }
        });

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

}
