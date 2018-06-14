package com.xwards.xview.rest;


import com.xwards.xview.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nithinjith on 11-08-2017.
 * REST CORE CLASS - Using RETROFIT
 */

public class NetworkClient {

    private static Retrofit retrofit = null;


    public static Retrofit getRetrofit() {

        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Accept", "Application/JSON").
                                                addHeader("Content-Type",
                                                        "Application/JSON").
                                                addHeader("api_key", "123")
                                        .
                                                build();
                                return chain.proceed(request);
                            }
                        }).build();

        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            OkHttpClient okHttpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.XViewAPI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(defaultHttpClient)
                    .build();
        }
        return retrofit;
    }


}