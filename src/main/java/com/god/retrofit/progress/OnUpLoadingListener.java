package com.god.retrofit.progress;

/**
 * Created by abook23 on 2016/11/22.
 * Versions 1.0
 * 进度监听
 */

public interface OnUpLoadingListener {
    void onProgress(long bytesRead, long contentLength, boolean done);
}
