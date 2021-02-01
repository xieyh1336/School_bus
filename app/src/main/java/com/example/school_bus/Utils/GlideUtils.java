package com.example.school_bus.Utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;

import java.io.File;
import java.math.BigDecimal;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-01 17:41
 * @类名 GlideUtils
 * @所在包 com\example\school_bus\Utils\GlideUtils.java
 * Glide框架工具类
 */
public class GlideUtils {
    private static GlideUtils glideUtils;

    public static GlideUtils getInstance(){
        if (glideUtils == null){
            glideUtils = new GlideUtils();
        }
        return glideUtils;
    }

    /**
     * 清楚图片磁盘缓存
     */
    public void clearImageDiskCache(Context context){
        try {
            if (Looper.myLooper() == Looper.getMainLooper()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(Context context){
        try {
            if (Looper.myLooper() == Looper.getMainLooper()){
                Glide.get(context).clearMemory();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context){
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
        String ImageExternalCatchDir = context.getExternalCacheDir() + ExternalPreferredCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
        deleteFolderFile(ImageExternalCatchDir, true);
    }

    /**
     * 获取Glide造成的缓存大小
     */
    public String getCacheSize(Context context){
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/" + ExternalPreferredCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     */
    public long getFolderSize(File file) throws Exception{
        long size = 0;
        try {
            File[] files = file.listFiles();
            for (File aFileList : files){
                if (aFileList.isDirectory()){
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     */
    public void deleteFolderFile(String filePath, boolean deleteThisPath){
        if (!TextUtils.isEmpty(filePath)){
            try {
                File file = new File(filePath);
                if (file.isDirectory()){
                    File files[] = file.listFiles();
                    for (File file1 : files){
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()){
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0){
                            file.delete();
                        }
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     */
    private static String getFormatSize(double size){
        double kiloByte = size / 1024;
        if (kiloByte < 1){
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1){
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
