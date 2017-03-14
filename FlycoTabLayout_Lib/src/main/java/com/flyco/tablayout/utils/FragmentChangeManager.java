package com.flyco.tablayout.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FragmentChangeManager {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;
    /**
     * Fragment切换数组
     */
    private ArrayList<Fragment> mFragments;
    /**
     * 当前选中的Tab
     */
    private int mCurrentTab = -1;

    public FragmentChangeManager(FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments) {
        this.mFragmentManager = fm;
        this.mContainerViewId = containerViewId;
        this.mFragments = fragments;
        List<Fragment> list = fm.getFragments();
        if (list != null && list.size() > 0) {
            Log.e("FragmentChangeManager", list.size() + "");
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            for (Fragment fragment : fragments) {
                fragmentTransaction.remove(fragment);
            }
            fragmentTransaction.commit();
        }
        setFragments(0);
    }

    /**
     * 界面切换控制
     */
    public void setFragments(int index) {
        if (mFragmentManager == null || index < 0 || index >= mFragments.size() || mCurrentTab == index) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragmentList = mFragmentManager.getFragments();
        Fragment newFragment = mFragments.get(index);
        Fragment currentFragment = null;
        if (mCurrentTab >= 0 && mCurrentTab < mFragments.size()) {
            currentFragment = mFragments.get(mCurrentTab);
        }
        if (fragmentList != null && fragmentList.contains(newFragment)) {
            transaction.show(newFragment);
        } else {
            transaction.add(mContainerViewId, newFragment);
        }
        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }
        transaction.commit();
        mCurrentTab = index;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public Fragment getCurrentFragment() {
        return mFragments.get(mCurrentTab);
    }
}