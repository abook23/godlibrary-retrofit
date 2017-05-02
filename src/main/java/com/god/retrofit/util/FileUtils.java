package com.god.retrofit.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by abook23 on 2016/11/22.
 * Versions 1.0
 */

public class FileUtils {

    public static String getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String getSDPath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String getDowloadDir(Context context) {
        return getSDPath(context) + File.separator + "Download";
    }

    public static File saveFile(InputStream inputStream, String parent, String fileName) {
        File file = new File(parent, fileName);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte[] buff = new byte[1024*2];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
        } finally {
            if (os != null) {
                try {
                    os.close();
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}
