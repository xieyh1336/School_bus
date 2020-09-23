package com.example.school_bus.Fragment;

/**
 * 导航界面
 */
public class NavigationFragment extends BaseFragment{
    private static NavigationFragment navigationFragment;

    public static NavigationFragment getInstance(){
        if (navigationFragment==null){
            navigationFragment=new NavigationFragment();
        }
        return navigationFragment;
    }
}
