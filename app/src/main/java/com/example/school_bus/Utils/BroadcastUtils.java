package com.example.school_bus.Utils;

import android.content.Context;
import android.content.Intent;

import com.example.school_bus.Activity.BaseActivity;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-04 15:09
 * @类名 BroadcastUtils
 * @所在包 com\example\school_bus\Utils\BroadcastUtils.java
 * 广播工具类
 */
public class BroadcastUtils {

    /**
     * 结束activity广播
     * @param context context
     */
    public static void sendFinishActivityBroadcast(Context context){
        Intent intent = new Intent(BaseActivity.RECEIVER_ACTION_FINISH_MAIN);
        context.sendBroadcast(intent);
        intent = new Intent(BaseActivity.RECEIVER_ACTION_FINISH_OFFLINE_MAP);
        context.sendBroadcast(intent);
    }
}
