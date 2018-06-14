package com.xwards.xview.rest;

import android.util.Log;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * Created by nithinjith.pp on 31-10-2017.
 * API CALLBACK
 */

public abstract class NetworkCallBack<M> extends DisposableObserver<M> {


    private static final String TAG = "NetworkCallback";

    public abstract void onSuccess(M model);

    public abstract void onFailure(String message, String errorCode);

    public abstract void onFinish();


    @Override
    public void onNext(@NonNull M m) {
        onSuccess(m);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        int code = 0;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            code = httpException.code();
            String message = httpException.getMessage();
            Log.i(TAG, "code : " + code);
            onFailure(message, String.valueOf(code));

        } else {
            onFailure(e.getMessage(), String.valueOf(code));
        }
        onFinish();
    }

    @Override
    public void onComplete() {

    }
}
