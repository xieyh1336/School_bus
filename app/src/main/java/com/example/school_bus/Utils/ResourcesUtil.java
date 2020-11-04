package com.example.school_bus.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 资源工具类
 */
public class ResourcesUtil {

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
        } catch (FileNotFoundException e) {
            MyLog.e("SavePicture", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            MyLog.e("SavePicture", e.getMessage());
            e.printStackTrace();
        }
    }

    //从assets中获取资源文件
    public static String getFromAssets(String fileName, Context context){
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //将drawable转换为bitmap
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        return bitmap;
    }
}
