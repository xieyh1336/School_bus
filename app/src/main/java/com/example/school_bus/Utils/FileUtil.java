package com.example.school_bus.Utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.gson.internal.$Gson$Types;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

    /**
     * 保存图片的方法
     * 已适配Android10
     * @param bitmap 图片
     * @param context 上下文
     * @return 是否保存成功
     */
    public static boolean SaveBitmapFromView(Bitmap bitmap, Context context){
        if (bitmap == null){
            MyLog.e("SavePicture", "bitmap为空");
            return false;
        }
        String fileName = System.currentTimeMillis() + ".jpg";//图片名
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            //文件夹
            File appDir = new File(Environment.DIRECTORY_PICTURES, "蓝梦地图");
            //如果没有文件夹，则创建文件夹
            if (!appDir.exists()){
                if (appDir.mkdir()){
                    MyLog.e(TAG, "文件夹创建成功");
                } else {
                    MyLog.e(TAG, "文件夹创建失败");
                    return false;
                }
            }
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
                return true;
            } catch (IOException e) {
                MyLog.e("SavePicture", e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
            //Android10
            Uri contentUri;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else {
                contentUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
            }
            ContentValues contentValues = new ContentValues();
            ContentResolver resolver = context.getContentResolver();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/蓝梦地图/");
            //告诉系统，文件还未准备好，暂时不对外暴露
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 1);
            Uri uri = resolver.insert(contentUri, contentValues);
            if (uri == null){
                return false;
            }
            try (OutputStream os = resolver.openOutputStream(uri)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                //告诉系统，文件准备好了，可以提供给外部了
                contentValues.clear();
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0);
                resolver.update(uri, contentValues, null, null);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                //失败的时候，删除此Uri记录
                resolver.delete(uri, null, null);
                return false;
            }
        }
    }

    /**
     * Uri转File的方法
     * 已适配Android10
     * @param uri uri
     * @param context 上下文
     * @return file
     */
    public static File uriToFile(Uri uri, Context context){
        File file = null;
        if (uri == null){
            return null;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            //Android10以下
            try {
                @SuppressLint("Recycle")
                Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    String name = cursor.getString(index);
                    file = new File(context.getFilesDir(), name);
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    int read;
                    int maxBufferSize = 1024 * 1024;
                    if (inputStream != null) {
                        int bytesAvailable = inputStream.available();
                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        final byte[] buffers = new byte[bufferSize];
                        while ((read = inputStream.read(buffers)) != -1){
                            outputStream.write(buffers, 0, read);
                        }
                        cursor.close();
                        inputStream.close();
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //Android10以上
            if (uri.getScheme() != null){
                if (uri.getScheme().equals(ContentResolver.SCHEME_FILE) && uri.getPath() != null){
                    file = new File(uri.getPath());
                } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)){
                    //将文件复制到沙盒目录中
                    ContentResolver contentResolver = context.getContentResolver();

                    String displayName = System.currentTimeMillis() + Math.round((Math.random() + 1) * 1000)
                            + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));
                    //以下方法可以获取原文件的文件名，但是比较耗时
//                    @SuppressLint("Recycle")
//                    Cursor cursor = contentResolver.query(uri, null, null, null, null);
//                    if ( cursor != null && cursor.moveToFirst()){
//                        String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                    }

                    try {
                        InputStream inputStream = contentResolver.openInputStream(uri);
                        if (context.getExternalCacheDir() != null){
                            File cache = new File(context.getExternalCacheDir().getAbsolutePath(), displayName);
                            FileOutputStream fileOutputStream = new FileOutputStream(cache);
                            if (inputStream != null) {
                                FileUtils.copy(inputStream, fileOutputStream);
                                file = cache;
                                fileOutputStream.close();
                                inputStream.close();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return file;
    }

    /**
     * 将drawable转换为bitmap
     * @param drawable drawable
     * @return bitmap
     */
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

    /**
     * 将Uri转化为path路径
     * @param context 上下文
     * @param uri Uri
     * @return path路径
     */
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
