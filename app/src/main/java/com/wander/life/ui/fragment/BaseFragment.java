package com.wander.life.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.wander.base.log.WLog;


/**
 * Created by wander on 2016/6/15.
 * email 805677461@qq.com
 */
public abstract class BaseFragment extends Fragment {
    protected View mView;
    protected Activity mContext;
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected String TAG = getClass().getName();
    private boolean umengCount = false;
    private String umengName;

    private boolean mHasExecuteInOnCreateView;
    private boolean mExecuteOnCreateView;

    public BaseFragment() {
    }

    /**
     * 当onCreateView执行后，立马被执行
     */
    protected void executeFollowOnVisible(){}

    /**
     * 当网络加载数据成功以后，自动被回调,用于绘制UI<br>
     * 子类只需要将以前在onCreateView实现的代码，放到这个方法做即可<br>
     * <p>
     * //     * @param inflater
     * //     * @param data
     * 网络请求成功后返回的String数据
     *
     * @return
     */
//    protected abstract View onCreateContentView(LayoutInflater inflater, String data);
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mExecuteOnCreateView = true;
        mContext = getActivity();
        initEventAndData();
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        initBar();
        return mView;
    }

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!mExecuteOnCreateView) {
            return;
        }
        WLog.i(TAG, " [setUserVisibleHint] isVisibleToUser " + isVisibleToUser);
        if (isVisibleToUser && !mHasExecuteInOnCreateView) {
            executeFollowOnVisible();
            mHasExecuteInOnCreateView = true;
        } else if (isVisibleToUser) {
            onVisibleInViewPager();
        } else if (!isVisibleToUser) {
            onInVisibleInViewPager();
        }
    }

    /**
     * 如果该Fragment在ViewPager里面,当Fragment可见的时候自动被回调(不包含第一次默认可见那次,必须是手动选中或者滑动选中)
     */
    protected void onVisibleInViewPager() {
        WLog.w(TAG, " [onVisibleInViewPager]");
    }

    /**
     * 如果该Fragment在ViewPager里面,当Fragment不可见的时候自动被回调
     */
    protected void onInVisibleInViewPager() {
        WLog.w(TAG, " [onInVisibleInViewPager]");
    }


    @Override
    public void onResume() {
        super.onResume();
        if (umengCount)
            MobclickAgent.onPageStart(umengName); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        if (umengCount)
            MobclickAgent.onPageEnd(umengName);
    }

    /**
     * 统计直接由activity实现的页面
     * 在onCreate中调用
     *
     * @param name 自定义的activity名字
     */
    protected void countThisFragment(String name) {
        umengCount = true;
        umengName = name;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected abstract int getLayoutId();

    public void initBar() {
    }

    protected void setTitle(CharSequence title) {

    }

    protected void showEmptyTip(String message) {

    }

    protected void onNetFailReload() {

    }


    /**
     * onCreate中执行
     */
    protected void initEventAndData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
        }
    }


}
