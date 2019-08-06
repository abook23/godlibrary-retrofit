package com.god.retrofit;

import android.content.Context;
import android.support.annotation.RawRes;

import com.god.retrofit.config.CookieManger;
import com.god.retrofit.config.SSLSocketManger;
import com.god.retrofit.initerceptor.CommonInterceptor;
import com.god.retrofit.initerceptor.LoggingInterceptor;
import com.god.retrofit.util.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.god.retrofit.util.FileUtils.getDiskCacheDir;


/**
 * Created by abook23 on 2016/11/18.
 * Versions 1.0
 */

public class ApiService {

    public static boolean DEBUG = true;
    public static boolean CACHE = false;

    private String baseUrl;
    private Retrofit mRetrofit;
    private int[] mCertificates;
    private static ApiService SERVICE;
    private long readTimeOut = 10;
    private int connectTimeOut = 10;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private List<Interceptor> interceptorList = new ArrayList<>();

    public static ApiService init(Context applicationContext, String baseUrl) {
        AppUtils.initial(applicationContext.getApplicationContext());
        SERVICE = new ApiService();
        SERVICE.baseUrl = baseUrl;
        return SERVICE;
    }

    public static ApiService init(Context applicationContext, String baseUrl, @RawRes int... cerIds) {
        AppUtils.initial(applicationContext.getApplicationContext());
        SERVICE = new ApiService();
        SERVICE.baseUrl = baseUrl;
        SERVICE.mCertificates = cerIds;
        return SERVICE;
    }

    public void addInterceptor(Interceptor interceptor) {
        interceptorList.add(interceptor);
    }

    private Retrofit getRetrofit() {
        if (mRetrofit == null) {
            synchronized (ApiService.class) {
                if (mRetrofit == null) {
                    OkHttpClient okHttpClient = getBuilder().build();
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)//"http://172.16.0.22:8099"
                            .client(okHttpClient)
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return mRetrofit;
    }

    private OkHttpClient.Builder getBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(readTimeOut, timeUnit)
                .connectTimeout(connectTimeOut, timeUnit)
                .cookieJar(new CookieManger(AppUtils.getApplicationContext()));//cookie session 长链
        if (CACHE) {//缓存
            builder.addNetworkInterceptor(new CommonInterceptor())
                    .cache(new Cache(new File(getDiskCacheDir(AppUtils.getApplicationContext()), "httpCache"), 10 * 1024 * 1024));//缓存,可用不用
        }
        if (DEBUG) {//日志监听
            builder.addNetworkInterceptor(new LoggingInterceptor(DEBUG, LoggingInterceptor.LogModel.CONCISE));
        }
        if (mCertificates != null && mCertificates.length > 0) {//https (自定义证书)
            SSLSocketFactory sslSocketFactory = SSLSocketManger.getSSLSocketFactory(AppUtils.getApplicationContext(), mCertificates);
            if (sslSocketFactory != null)
                builder.socketFactory(sslSocketFactory);
        }
        for (Interceptor interceptor : interceptorList) {
            builder.addInterceptor(interceptor);
        }
        return builder;
    }

    public void setTimeOut(long readTimeOut, int connectTimeOut) {
        setTimeOut(readTimeOut, connectTimeOut, TimeUnit.SECONDS);
    }

    public void setTimeOut(long readTimeOut, int connectTimeOut, TimeUnit timeUnit) {
        this.readTimeOut = readTimeOut;
        this.connectTimeOut = connectTimeOut;
        this.timeUnit = timeUnit;
    }

    public static <T> T create(Class<T> tClass) {
        return SERVICE.getRetrofit().create(tClass);
    }

    public static Retrofit retrofit() {
        return SERVICE.getRetrofit();
    }


}
