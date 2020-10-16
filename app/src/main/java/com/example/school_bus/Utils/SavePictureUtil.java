package com.example.school_bus.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 保存图片工具类
 */
public class SavePictureUtil {

    //将Url图片转为Bitmap的方法，该方法不能在主线程中执行，需创建子线程执行！
    public static Bitmap getBitmap(String imgUrl, Context context) {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        URL url;
        try {
            url = new URL(imgUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(2000);
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == 200) {
                //网络连接成功
                inputStream = httpURLConnection.getInputStream();
                outputStream = new ByteArrayOutputStream();
                byte buffer[] = new byte[1024 * 8];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                byte[] bu = outputStream.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bu, 0, bu.length);
                return bitmap;
            }else {
                Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT);
                MyLog.e("SavePicture","网络连接失败----" + httpURLConnection.getResponseCode());
            }
        } catch (Exception e) {
            MyLog.e("SavePicture", e.getMessage());
        }finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    MyLog.e("SavePicture", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    MyLog.e("SavePicture", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    //保存图片的方法
    public static void SaveBitmapFromView(Bitmap bitmap, Context context){
        if (bitmap == null){
            MyLog.e("SavePicture", "bitmap为空");
            return;
        }
        //文件夹
        @SuppressWarnings("deprecation")
        File appDir = new File(Environment.getExternalStorageDirectory(), "School_bus");
        //如果没有文件夹，则创建文件夹
        if (!appDir.exists()){
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";//图片名
        //图片
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);//获取文件流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);//保存为图片
            fos.flush();
            fos.close();
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            //noinspection deprecation
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            Toast.makeText(context, "已保存图片至“School_bus”中",Toast.LENGTH_SHORT);
        } catch (FileNotFoundException e) {
            MyLog.e("SavePicture", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            MyLog.e("SavePicture", e.getMessage());
            e.printStackTrace();
        }
    }
}