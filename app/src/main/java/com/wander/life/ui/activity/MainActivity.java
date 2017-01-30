package com.wander.life.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.DefaultTabEntity;
import com.wander.base.RxUtils.rxbus.EventThread;
import com.wander.base.RxUtils.rxbus.RxBus;
import com.wander.base.RxUtils.rxbus.Subscribe;
import com.wander.life.R;
import com.wander.life.presenter.MainPresenter;
import com.wander.life.test.fragment.LocationFragment;
import com.wander.life.ui.fragment.WorldFragment;
import com.wander.life.ui.iviews.IMainView;
import com.wander.life.ui.listeners.OnFragmentMainListener;

import java.util.ArrayList;


public class MainActivity extends PresenterActivity<MainPresenter> implements IMainView,OnFragmentMainListener {

    private CommonTabLayout mTabLayout;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().register(this);

        initView();


    }

    private void initView() {
        initTab();


    }

    private void initTab() {
        mTabLayout = (CommonTabLayout) findViewById(R.id.main_tab);
        ArrayList<CustomTabEntity> fragmentList = new ArrayList<>();
        fragments = new ArrayList<>();
        //不二 世界 发现 我的
        fragmentList.add(new DefaultTabEntity("不二", R.mipmap.ic_tab_chat_selected, R.mipmap.ic_tab_chat_normal));
        fragments.add(new LocationFragment());
        fragmentList.add(new DefaultTabEntity("世界", R.mipmap.ic_tab_view_selected, R.mipmap.ic_tab_view_normal));
        fragments.add(new WorldFragment());
        fragmentList.add(new DefaultTabEntity("发现", R.mipmap.ic_tab_home_selected, R.mipmap.ic_tab_home_normal));
        fragments.add(new LocationFragment());
        fragmentList.add(new DefaultTabEntity("我的", R.mipmap.ic_tab_me_selected, R.mipmap.ic_tab_me_normal));
        fragments.add(new LocationFragment());


        mTabLayout.setTabData(fragmentList, this, R.id.main_container, fragments);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Subscribe(tag = 100, thread = EventThread.MAIN_THREAD)
    private void changeText(String s) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unRegister(this);
    }

    @Override
    protected MainPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new MainPresenter(this, this);
        }
        return mPresenter;
    }

    @Override
    protected void initEventAndData() {

    }

}
