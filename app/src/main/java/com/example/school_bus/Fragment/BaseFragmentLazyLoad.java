package com.example.school_bus.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.school_bus.R;

import java.util.Calendar;


public abstract class BaseFragmentLazyLoad extends Fragment {
    protected Dialog loadingDialog;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private View mToastView;

    private boolean isFragmentVisible;
    private boolean isReuseView;
    private boolean isFirstVisible;
    private View rootView;

    public void showToast(String msg){
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    /**
     *将显示Dialog的方法封装在这里面
     */
    public Dialog showLoading(String msg) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.dialog_loading, null); // 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_loading_view); // 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView); // 提示文字
        tipTextView.setText(msg); // 设置加载信息
        loadingDialog = new Dialog(getContext(), R.style.MyDialogStyle); // 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)); // 设置布局
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * 关闭dialog
     */
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (rootView==null){
            rootView=view;
            if (getUserVisibleHint()){
                if (isFirstVisible){
                    onFragmentFirstVisible();
                    isFirstVisible=false;
                }
                onFragmentVisibleChange(true);
                isFragmentVisible=true;
            }
        }
        super.onViewCreated(isReuseView?rootView:view, savedInstanceState);
        lazyLoad();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
    //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
    //如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
    //总结: setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
    //如果我们需要在Fragment可见与不可见时干点事，用这个的话就会有多余的回调了，那么就需要重新封装一个
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用
        if (rootView==null){
            return;
        }
        if (isFirstVisible&&isVisibleToUser){
            onFragmentFirstVisible();
            isFirstVisible=false;
        }
        if (isVisibleToUser){
            onFragmentVisibleChange(true);
            isFragmentVisible=true;
            return;
        }
        if (isFragmentVisible){
            isFragmentVisible=false;
            onFragmentVisibleChange(false);
        }
    }

    private void initVariable(){
        isFirstVisible=true;
        isFragmentVisible=false;
        rootView=null;
        isReuseView=true;
    }


    /**
     *设置是否使用view的复用，默认开启
     * view的复用是指，ViewPager 在销毁和重建Fragment时会不断调用onCreateView()-〉onDestroyView()
     *之间的生命函数，这样可能会出现重复创建view 的情况，导致界面上显示多个相同的 Fragment
     * view的复用其实就是指保存第一次创建的view，后面再onCreateView(）时直接返回第一次创建的view
     *@param isReuse
     */
    protected void reuseView(boolean isReuse){
        isReuseView=isReuse;
    }

    /**
     *去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     *回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     *可在该回调方法里进行一些ui显示与隐藏，比如加载框的显示和隐藏
     *@param isVisible true不可见->可见
     *false可见―->不可见
     */
    protected abstract void onFragmentVisibleChange(boolean isVisible);

    /**
     *在fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
     *这样就可以防止每次进入都重复加载数据
     *该方法会在onFragmentVisibleChange()之前调用，所以第一次打开时，可以用一个全局变量表示数据下载状态，
     *然后在该方法内将状态设置为下载状态，接着去执行下载的任务
     *最后在onFragmentVisibleChange(）里根据数据下载状态来控制下载进度ui控件的显示与隐藏
     */
    protected void onFragmentFirstVisible(){

    }

    protected boolean isFragmentVisible(){
        return isFragmentVisible;
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

        }
    }

    protected  void loadData(){}

}
