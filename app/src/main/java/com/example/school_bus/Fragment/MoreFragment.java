package com.example.school_bus.Fragment;

/**
 * 更多界面
 */
public class MoreFragment extends BaseFragment{
    private static MoreFragment moreFragment;

    public static MoreFragment getInstance(){
        if (moreFragment==null){
            moreFragment=new MoreFragment();
        }
        return moreFragment;
    }
}
