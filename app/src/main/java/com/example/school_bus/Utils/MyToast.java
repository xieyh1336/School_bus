package com.example.school_bus.Utils;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

    private static Context context;

    public static void init(Context context){
        MyToast.context = context;
    }

    public static void showToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
