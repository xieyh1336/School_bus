package com.example.school_bus.Utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.gson.internal.$Gson$Types;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.temporal.TemporalAccessor;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-06 16:37
 * @类名 FileUtil
 * @所在包 com\example\school_bus\Utils\FileUtil.java
 * 资源工具类
 */
public class FileUtil {

    private static String TAG = "FileUtil";

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
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
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

    //将Uri转化为path
    public static String getFilePathByUri(Context context, Uri uri){
        if ("content".equalsIgnoreCase(uri.getScheme())){
            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19){
                return getRealPathFromUriAboveApi19(context, uri);
            } else {
                return getRealPathFromUriBelowApi19(context, uri);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }

    /**
     * 适配api19及以上，根据Uri获取图片的绝对路径
     */
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri){
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)){
            //如果是Document类型的uri，则通过Document，id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)){
                //使用':'分割
                String type = documentId.split(":")[0];
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};

                Uri contentUri = null;
                if ("image".equals(type)){
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }else if ("video".equals(type)){
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }else if ("audio".equals(type)){
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            } else if (isExternalStorageDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)){
                    filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else {
                MyLog.e(TAG, "路径错误");
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 适配api19以下（不包括19），根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUriBelowApi19(Context context, Uri uri){
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的_data列，即返回Uri对应的文件路径
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs){
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()){
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e){
            if (cursor != null){
                cursor.close();
            }
        }
        return path;
    }


    private static boolean isMediaDocument(Uri uri){
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri){
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri){
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 格式化单位
     */
    public static String getFormatSize(long size){
        long kiloByte = size / 1024;
        if (kiloByte < 1){
            return size + "B";
        }

        long megaByte = kiloByte / 1024;
        if (megaByte < 1){
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
        }

        long gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }

        long teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }
}
