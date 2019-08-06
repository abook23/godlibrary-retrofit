package com.god.retrofit.rxjava;


import android.widget.Toast;

import com.god.retrofit.util.AppUtils;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by abook23 on 2016/11/18.
 * Versions 1.0
 */

public abstract class ObserverBaseWeb<T> implements Observer<T> {

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        String errorMsg;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            errorMsg = httpException.code() + httpException.message();
        } else if (e instanceof IOException) {
            String msg = e.getMessage();
            if (msg.startsWith("Failed to connect to")) {
                errorMsg = "链接服务器失败";
            } else {
                errorMsg = "Please check your network status\n" + e.getMessage();
                e.printStackTrace();
            }
        } else {
            errorMsg = e.getMessage();
            e.printStackTrace();
        }
        if (AppUtils.getApplicationContext() != null)
            Toast.makeText(AppUtils.getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

}
