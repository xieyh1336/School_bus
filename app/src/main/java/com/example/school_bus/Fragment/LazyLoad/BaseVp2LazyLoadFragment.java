package com.example.school_bus.Fragment.LazyLoad;

import androidx.fragment.app.Fragment;

import com.example.school_bus.Fragment.BaseFragment;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-03 11:20
 * @类名 BaseVp2LazyLoadFragment
 * @所在包 com\example\school_bus\Fragment\LazyLoad\BaseVp2LazyLoadFragment.java
 * 在viewPager2下的懒加载fragment
 */
public abstract class BaseVp2LazyLoadFragment extends BaseFragment {

    private boolean isLoad = false;

    public abstract void lazyLoad();

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoad){
            lazyLoad();
            isLoad = true;
        }
    }
}
