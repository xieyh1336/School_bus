package com.example.school_bus.Fragment;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.school_bus.R;

import java.util.Calendar;

public class BaseFragment extends Fragment {
    protected Dialog loadingDialog;

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

    /**
     * 防止多次点击
     */
    public abstract class NoDoubleClickListener implements View.OnClickListener{

        public static final int MIN_CLICK_DELAY_TIME = 1000;   //点击时间间隔
        private long lastClickTime = 0;

        @Override
        public void onClick(View view) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if( (currentTime-lastClickTime) > MIN_CLICK_DELAY_TIME ){
                lastClickTime = currentTime;
                onNoDoubleClick(view);
            }
        }
        public abstract void onNoDoubleClick(View view);
    }
}
