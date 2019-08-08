package com.god.retrofit.listener;

import com.god.retrofit.rxjava.ObserverBaseWeb;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

public abstract class Call<T> extends ObserverBaseWeb<ResponseBody> {

    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(responseBody.string());
            Type type = getClass().getGenericSuperclass();
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            T result;
            if (types[0] instanceof Class) {
                Class<T> c = (Class<T>) types[0];
                result = gson.fromJson(json, c);
            } else {
                Type type1 = ((ParameterizedType) types[0]).getRawType();
                Type[] type2 = ((ParameterizedType) types[0]).getActualTypeArguments();
                Type ty = new ParameterizedTypeImpl((Class) type1, new Type[]{type2[0]});
                result = gson.fromJson(json, ty);
            }
            onSuccess(result);
        } catch (IOException e) {
            e.printStackTrace();
            onError(e);
        }
    }

    public abstract void onSuccess(T t);

    private class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
