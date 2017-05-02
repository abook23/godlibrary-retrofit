package com.god.retrofit.initerceptor;

import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by abook23 on 2016/11/21.
 * Versions 1.0
 */

public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "okhttp_log";
    private boolean showLog;
    private LogModel mLogModel;

    public LoggingInterceptor() {
        this.showLog = true;
        this.mLogModel = LogModel.concise;
    }
    public LoggingInterceptor(boolean showLog, LogModel logModel) {
        this.showLog = showLog;
        this.mLogModel = logModel;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        //Request request = chain.request();
        // Log.v(TAG, "request:" + request.toString());

        long t1 = System.nanoTime();
        Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();

        if (showLog) {
            switch (mLogModel) {
                case all:
                    Log.i(TAG, String.format(Locale.getDefault(),
                            "Received response for %s in %.1fms%n%s%n%s",
                            response.request().url(), (t2 - t1) / 1e6d, response.headers(),content));
                   // Log.i(TAG, content);
                    break;
                case concise:
                    Log.i(TAG, String.format(Locale.getDefault(),
                            "%s in %.1fms%n%s",
                            response.request().url(), (t2 - t1) / 1e6d,content));
                    //Log.i(TAG, content);
                    break;
                default:
                    break;
            }
        }
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
   //     return response;
    }

    public enum LogModel {
        all, concise
    }
}
