package com.lfjmgs.networkutils.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lfjmgs.networkutils.ApiService;
import com.lfjmgs.networkutils.BuildConfig;


import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liufang03 on 2017/7/17.
 */

public class RetrofitUtils {

    public static final String BASE_URL = ApiService.BASE_URL;
    public static final int TIMEOUT = 60;

    public static <T> T createService(Class<T> serviceClass) {
        return getRetrofit().create(serviceClass);
    }

    private static Retrofit sRetrofit;

    public static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = buildRetrofit();
        }
        return sRetrofit;
    }

    @NonNull
    private static Retrofit buildRetrofit() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new CommonHeadersInterceptor()) // 添加共同的请求头
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS); // 设置超时时间
        // debug环境添加log
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(logging);
        }
        OkHttpClient client = httpClientBuilder.build();

        Gson gson = new GsonBuilder().create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(new ObserveOnMainCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(DataMapGsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client).build();
    }
}
