package com.god.retrofit.progress;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by abook23 on 2016/11/22.
 * Versions 1.0
 */

public class HttpHelper {
    /**
     * 包装OkHttpClient，用于下载文件的回调
     *
     * @param listener 进度回调接口
     * @return 包装后的OkHttpClient
     */
    public static OkHttpClient addProgressResponseListener(final OnDownloadListener listener) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        //增加拦截器
        client.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), listener))
                        .build();
            }
        });
        return client.build();
    }


    /**
     * 包装OkHttpClient，用于上传文件的回调
     *
     * @param progressListener 进度回调接口
     * @return 包装后的OkHttpClient
     */
    public static OkHttpClient addProgressRequestListener(final OnUpLoadingListener progressListener) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        //增加拦截器
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), new ProgressRequestBody(original.body(), progressListener))
                        .build();
                return chain.proceed(request);
            }
        });
        return client.build();
    }
}
